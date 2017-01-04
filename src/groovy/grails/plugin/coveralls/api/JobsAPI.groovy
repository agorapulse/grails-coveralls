package grails.plugin.coveralls.api

import grails.plugin.coveralls.util.HTTPClientHelper
import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import grails.plugin.coveralls.git.GitReport

import static groovyx.net.http.Method.POST

class JobsAPI {

    static API_PROTOCOL = 'https'
    static API_HOST = API_PROTOCOL + '://coveralls.io'
    static API_PATH = '/api/v1/jobs'

    def eventListener

    JobsAPI(eventListener) {
        this.eventListener = eventListener
    }

    def create(String serviceName, String serviceJobId, String repoToken, List sourceReports, GitReport gitReport = null) {
        Report report = new Report(
                service_name: serviceName,
                service_job_id: serviceJobId,
                repo_token: repoToken,
                source_files: sourceReports,
                git: gitReport
        )

        String json = report.toJson()

        HTTPBuilder http = new HTTPBuilder(API_HOST + API_PATH)
        HTTPClientHelper.configureProxy(http, API_HOST, API_PROTOCOL)
        http.request(POST) { req ->
            req.entity = MultipartEntityBuilder.create()
                    .addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file')
                    .build()

            response.success = { resp, reader ->
                assert resp.status == 200
                return true
            }

            response.failure = { resp, reader ->
                eventListener?.triggerEvent("StatusError", "Could not post coverage reports: ${resp.statusLine.toString()}")
                return false
            }
        }
    }

}