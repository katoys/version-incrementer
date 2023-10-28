package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.VersionRepository

class SemanticVersionIncrementer(
    private val yamlPath: String,
    private val versionRepository: VersionRepository = YamlSemanticVersionRepository(yamlPath)
) {

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
