package io.github.katoys.version.incrementer

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File

interface VersionYaml {
    val yamlPath: String

    fun readValue(type: Version.Type): String {
        val yaml = File(yamlPath).readText().let { Yaml().load(it) as Map<String, Any> }
        if (yaml["type"] != type.name) {
            throw IllegalArgumentException("type is not $type")
        }
        return yaml["value"] as String
    }

    fun write(version: Version) {
        val dumperOptions = DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        }
        val yaml = Yaml(dumperOptions).dump(
            mapOf(
                "type" to version.type.name,
                "value" to version.value,
            )
        )
        File(yamlPath).writeText(yaml)
    }
}
