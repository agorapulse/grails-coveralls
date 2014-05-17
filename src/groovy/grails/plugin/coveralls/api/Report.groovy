package grails.plugin.coveralls.api

import grails.plugin.coveralls.service.ServiceInfo
import grails.plugin.coveralls.coverage.SourceReport
import groovy.json.JsonBuilder

/**
 * The model class of the report for Coveralls' format.
 */
class Report {

	String service_job_id
	String service_name
	String repo_token
	List<SourceReport> source_files

    public Report(ServiceInfo serviceInfo, List<SourceReport> sourceFiles) {
        this.service_name = serviceInfo.serviceName;
        this.service_job_id = serviceInfo.serviceJobId;
        this.repo_token = serviceInfo.repoToken;
        this.source_files = sourceFiles;
    }

    public String toJson() {
        JsonBuilder json = new JsonBuilder(this)
        return json.toString()
    }

}
