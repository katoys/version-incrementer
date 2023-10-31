package io.github.katoys.version.incrementer

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class VersionIncrementerPluginTest {

    @Test
    fun `plugin registers task`() {
        // given
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("io.github.katoys.version-incrementer")

        // when & then
        assertNotNull(project.tasks.findByName("upMajor"))
    }
}
