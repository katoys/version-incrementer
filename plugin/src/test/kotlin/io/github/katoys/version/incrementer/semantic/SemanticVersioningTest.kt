package io.github.katoys.version.incrementer.semantic

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SemanticVersioningTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "1.2.3-RC",
            "1.2.3",
            "999.999.999",
            "999.999.999-SNAPSHOT",
        ]
    )
    fun `init() is correct`(
        value: String
    ) {
        // given
        val expected = SemanticVersion.from(value)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { save(expected) } returns Unit
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.init(value)
        // then
        assertEquals(expected, actual)
        verify(exactly = 1) { versionRepository.save(expected) }
    }

    @Test
    fun `current() is correct`() {
        // given
        val expected = SemanticVersion.from("1.2.3-RC")
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns expected
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.current()
        // then
        assertEquals(expected, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 0) { versionRepository.save(any()) }
    }

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
        modifier: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upMajor(modifier)
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
        modifier: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upPatch(modifier)
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
        modifier: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.upMinor(modifier)
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
    fun `modifier() is correct`(
        current: String,
        modifier: String?,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current)
        val newVersion = SemanticVersion.from(expected)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.modifier(modifier)
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }

    @ParameterizedTest
    @CsvSource(value = [
        "1.2.3-alpha,1.2.3-alpha.1",
        "1.2.3-alpha.1,1.2.3-alpha.2",
        "1.2.3-alpha.2,1.2.3-alpha.3",
        "1.2.3,1.2.3",
    ])
    fun `nextModifierSeq() is correct`(
        current: String,
        expected: String
    ) {
        // given
        val currentVersion = SemanticVersion.from(current).also(::println)
        val newVersion = SemanticVersion.from(expected).also(::println)
        val versionRepository = mockk<YamlSemanticVersionRepository> {
            every { find() } returns currentVersion
            every { save(newVersion) } returns Unit
        }
        val sut = SemanticVersioning(
            versionRepository = versionRepository
        )
        // when
        val actual = sut.nextModifierSeq()
        // then
        assertEquals(newVersion, actual)
        verify(exactly = 1) { versionRepository.find() }
        verify(exactly = 1) { versionRepository.save(newVersion) }
    }
}