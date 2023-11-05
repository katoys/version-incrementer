package io.github.katoys.version.incrementer.semantic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SemanticVersionTest {

    @Nested
    inner class From {

        @ParameterizedTest
        @CsvSource(
            value = [
                "0.0.0,0,0,0,",
                "1.2.3,1,2,3,",
                "999.999.999,999,999,999,",
                "1.2.3-SNAPSHOT,1,2,3,SNAPSHOT"
            ]
        )
        fun `from string`(value: String, major: Int, minor: Int, patch: Int, modifier: String?) {
            // when
            val actual = SemanticVersion.from(value)
            // then
            assertEquals(value, actual.value)
            assertEquals(major, actual.element.major)
            assertEquals(minor, actual.element.minor)
            assertEquals(patch, actual.element.patch)
            assertEquals(modifier, actual.element.modifier)
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "0.0.0,0,0,0,",
                "1.2.3,1,2,3,",
                "999.999.999,999,999,999,",
                "1.2.3-SNAPSHOT,1,2,3,SNAPSHOT"
            ]
        )
        fun `from element`(value: String, major: Int, minor: Int, patch: Int, modifier: String?) {
            // when
            val actual = SemanticVersion.from(
                major = major,
                minor = minor,
                patch = patch,
                modifier = modifier
            )
            // then
            assertEquals(value, actual.value)
            assertEquals(major, actual.element.major)
            assertEquals(minor, actual.element.minor)
            assertEquals(patch, actual.element.patch)
            assertEquals(modifier, actual.element.modifier)
        }
    }

    @Nested
    inner class Up {

        @ParameterizedTest
        @CsvSource(
            value = [
                "0.0.0,1.0.0",
                "1.2.3,2.0.0",
                "999.999.999,1000.0.0",
                "1.2.3-SNAPSHOT,2.0.0-SNAPSHOT"
            ]
        )
        fun `up major`(value: String, expected: String) {
            // when
            val actual = SemanticVersion.from(value).upMajor()
            // then
            assertEquals(expected, actual.value)
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "0.0.0,0.1.0",
                "1.2.3,1.3.0",
                "999.999.999,999.1000.0",
                "1.2.3-SNAPSHOT,1.3.0-SNAPSHOT"
            ]
        )
        fun `up minor`(value: String, expected: String) {
            // when
            val actual = SemanticVersion.from(value).upMinor()
            // then
            assertEquals(expected, actual.value)
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "0.0.0,0.0.1",
                "1.2.3,1.2.4",
                "999.999.999,999.999.1000",
                "1.2.3-SNAPSHOT,1.2.4-SNAPSHOT"
            ]
        )
        fun `up patch`(value: String, expected: String) {
            // when
            val actual = SemanticVersion.from(value).upPatch()
            // then
            assertEquals(expected, actual.value)
        }
    }

    @Nested
    inner class Modifier {

        @ParameterizedTest
        @CsvSource(
            value = [
                "1.2.3,SNAPSHOT,1.2.3-SNAPSHOT",
                "1.2.3-SNAPSHOT,SNAPSHOT,1.2.3-SNAPSHOT",
                "1.2.3-SNAPSHOT,RELEASE,1.2.3-RELEASE",
                "1.2.3-SNAPSHOT,,1.2.3",
                "1.2.3-SNAPSHOT,'',1.2.3",
            ]
        )
        fun `append modifier`(value: String, modifier: String?, expected: String) {
            // when
            val actual = SemanticVersion.from(value).appendModifier(modifier)
            // then
            assertEquals(expected, actual.value)
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "1.2.3-SNAPSHOT,1.2.3",
                "1.2.3-RELEASE,1.2.3",
                "1.2.3,1.2.3",
            ]
        )
        fun `remove modifier`(value: String, expected: String) {
            // when
            val actual = SemanticVersion.from(value).removeModifier()
            // then
            assertEquals(expected, actual.value)
        }
    }
}