package io.github.katoys.version.incrementer

import org.gradle.internal.impldep.org.yaml.snakeyaml.DumperOptions
import org.gradle.internal.impldep.org.yaml.snakeyaml.Yaml
import org.slf4j.LoggerFactory
import java.io.File

/**
 * [Version] repository.
 */
interface VersionRepository {

    fun find(): Version

    fun save(version: Version)
}

/**
 * [VersionRepository] implementation with YAML for semantic-version.
 */
class YamlSemanticVersionRepository(
    private val path: String
) : VersionRepository {

    private val logger = LoggerFactory.getLogger(YamlSemanticVersionRepository::class.java)

    override fun find(): Version {
        val yaml = File(path).readText().let { Yaml().load(it) as Map<String, Any> }
        val version = (yaml["element"] as Map<*, *>).let {
            SemanticVersion.from(
                major = it["major"] as Int,
                minor = it["minor"] as Int,
                patch = it["patch"] as Int,
                suffix = it["suffix"] as String?
            )
        }
        if (version.value != yaml["value"]) {
            logger.warn("Version data is invalid. value is `${yaml["value"]}`, but element is `${version.element}`.")
        }
        return version
    }

    override fun save(version: Version) {
        if (version !is SemanticVersion) {
            throw IllegalArgumentException("version must be SemanticVersion")
        }

        val versionYaml = mapOf(
            "value" to version.value,
            "element" to mapOf(
                "major" to version.element.major,
                "minor" to version.element.minor,
                "patch" to version.element.patch,
                "suffix" to version.element.suffix
            )
        )
        val dumperOptions = DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        }
        val yaml = Yaml(dumperOptions).dump(versionYaml)
        File(path).writeText(yaml)
    }
}
