package grails.plugin.coveralls.api

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

    public String toJson() {
        JsonBuilder json = new JsonBuilder(this)
        return json.toString()
    }

}
