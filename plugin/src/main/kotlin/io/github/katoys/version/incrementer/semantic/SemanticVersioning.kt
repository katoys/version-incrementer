package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.VersionRepository
import io.github.katoys.version.incrementer.VersionYaml

class SemanticVersioning(
    private val yamlPath: String = VersionYaml.DEFAULT_PATH,
    private val versionRepository: VersionRepository = YamlSemanticVersionRepository(yamlPath),
) {

    fun init(value: String) = SemanticVersion.from(value)
        .also { versionRepository.save(it) }

    fun current() = versionRepository.find() as? SemanticVersion
        ?: throw IllegalArgumentException("versionRepository is null")

    fun upMajor(modifier: String? = null) = change(ModifierOperation.Update(modifier)) { it.upMajor() }

    fun upMinor(modifier: String? = null) = change(ModifierOperation.Update(modifier)) { it.upMinor() }

    fun upPatch(modifier: String? = null) = change(ModifierOperation.Update(modifier)) { it.upPatch() }

    fun modifier(modifier: String? = null) = change(ModifierOperation.Update(modifier)) { it }

    fun nextModifierSeq() = change(ModifierOperation.NextSeq) { it }

    private fun change(
        modifierOperation: ModifierOperation,
        incrementVersion: (SemanticVersion) -> SemanticVersion
    ): SemanticVersion {
        val currentVersion = versionRepository.find() as SemanticVersion
        val newVersion = incrementVersion(currentVersion)
            .let {
                when (modifierOperation) {
                    is ModifierOperation.NextSeq -> it.nextModifierSeq()
                    is ModifierOperation.Update -> if (modifierOperation.modifier.isNullOrEmpty()) {
                        it.removeModifier()
                    } else {
                        it.appendModifier(modifierOperation.modifier)
                    }
                }
            }
        versionRepository.save(newVersion)
        return newVersion
    }

    private sealed interface ModifierOperation {

        class Update(val modifier: String?) : ModifierOperation

        data object NextSeq : ModifierOperation
    }
}
