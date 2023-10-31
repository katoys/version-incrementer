package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.VersionRepository
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class SemanticVersionIncrementerTasks(
    private val yamlPath: String,
    private val versionRepository: VersionRepository = YamlSemanticVersionRepository(yamlPath)
) {
    companion object {
        const val DEFAULT_YAML_PATH = "version.yml"
    }

    class Config(
        val yamlPath: String = DEFAULT_YAML_PATH,
        val keepSuffix: Boolean = false
    )

    abstract class UpMajor : DefaultTask() {

        @get:Input
        var config = Config()

        @TaskAction
        fun task() {
            if (config.keepSuffix) {
                println("upMajor: keep suffix, ${config.yamlPath}")
            } else {
                println("upMajor: remove suffix, ${config.yamlPath}")
            }
        }
    }

    fun upMajor(isKeepSuffix: Boolean = false) = if (isKeepSuffix) {
        change { it.upMajor() }
    } else {
        change { it.upMajor().removeSuffix() }
    }

    fun upMinor(isKeepSuffix: Boolean = false) = if (isKeepSuffix) {
        change { it.upMinor() }
    } else {
        change { it.upMinor().removeSuffix() }
    }


    fun upPatch(isKeepSuffix: Boolean = false) = if (isKeepSuffix) {
        change { it.upPatch() }
    } else {
        change { it.upPatch().removeSuffix() }
    }

    fun appendSuffix(suffix: String) = change {
        it.appendSuffix(suffix)
    }

    fun removeSuffix() = change {
        it.removeSuffix()
    }

    private fun change(createNewVersion: (SemanticVersion) -> SemanticVersion): SemanticVersion {
        val currentVersion = versionRepository.find() as SemanticVersion
        val newVersion = createNewVersion(currentVersion)
        versionRepository.save(newVersion)
        return newVersion
    }
}
