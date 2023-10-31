package io.github.katoys.version.incrementer

import io.github.katoys.version.incrementer.semantic.SemanticVersionIncrementerTasks
import org.gradle.api.Project
import org.gradle.api.Plugin

class VersionIncrementerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // Register a task
        project.tasks.register("greeting") { task ->
            task.doLast {
                println("Hello from plugin 'io.github.katoys.version-incrementer'")
            }
        }

        project.tasks.register("upMajor", SemanticVersionIncrementerTasks.UpMajor::class.java) {
            it.config = SemanticVersionIncrementerTasks.Config(
                yamlPath = project.properties["yamlPath"] as String? ?: "",
                keepSuffix = project.hasProperty("keepSuffix") && (project.properties["keepSuffix"] as String).toBoolean()
            )
        }
    }
}
