grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.fork = [
        //test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true], // configure settings for the test-app JVM
        console: false
]

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        compile('org.codehaus.groovy.modules.http-builder:http-builder:0.7') {
            excludes 'commons-logging', 'xml-apis', 'groovy'
        }
        compile 'org.apache.httpcomponents:httpmime:4.3.3'
    }
    plugins {
        build(':release:3.0.1',
               ':rest-client-builder:1.0.3') {
            export = false
        }
        test(':code-coverage:1.2.7') {
            export = false
        }
    }
}
