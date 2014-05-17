
Coveralls.io Grails Plugin
=======================

# Introduction

The Coveralls.io Plugin provides a Gant script to automatically send [Grails](http://grails.org/plugin/code-coverage) code coverage report to [Coveralls.io](http://coveralls.io).
This script can easily be integrated to a build pipeline for continuous delivery/deployment: it works pretty well with [Travis CI](http://travis-ci.org) and [Travis Pro](http://travis-ci.com).

It adds one [Grails](http://grails.org) Gant scripts:

- *coveralls* to send coverage report to [Coveralls.io](http://coveralls.io).

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
                //your dependencies
        }
		plugins {
				//here go your plugin dependencies
				build ':coveralls:0.1'
				test(':code-coverage:1.2.7') {
                    export = false
                }
		}
}
```


# Config

You can add your config in **Config.groovy** but it is not required, especially when running Travis CI.
All parameters can be passed as arguments to *coveralls* Gant script.

```groovy
// Single provider
grails {
    plugin {
        coveralls {
            // Cobertura XML coverage report path
            report = 'path/to/cobertura.xml' // if not defined, default to 'target/test-reports/cobertura/coverage.xml'
            // Coveralls repo token, not required for Travis CI public repo (required for Travis Pro or other CI).
            token = '...'
            // CI Service name (default to 'travis-ci' and 'travis-pro' for Travis)
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
grails coveralls --token=$REPO_TOKEN --report=target/test-reports/cobertura/coverage.xml
```

Example of Travis CI config file for a public repo hosted on GitHub:

```yml
language: groovy
jdk:
- oraclejdk7
script:
- ./grailsw refresh-dependencies
- ./grailsw "test-app unit: -coverage -xml"
after_success:
- ./grailsw coveralls
```

# Latest releases

* 2014-05-07 **V0.1** : Initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-coveralls/issues) section on GitHub.

# Credits

This is a port to Grails of [Coveralls Gradle Plugin](https://github.com/kt3k/coveralls-gradle-plugin) by *Yoshiya Hinosawa*.
