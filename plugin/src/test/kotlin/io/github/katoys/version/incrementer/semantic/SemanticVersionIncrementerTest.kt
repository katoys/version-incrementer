package io.github.katoys.version.incrementer.semantic

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SemanticVersionIncrementerTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-rc,true,2.0.0-rc",
            "1.2.3-rc,false,2.0.0",
            "1.2.3,true,2.0.0",
            "1.2.3,false,2.0.0",
        ]
    )
    fun `upMajor() is correct`(
        current: String,
        isKeepSuffix: Boolean,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementerTasks(
            yamlPath = "dummy-version.yml",
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upMajor(isKeepSuffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-rc,true,1.3.0-rc",
            "1.2.3-rc,false,1.3.0",
            "1.2.3,true,1.3.0",
            "1.2.3,false,1.3.0",
        ]
    )
    fun `upMinor() is correct`(
        current: String,
        isKeepSuffix: Boolean,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementerTasks(
            yamlPath = "dummy-version.yml",
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upMinor(isKeepSuffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-rc,true,1.2.4-rc",
            "1.2.3-rc,false,1.2.4",
            "1.2.3,true,1.2.4",
            "1.2.3,false,1.2.4",
        ]
    )
    fun `upPatch() is correct`(
        current: String,
        isKeepSuffix: Boolean,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementerTasks(
            yamlPath = "dummy-version.yml",
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upPatch(isKeepSuffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-rc,SNAPSHOT,1.2.3-SNAPSHOT",
        ]
    )
    fun `appendSuffix() is correct`(
        current: String,
        suffix: String,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementerTasks(
            yamlPath = "dummy-version.yml",
            versionRepository = versionRepository
        )
        // when
        val actual = sut.appendSuffix(suffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-rc,1.2.3",
        ]
    )
    fun `removeSuffix() is correct`(
        current: String,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementerTasks(
            yamlPath = "dummy-version.yml",
            versionRepository = versionRepository
        )
        // when
        val actual = sut.removeSuffix()
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }
}