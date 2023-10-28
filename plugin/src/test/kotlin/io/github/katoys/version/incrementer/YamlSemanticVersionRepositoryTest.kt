package io.github.katoys.version.incrementer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.nio.file.Files
import java.nio.file.Paths

class YamlSemanticVersionRepositoryTest {

    @Nested
    inner class Find {

        @ParameterizedTest
        @CsvSource(
            value = [
                "src/test/resources/version-0.0.0.yml, 0, 0, 0,",
                "src/test/resources/version-0.0.1.yml, 0, 0, 1,",
                "src/test/resources/version-0.1.0.yml, 0, 1, 0,",
                "src/test/resources/version-1.0.0.yml, 1, 0, 0,",
                "src/test/resources/version-9.9.9-SNAPSHOT.yml, 9, 9, 9, SNAPSHOT",
            ]
        )
        fun `can read version yaml`(
            yamlPath: String,
            expectedMajor: Int,
            expectedMinor: Int,
            expectedPatch: Int,
            expectedSuffix: String?
        ) {
            // given
            val repository = YamlSemanticVersionRepository(yamlPath)
            // when
            val version = repository.find()
            // then
            if (version is SemanticVersion) {
                assertEquals(expectedMajor, version.element.major)
                assertEquals(expectedMinor, version.element.minor)
                assertEquals(expectedPatch, version.element.patch)
                assertEquals(expectedSuffix, version.element.suffix)
            } else {
                fail("version is not SemanticVersion")
            }
        }

    }

    @Nested
    inner class Save {

        private val yamlPath = "src/test/resources/testing-write-version.yml"

        @BeforeEach
        fun beforeEach() {
            Files.deleteIfExists(Paths.get(yamlPath))
        }

        @Test
        fun `can write version yaml with repeat`() {
            // given
            val repository = YamlSemanticVersionRepository(yamlPath)

            listOf(
                SemanticVersion.from(0, 0, 1, "SNAPSHOT"),
                SemanticVersion.from(1, 0, 0),
                SemanticVersion.from(1, 1, 0, "SNAPSHOT"),
                SemanticVersion.from(1, 1, 0),
                SemanticVersion.from(2, 0, 0, "alpha"),
                SemanticVersion.from(2, 0, 0, "beta"),
                SemanticVersion.from(2, 0, 0, "RC"),
                SemanticVersion.from(2, 0, 0),
            ).forEach {
                // when
                repository.save(it)
                // then
                assertEquals(it, repository.find())
            }
        }
    }
}