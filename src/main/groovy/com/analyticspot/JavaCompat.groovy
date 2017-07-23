package com.analyticspot

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Paths

/**
 * Plugin for Java libraries that are JDK 7 compatible. See the README for details.
 */
class JavaCompat extends JavaLibrary implements Plugin<Project> {
    void apply(Project project) {
        super.apply(project)

        project.sourceCompatibility = 1.7
        project.targetCompatibility = 1.7


        project.compileJava {
            options.bootClasspath = getJdk7JarPath()
        }

        project.compileTestJava {
            options.bootClasspath = getJdk7JarPath()
        }
    }

    /**
     * See the README.
     */
    String getJdk7Path() {
        def jdk7Path = System.getenv('JDK7_HOME')
        if (jdk7Path == null) {
            throw new GradleException('Missing JDK7_HOME environment variable')
        }
    }

    /**
     * Get the full path the the jdk7 standard library jar.
     */
    String getJdk7JarPath() {
        def fullPath = Paths.get(getJdk7Path(), 'jre/lib/rt.jar')
        if (!fullPath.toFile().exists()) {
            throw new GradleException("$fullPath does not exist.")
        }
        return fullPath.toString()
    }
}
