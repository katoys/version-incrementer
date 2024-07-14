package io.github.katoys.version.incrementer

import io.github.katoys.version.incrementer.semantic.SemanticVersioning
import org.gradle.api.Project
import org.gradle.api.Plugin

class VersionIncrementerPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.tasks.register("versioning") { task ->
            task.doLast {
                val property = Property(project)

                val version = when (property.type) {
                    Version.Type.Semantic -> semanticVersioning(
                        yamlPath = property.yamlPath,
                        action = property.action,
                        modifier = property.modifier,
                        value = property.value
                    )
                }

                printVersion(version)
            }
        }

        project.tasks.register("printCurrentVersion") { task ->
            task.doLast {
                val property = Property(project)

                val version = when (property.type) {
                    Version.Type.Semantic -> SemanticVersioning(property.yamlPath).current()
                }

                printVersion(version)
            }
        }
    }

    private fun printVersion(version: Version) = println(version.value)

    private fun semanticVersioning(
        yamlPath: String,
        action: String?,
        modifier: String?,
        value: String?
    ): Version = SemanticVersioning(yamlPath).let {
        when (action?.lowercase()) {
            "init" -> it.init(value = value ?: throw IllegalArgumentException("value is null"))
            "up-major" -> it.upMajor(modifier)
            "up-minor" -> it.upMinor(modifier)
            "up-patch" -> it.upPatch(modifier)
            "append-modifier" -> it.modifier(modifier)
            "next-modifier-seq" -> it.nextModifierSeq()
            "remove-modifier" -> it.modifier()
            else -> throw IllegalArgumentException("'$action' is unknown action")
        }
    }

    class Property(
        private val project: Project
    ) {
        val type: Version.Type
            get() = Version.Type.from(project.properties["type"] as String?)
        val yamlPath: String
            get() = project.properties["yamlPath"] as String? ?: VersionYaml.DEFAULT_PATH
        val action: String?
            get() = project.properties["action"] as String?
        val modifier: String?
            get() = project.properties["modifier"] as String?
        val value: String?
            get() = project.properties["value"] as String?
    }
}
