includeTargets << grailsScript("_GrailsBootstrap")

USAGE = """
    coveralls [--report=REPORT] [--token=TOKEN] [--service=SERVICE] [--gitSync]

where
    REPORT          = Cobertura XML coverage report.
                    (default: grails.plugin.coveralls.report or 'target/test-reports/cobertura/coverage.xml')

    TOKEN           = Coveralls repo token, not required for Travis CI (required for Travis Pro or other CI).
                    (default: grails.plugin.coveralls.token or COVERALLS_REPO_TOKEN env var)

    SERVICE         = Service name, not required for Travis (automatically detected).
                    (default: grails.plugin.coverals.service)

    GITSYNC        = if present will send Git information to coveralls (automatically detected)
                    (default: false)
"""

target(coveralls: "Create coverage report and post it to Coveralls.io") {
    depends(compile, parseArguments)

    if (argsMap['help']) {
        println USAGE
        exit 0
    }

    def coverallsConfig = config.grails.plugin?.coveralls
    String reportPath = argsMap['report'] ?: coverallsConfig?.report ?: 'target/test-reports/cobertura/coverage.xml'
    String repoToken = argsMap['token'] ?: coverallsConfig?.token ?: System.getenv('COVERALLS_REPO_TOKEN')
    String serviceName = argsMap['service'] ?: coverallsConfig?.service ?: ''
    Boolean gitSync = argsMap['gitSync'] ?: coverallsConfig?.gitSync ?: false

    def serviceJobId

    if (System.getenv('TRAVIS') == 'true' && System.getenv('TRAVIS_JOB_ID') != null) {
        serviceName = repoToken ? 'travis-pro' : 'travis-ci'
        serviceJobId = System.getenv('TRAVIS_JOB_ID')
    } else if (repoToken) {
        // RepoToken is required for CI service
        serviceName = 'other'
    }

    if (!serviceName) {
        event("StatusError", ["No available CI service, use 'grails help coveralls' to show usage."])
        exit 1
    }

    event("StatusUpdate", ["Service Name: $serviceName"])
    event("StatusUpdate", ["Service Job ID: $serviceJobId"])
    event("StatusUpdate", ["Coveralls Repo Token: ${repoToken ? 'found' : 'null'}"])

    File file = new File(reportPath)
    if (!file.exists()) {
        event("StatusError", ["No cobertura report found at: ${file.absolutePath}."])
        exit 1
    }

    event("StatusUpdate", ["Coverage Report: $file.absolutePath"])

    def coberturaSourceReportFactory = classLoader.loadClass('grails.plugin.coveralls.coverage.CoberturaSourceReportFactory').newInstance()
    def sourceReports = coberturaSourceReportFactory.createReportList(file)

    if (sourceReports.size == 0) {
        event("StatusError", ["No source file found in coverage report ${file.absolutePath}."])
        exit 1
    }
    def jobsAPI = classLoader.loadClass('grails.plugin.coveralls.api.JobsAPI').newInstance(eventListener)

    def success
    if (gitSync) {
        event("StatusUpdate", ["Collecting git information to send to coveralls"])
        def gitInfoFactory = classLoader.loadClass('grails.plugin.coveralls.git.GitInfoFactory').newInstance()
        gitReport = gitInfoFactory.createReport()
        success = jobsAPI.create(serviceName, serviceJobId, repoToken, sourceReports, gitReport)
    } else {
        success = jobsAPI.create(serviceName, serviceJobId, repoToken, sourceReports)
    }

    if (!success) {
        exit 1
    }

    event("StatusFinal", ["Coverage reports sent successfully!"])

}

setDefaultTarget(coveralls)
