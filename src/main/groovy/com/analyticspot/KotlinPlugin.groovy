package com.analyticspot

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin for projects that contain Kotlin code. Safe to use with either of the ANX Java plugins.
 */
class KotlinPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.apply plugin: "kotlin"

        // We shared some build scans with the Gradle team and they said, "Based on the tasks I see executing in some
        // projects, he needs to either disable cacheability for tasks in any project that does both Kotlin and Java
        // compilation in separate tasks or he needs to move all of his Java sources into the kotlin source directory.
        // His build will be flaky otherwise and break in weird ways." so this disables caching in any project that
        // uses Kotlin. Once Kotlin has task cache enabled (should be soon) we can remove this. There is not a public
        // bug for Kotlin caching so there's not an easy way to track when this is done - we'll just have to check
        // from time to time.
        project.afterEvaluate {
            project.tasks.all { task ->
                task.outputs.doNotCacheIf("Kotlin and caching don't mix") { true }
            }
        }


        project.dependencies {
            compile 'org.jetbrains.kotlin:kotlin-stdlib:1.1.3'
        }

        KotlinLint.setupKtLint(project)
    }
}
