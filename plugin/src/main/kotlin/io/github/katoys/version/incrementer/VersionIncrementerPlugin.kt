package io.github.katoys.version.incrementer

import io.github.katoys.version.incrementer.semantic.SemanticVersionIncrementer
import org.gradle.api.Project
import org.gradle.api.Plugin

class VersionIncrementerPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.tasks.register("versioning") { task ->
            task.doLast {
                // get properties
                val type = (project.properties["type"] as String?)?.let { Type.from(it) } ?: Type.DEFAULT
                val action = project.properties["action"] as String?
                val modifier = project.properties["modifier"] as String?
                val value = project.properties["value"] as String?
                val yamlPath = project.properties["yamlPath"] as String?

                // versioning
                val version = when (type) {
                    Type.Semantic -> {
                        semanticVersioning(
                            yamlPath = yamlPath ?: VersionYaml.DEFAULT_PATH,
                            action = action,
                            modifier = modifier,
                            value = value
                        )
                    }
                }

                // print version json
                println("version: ${version.value}")
            }
        }
    }

    enum class Type {
        Semantic;

        companion object {

            val DEFAULT = Semantic

            fun from(value: String): Type {
                return when (value.lowercase()) {
                    "semantic" -> Semantic
                    else -> throw IllegalArgumentException("'$value' is unknown type")
                }
            }
        }
    }

    private fun semanticVersioning(
        yamlPath: String,
        action: String?,
        modifier: String?,
        value: String?
    ): Version = SemanticVersionIncrementer(yamlPath).let {
        when (action?.lowercase()) {
            "init" -> it.init(value = value ?: throw IllegalArgumentException("value is null"))
            "current" -> it.current()
            "up-major" -> it.upMajor(modifier)
            "up-minor" -> it.upMinor(modifier)
            "up-patch" -> it.upPatch(modifier)
            "append-modifier" -> it.modifier(modifier)
            "remove-modifier" -> it.modifier()
            else -> throw IllegalArgumentException("'$action' is unknown action")
        }
    }
}
