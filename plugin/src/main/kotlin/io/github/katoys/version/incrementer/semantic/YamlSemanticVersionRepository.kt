package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.Version
import io.github.katoys.version.incrementer.VersionRepository
import io.github.katoys.version.incrementer.VersionYaml

class YamlSemanticVersionRepository(
    override val yamlPath: String
) : VersionRepository, VersionYaml {

    override fun find(): Version = SemanticVersion.from(
        readValue(Version.Type.Semantic)
    )

    override fun save(version: Version) {
        if (version !is SemanticVersion) {
            throw IllegalArgumentException("version must be SemanticVersion")
        }
        write(version)
    }
}
