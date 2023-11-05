package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.VersionRepository

class SemanticVersionIncrementer(
    private val versionRepository: VersionRepository?,
) {

    fun init(value: String) = SemanticVersion.from(value)
        .also { versionRepository?.save(it) ?: throw IllegalArgumentException("versionRepository is null") }

    fun current() = versionRepository?.find() as? SemanticVersion
        ?: throw IllegalArgumentException("versionRepository is null")

    fun upMajor(suffix: String? = null) = change(suffix) { it.upMajor() }

    fun upMinor(suffix: String? = null) = change(suffix) { it.upMinor() }

    fun upPatch(suffix: String? = null) = change(suffix) { it.upPatch() }

    fun suffix(suffix: String? = null) = change(suffix) { it }

    private fun change(
        suffix: String?,
        incrementVersion: (SemanticVersion) -> SemanticVersion
    ): SemanticVersion {
        if (versionRepository == null) {
            throw IllegalArgumentException("versionRepository is null")
        }
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
