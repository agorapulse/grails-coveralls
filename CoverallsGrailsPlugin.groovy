class CoverallsGrailsPlugin {

    def version = "0.1"
    def grailsVersion = "2.3 > *"
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Benoit Hediard"
    def authorEmail = "ben@benorama.com"
    def title = "Coveralls.io Grails Plugin"
    def description = '''The Coveralls Plugin allows you to send Grails code coverage report to Coveralls.
Coveralls works with your continuous integration server to give you test coverage history and statistics.'''

    def documentation = "http://grails.org/plugin/grails-coveralls"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-open-exchange-rates/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-open-exchange-rates" ]

}
