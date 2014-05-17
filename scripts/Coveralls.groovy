includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsClasspath")

target(coveralls: "The description of the script goes here!") {
    depends(parseArguments)

    COBERTURA_REPORT_PATH = 'target/test-reports/cobertura/coverage.xml'

    //ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar()
    def serviceInfoFactory = classLoader.loadClass('grails.plugin.coveralls.service.ServiceInfoFactory').newInstance()
    def serviceInfo = serviceInfoFactory.createFromEnvVar()

    if (serviceInfo == null) {
        println 'no available CI service'
        return
    }

    println 'service name: ' + serviceInfo.serviceName
    println 'service job id: ' + serviceInfo.serviceJobId
    if (serviceInfo.repoToken != null) {
        println 'repo token: present'
    } else {
        println 'repo token: null'
    }

    File file = new File(COBERTURA_REPORT_PATH)
    if (!file.exists()) {
        println 'No cobertura report found at: ' + file.absolutePath
        return
    }

    println 'Cobertura report file: ' + file.absolutePath

    def coberturaSourceReportFactory = classLoader.loadClass('grails.plugin.coveralls.coverage.CoberturaSourceReportFactory').newInstance()
    def sourceReports = coberturaSourceReportFactory.createReportList(file)

    if (sourceReports.size == 0) {
        println "No source file found with coverage file:  $COBERTURA_REPORT_PATH"
        return
    }

    def jobsAPI = classLoader.loadClass('grails.plugin.coveralls.api.JobsAPI').newInstance()
    jobsAPI.create(serviceInfo, sourceReports)

    println 'done'
}

setDefaultTarget(coveralls)
