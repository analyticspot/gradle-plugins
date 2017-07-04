package com.analyticspot

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

/**
 * The standard Analytics Spot java library plugin. See the README.md for details.
 */
class JavaLibrary implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.with {
            plugins.apply('java')
            plugins.apply('idea')

            repositories {
                mavenLocal()
                jcenter()
            }

            // Java config
            sourceCompatibility = 1.8

            project.compileJava {
                options.compilerArgs << "-Werror"
                options.compilerArgs << "-Xlint:all"
            }
            project.compileTestJava {
                options.compilerArgs << "-Werror"
                options.compilerArgs << "-Xlint:all"
            }

            // Sets up the "provided" configuration. See the README for details.
            configurations {
                provided
            }
            sourceSets.main {
                compileClasspath += configurations.provided
            }

            // This tells IntelliJ that the stuff in provided is available.
            project.idea.module {
                scopes.PROVIDED.plus += [ configurations.provided ]
            }

            // Set up testing. In addition to testNG we show exceptions, logging output, etc.
            test {
                useTestNG()
                enableAssertions = true
                testLogging {
                    showExceptions = true
                    showStandardStreams = true
                    exceptionFormat = 'full'
                }
            }

            def testSuccess = 0
            def failedTests = []
            // Map from suite name to list of failing tests
            // def testFailures = new ArrayList<String  List<String>>()
            project.test.afterTest { TestDescriptor tDesc, TestResult tResult ->
                assert tResult.testCount == 1
                if (tResult.successfulTestCount > 0) {
                    ++testSuccess
                } else {
                    failedTests << "${tDesc.className}.${tDesc.name}"
                }
            }

            project.getGradle().buildFinished {
                def totalTests = testSuccess + failedTests.size()
                if (totalTests > 0) {
                    if (failedTests.size() > 0) {
                        logger.error(
                                "Ran $totalTests tests for project ${path}. ${failedTests.size()} failed:")
                        for (ft in failedTests) {
                            logger.error(": $ft")
                        }
                    } else {
                        logger.info("Successfully ran $totalTests tests for project ${path}")
                    }
                }
            }
        }
    }
}
