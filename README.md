
Coveralls Grails Plugin
=======================

# Introduction

The Coveralls Plugin allows you to send [Grails code coverage](http://grails.org/plugin/code-coverage) report to [Coveralls](http://coveralls.io).
[Coveralls](http://coveralls.io) works with your continuous integration server to give you test coverage history and statistics.

It works pretty well with [Travis CI](http://travis-ci.org) and [Travis Pro](http://travis-ci.com).

It adds the following [Grails](http://grails.org) Gant scripts:

- *coveralls* to send coverage report to [Coveralls](http://coveralls.io).

# Installation

Declare the plugin dependency in the BuildConfig.groovvy file, as shown here:

```groovy
grails.project.dependency.resolution = {
		inherits("global") { }
		log "info"
		repositories {
                //your repositories
        }
        dependencies {
                // Latest httpcore and httpmime for Coveralls plugin
                build 'org.apache.httpcomponents:httpcore:4.3.2'
                build 'org.apache.httpcomponents:httpclient:4.3.2'
                build 'org.apache.httpcomponents:httpmime:4.3.3'
        }
		plugins {
				// Coveralls plugin
				build(':coveralls:0.1', ':rest-client-builder:1.0.3') {
				    export = false
				}
				test(':code-coverage:1.2.7') {
                    export = false
                }
		}
}
```


# Config

Create a [Coveralls.io](http://coveralls.io) account and add the [GitHub](http://github.com) repositories you want to monitor.

You can add your config in **BuildConfig.groovy** but it is not required, especially when running Travis CI.
All parameters can be passed as arguments to *coveralls* Gant script.

```groovy
grails {
    plugin {
        coveralls {
            // Cobertura XML coverage report path
            report = 'path/to/cobertura.xml' // if not defined, default to 'target/test-reports/cobertura/coverage.xml'
            // Coveralls repo token, not required for Travis CI public repo (required for Travis Pro with private repo or other CI).
            token = '...'
            // CI Service name (not required for Travis, automatically detected for 'travis-ci' and 'travis-pro')
            service = 'other'
        }
    }
}
```

# Usage

## Sending your coverage reports to Coveralls.io

Add this command to your build process (after testing and coverage report generation).

```groovy
// For Travis CI and GitHub public repo or if all the settings are defined in your Config.groovy
grails coveralls
// Or
grails coveralls --token=$COVERALLS_REPO_TOKEN --report=target/test-reports/cobertura/coverage.xml
```

Example of Travis CI config file for a **public** repo hosted on GitHub:

```yml
// .travis.yml
language: groovy
jdk: oraclejdk7
script:
  - ./grailsw refresh-dependencies
  - ./grailsw test-app -coverage -xml
after_success:
  - ./grailsw coveralls
```

Example of Travis CI config file for a **private** repo hosted on GitHub:

```yml
// .travis.yml
language: groovy
jdk: oraclejdk7
env:
  - COVERALLS_REPO_TOKEN=your_coveralls_repo_token // Should be encrypted
script:
  - ./grailsw refresh-dependencies
  - ./grailsw test-app -coverage -xml
after_success:
  - ./grailsw coveralls --token=$COVERALLS_REPO_TOKEN
```

# Latest releases

* 2014-05-21 **V0.1.1** : Plugin descriptor + README update
* 2014-05-21 **V0.1** : Initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-coveralls/issues) section on GitHub.

# Credits

This is a port to Grails of [Coveralls Gradle Plugin](https://github.com/kt3k/coveralls-gradle-plugin) by *Yoshiya Hinosawa*.
