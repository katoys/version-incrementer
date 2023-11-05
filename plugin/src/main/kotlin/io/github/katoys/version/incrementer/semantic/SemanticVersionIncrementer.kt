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

    fun upMajor(modifier: String? = null) = change(modifier) { it.upMajor() }

    fun upMinor(modifier: String? = null) = change(modifier) { it.upMinor() }

    fun upPatch(modifier: String? = null) = change(modifier) { it.upPatch() }

    fun modifier(modifier: String? = null) = change(modifier) { it }

    private fun change(
        modifier: String?,
        incrementVersion: (SemanticVersion) -> SemanticVersion
    ): SemanticVersion {
        val currentVersion = versionRepository.find() as SemanticVersion
        val newVersion = incrementVersion(currentVersion)
            .let {
                if (modifier?.isNotEmpty() == true) {
                    it.appendModifier(modifier)
                } else {
                    it.removeModifier()
                }
            }
        versionRepository.save(newVersion)
        return newVersion
    }
}
