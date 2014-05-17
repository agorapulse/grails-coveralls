package grails.plugin.coveralls.api

import grails.plugin.coveralls.service.ServiceInfo
import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder

import static groovyx.net.http.Method.POST

class JobsAPI {

    static API_HOST = 'https://coveralls.io'
    static API_PATH = '/api/v1/jobs'

    def create(ServiceInfo serviceInfo, List sourceReports) {
        Report report = new Report(serviceInfo, sourceReports)
        String json = report.toJson()

        HTTPBuilder http = new HTTPBuilder(API_HOST + API_PATH)
        /*http.post(body: [test: true]) { resp ->

            println "POST Success: ${resp.statusLine}"
            assert resp.statusLine.statusCode == 201
        }*/


        http.request(POST) { req ->
            req.entity = MultipartEntityBuilder.create()
                    .addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file')
                    .build()

            /*response.success = { resp, reader ->
                //this.logger.info resp.statusLine.toString()
                //this.logger.info resp.getAllHeaders().toString()
                System.out << reader
            }

            response.failure = { resp, reader ->
                //this.logger.error resp.statusLine.toString()
                //this.logger.error resp.getAllHeaders().toString()
                System.out << reader
            }*/
        }
    }

}