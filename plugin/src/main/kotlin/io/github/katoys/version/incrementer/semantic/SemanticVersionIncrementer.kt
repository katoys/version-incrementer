package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.VersionRepository
import io.github.katoys.version.incrementer.VersionYaml

class SemanticVersionIncrementer(
    private val yamlPath: String = VersionYaml.DEFAULT_PATH,
    private val versionRepository: VersionRepository = YamlSemanticVersionRepository(yamlPath),
) {

    fun init(value: String) = SemanticVersion.from(value)
        .also { versionRepository.save(it) }

    fun current() = versionRepository.find() as? SemanticVersion
        ?: throw IllegalArgumentException("versionRepository is null")

    fun upMajor(suffix: String? = null) = change(suffix) { it.upMajor() }

    fun upMinor(suffix: String? = null) = change(suffix) { it.upMinor() }

    fun upPatch(suffix: String? = null) = change(suffix) { it.upPatch() }

    fun suffix(suffix: String? = null) = change(suffix) { it }

    private fun change(
        suffix: String?,
        incrementVersion: (SemanticVersion) -> SemanticVersion
    ): SemanticVersion {
        val currentVersion = versionRepository.find() as SemanticVersion
        val newVersion = incrementVersion(currentVersion)
            .let {
                if (suffix?.isNotEmpty() == true) {
                    it.appendSuffix(suffix)
                } else {
                    it.removeSuffix()
                }
            }
        versionRepository.save(newVersion)
        return newVersion
    }
}
