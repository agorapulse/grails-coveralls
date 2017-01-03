package grails.plugin.coveralls.api

import grails.plugin.coveralls.coverage.SourceReport
import grails.plugin.coveralls.git.GitReport
import groovy.json.JsonBuilder

/**
 * The model class of the report for Coveralls' format.
 */
class Report {
	String service_job_id
	String service_name
	String repo_token
	List<SourceReport> source_files
    GitReport git // Optional

    public Map ignoreNull() {
        def result = [
                service_job_id: service_job_id,
                service_name: service_name,
                repo_token: repo_token,
                source_files: source_files
        ]
        if (git) {
            result['git'] = git
        }
        return result
    }

    public String toJson() {
        JsonBuilder json = new JsonBuilder(this.ignoreNull())
        return json.toString()
    }

}
