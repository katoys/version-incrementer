package io.github.katoys.version.incrementer

import org.gradle.api.Project
import org.gradle.api.Plugin

class VersionIncrementerPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // Register a task
        project.tasks.register("greeting") { task ->
            task.doLast {
                println("Hello from plugin 'io.github.katoys.version-incrementer'")
            }
        }
    }
}
