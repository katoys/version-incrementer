package io.github.katoys.version.incrementer

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class VersionIncrementerPluginTest {

    @Test
    fun `plugin registers task`() {
        // given
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("io.github.katoys.version-incrementer")

        // when & then
        assertNotNull(project.tasks.findByName("versioning"))
    }
}
