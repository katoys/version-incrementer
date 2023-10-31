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
            "1.2.3-RC,RC,2.0.0-RC",
            "1.2.3-RC,,2.0.0",
            "1.2.3-RC,'',2.0.0",
            "1.2.3,RC,2.0.0-RC",
            "1.2.3,,2.0.0",
            "1.2.3,'',2.0.0",
        ]
    )
    fun `upMajor() is correct`(
        current: String,
        suffix: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementer(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upMajor(suffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-RC,RC,1.2.4-RC",
            "1.2.3-RC,,1.2.4",
            "1.2.3-RC,'',1.2.4",
            "1.2.3,RC,1.2.4-RC",
            "1.2.3,,1.2.4",
            "1.2.3,'',1.2.4",
        ]
    )
    fun `upPatch() is correct`(
        current: String,
        suffix: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementer(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upPatch(suffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-RC,RC,1.3.0-RC",
            "1.2.3-RC,,1.3.0",
            "1.2.3-RC,'',1.3.0",
            "1.2.3,RC,1.3.0-RC",
            "1.2.3,,1.3.0",
            "1.2.3,'',1.3.0",
        ]
    )
    fun `upMinor() is correct`(
        current: String,
        suffix: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementer(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upMinor(suffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-RC,SNAPSHOT,1.2.3-SNAPSHOT",
            "1.2.3-RC,RC,1.2.3-RC",
            "1.2.3-RC,,1.2.3",
            "1.2.3-RC,'',1.2.3",
            "1.2.3,,1.2.3",
            "1.2.3,'',1.2.3",
        ]
    )
    fun `suffix() is correct`(
        current: String,
        suffix: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersionIncrementer(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.suffix(suffix)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }
}