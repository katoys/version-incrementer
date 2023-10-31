package io.github.katoys.version.incrementer

import java.io.File
import kotlin.test.assertTrue
import kotlin.test.Test
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir

@Suppress("FunctionName")
class VersionIncrementerPluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    @BeforeEach
    fun beforeEach() {
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id('io.github.katoys.version-incrementer')
            }
        """.trimIndent()
        )
    }

    @Test
    fun `can run task`() {
        // when: Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("semantic", "-Ptarget=current", "-PyamlPath=src/test/resources/semantic/testing-write-version.yml")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        // then: Verify the result
        assertTrue(result.output.contains("upMajor"))
    }
}
