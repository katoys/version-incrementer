package io.github.katoys.version.incrementer

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@Suppress("ClassName")
class VersionIncrementerPluginTest {

    @Test
    fun `plugin registers task`() {
        // given
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("io.github.katoys.version-incrementer")

        // when & then
        assertNotNull(project.tasks.findByName("versioning"))
        assertNotNull(project.tasks.findByName("printCurrentVersion"))
    }

    @Nested
    inner class `Property#type` {
        @Test
        fun `default value`() {
            val project = ProjectBuilder.builder().build()
            val sut = VersionIncrementerPlugin.Property(project)

            assertEquals(Version.Type.Semantic, sut.type)
        }

        @Test
        fun `set`() {
            val project = ProjectBuilder.builder().build().also {
                it.extensions.extraProperties.set("type", "semantic")
            }
            val sut = VersionIncrementerPlugin.Property(project)

            assertEquals(Version.Type.Semantic, sut.type)
        }
    }

    @Nested
    inner class `Property#yamlPath` {
        @Test
        fun `default value`() {
            val project = ProjectBuilder.builder().build()
            val sut = VersionIncrementerPlugin.Property(project)
            assertEquals("version.yml", sut.yamlPath)
        }

        @Test
        fun `set`() {
            val project = ProjectBuilder.builder().build().also {
                it.extensions.extraProperties.set("yamlPath", "path/to/version-test.yml")
            }
            val sut = VersionIncrementerPlugin.Property(project)
            assertEquals("path/to/version-test.yml", sut.yamlPath)
        }
    }

    @Nested
    inner class `Property#action` {
        @Test
        fun `default value`() {
            val project = ProjectBuilder.builder().build()
            val sut = VersionIncrementerPlugin.Property(project)
            assertEquals("version.yml", sut.yamlPath)
        }

        @Test
        fun `set`() {
            val project = ProjectBuilder.builder().build().also {
                it.extensions.extraProperties.set("action", "someAction")
            }
            val sut = VersionIncrementerPlugin.Property(project)
            assertEquals("someAction", sut.action)
        }
    }

    @Nested
    inner class `Property#modifier` {
        @Test
        fun `default value`() {
            val project = ProjectBuilder.builder().build()
            val sut = VersionIncrementerPlugin.Property(project)
            assertNull(sut.modifier)
        }

        @Test
        fun `set`() {
            val project = ProjectBuilder.builder().build().also {
                it.extensions.extraProperties.set("modifier", "someModifier")
            }
            val sut = VersionIncrementerPlugin.Property(project)
            assertEquals("someModifier", sut.modifier)
        }
    }

    @Nested
    inner class `Property#value` {
        @Test
        fun `default value`() {
            val project = ProjectBuilder.builder().build()
            val sut = VersionIncrementerPlugin.Property(project)
            assertNull(sut.value)
        }

        @Test
        fun `set`() {
            val project = ProjectBuilder.builder().build().also {
                it.extensions.extraProperties.set("value", "someValue")
            }
            val sut = VersionIncrementerPlugin.Property(project)
            assertEquals("someValue", sut.value)
        }
    }
}
