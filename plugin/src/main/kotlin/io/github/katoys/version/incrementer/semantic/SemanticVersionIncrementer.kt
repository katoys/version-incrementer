package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.VersionRepository

class SemanticVersionIncrementer(
    private val yamlPath: String,
    private val versionRepository: VersionRepository = YamlSemanticVersionRepository(yamlPath)
) {

    fun upMajor(isKeepSuffix: Boolean = false) = if (isKeepSuffix) {
        increment { it.upMajor() }
    } else {
        increment { it.upMajor().removeSuffix() }
    }

    fun upMinor(isKeepSuffix: Boolean = false) = if (isKeepSuffix) {
        increment { it.upMinor() }
    } else {
        increment { it.upMinor().removeSuffix() }
    }


    fun upPatch(isKeepSuffix: Boolean = false) = if (isKeepSuffix) {
        increment { it.upPatch() }
    } else {
        increment { it.upPatch().removeSuffix() }
    }

    fun appendSuffix(suffix: String) = increment {
        it.appendSuffix(suffix)
    }

    fun removeSuffix() = increment {
        it.removeSuffix()
    }

    private fun increment(createNewVersion: (SemanticVersion) -> SemanticVersion): SemanticVersion {
        val currentVersion = versionRepository.find() as SemanticVersion
        val newVersion = createNewVersion(currentVersion)
        versionRepository.save(newVersion)
        return newVersion
    }
}
