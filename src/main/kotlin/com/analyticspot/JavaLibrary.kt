package com.analyticspot

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by oliver on 7/3/17.
 */
class JavaLibrary : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            task("testing").doLast {
                print("It is working!!")
            }
        }
    }
}