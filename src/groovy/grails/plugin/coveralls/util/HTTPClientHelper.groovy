package grails.plugin.coveralls.util

import groovyx.net.http.HTTPBuilder
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials

class HTTPClientHelper {

    static configureProxy(HTTPBuilder http, String url, String protocol) {
        if (System.getProperty("${protocol}.proxyHost") && url.startsWith("${protocol}:")) {
            String proxyHost = System.getProperty("${protocol}.proxyHost")
            Integer proxyPort = Integer.parseInt(System.getProperty("${protocol}.proxyPort", (protocol == 'http' ? 80 : 443)))
            String proxyUser = System.getProperty("${protocol}.proxyUser")
            String proxyPassword = System.getProperty("${protocol}.proxyPassword", '')

            if (proxyUser) {
                http.client.getCredentialsProvider().setCredentials(
                        new AuthScope(proxyHost, proxyPort),
                        new UsernamePasswordCredentials(proxyUser, proxyPassword)
                )
            }
            http.setProxy(proxyHost, proxyPort, protocol)
        }
    }
}
