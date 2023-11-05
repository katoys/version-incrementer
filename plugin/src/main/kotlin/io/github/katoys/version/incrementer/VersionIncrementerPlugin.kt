package io.github.katoys.version.incrementer

import io.github.katoys.version.incrementer.semantic.SemanticVersionIncrementer
import io.github.katoys.version.incrementer.semantic.YamlSemanticVersionRepository
import org.gradle.api.Project
import org.gradle.api.Plugin

class VersionIncrementerPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val yamlPath = (project.properties["yamlPath"] as String? ?: "version.yml")
            .also { project.logger.info("version yaml path: $it") }

        project.tasks.register("semantic") {
            it.doLast {

                // get properties
                val action = project.properties["action"] as String?
                val suffix = project.properties["suffix"] as String?
                val value = project.properties["value"] as String?

                // init version incrementer
                val versionIncrementer = SemanticVersionIncrementer(
                    versionRepository = YamlSemanticVersionRepository(yamlPath)
                )

                // execute
                val version = when (action?.lowercase()) {
                    "init" -> versionIncrementer.init(value = value ?: throw IllegalArgumentException("value is null"))
                    "current" -> versionIncrementer.current()
                    "up-major" -> versionIncrementer.upMajor(suffix)
                    "up-minor" -> versionIncrementer.upMinor(suffix)
                    "up-patch" -> versionIncrementer.upPatch(suffix)
                    "append-suffix" -> versionIncrementer.suffix(suffix)
                    "remove-suffix" -> versionIncrementer.suffix()
                    else -> throw IllegalArgumentException("'$action' is unknown action")
                }

                // print version json
                println("version: ${version.value}")
            }
        }
    }
}
