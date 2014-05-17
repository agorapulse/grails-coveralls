includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsClasspath")

USAGE = """
    coveralls [--report=REPORT] [--token=TOKEN] [--service=SERVICE]

where
    REPORT          = Cobertura XML coverage report.
                    (default: grails.plugin.coveralls.report or 'target/test-reports/cobertura/coverage.xml')

    TOKEN           = Coveralls repo token, not required for Travis CI (required for Travis Pro or other CI).
                    (default: grails.plugin.coveralls.token or COVERALLS_REPO_TOKEN env var)

    SERVICE         = Service name, not required for Travis (automatically detected).
                    (default: grails.plugin.coverals.service)
"""

target(coveralls: "Create coverage report and post it to Coveralls.io") {
    depends(parseArguments)

    if (argsMap['help']) {
        println USAGE
        exit 0
    }

    def coverallsConfig = config.grails.plugin?.coveralls
    String reportPath = argsMap['report'] ?: coverallsConfig?.report ?: 'target/test-reports/cobertura/coverage.xml'
    String repoToken = argsMap['token'] ?: coverallsConfig?.token ?: System.getenv('COVERALLS_REPO_TOKEN')
    String serviceName = argsMap['service'] ?: coverallsConfig?.service ?: ''
    def serviceJobId

    if (repoToken) {
        if (System.getenv('TRAVIS') == 'true' && System.getenv('TRAVIS_JOB_ID') != null) {
            serviceName = 'travis-pro'
            serviceJobId = System.getenv('TRAVIS_JOB_ID')
        } else if (!serviceName) {
            serviceName = 'other'
        }
    } else {
        if (System.getenv('TRAVIS') == 'true' && System.getenv('TRAVIS_JOB_ID') != null) {
            serviceName = 'travis-ci'
        }
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
    def success = jobsAPI.create(serviceName, serviceJobId, repoToken, sourceReports)
    if (!success) {
        exit 1
    }

    event("StatusFinal", ["Coverage reports sent successfully!"])

}

setDefaultTarget(coveralls)
