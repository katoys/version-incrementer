package io.github.katoys.version.incrementer

import org.gradle.testkit.runner.BuildResult
import java.io.File
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

@Suppress("FunctionName")
class VersionIncrementerPluginFunctionalTest {

    companion object {
        const val VERSION_YAML = "version.yml"
    }

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    @BeforeEach
    fun beforeAll() {
        settingsFile.writeText("")
        buildFile.writeText(
            """
            plugins {
                id('io.github.katoys.version-incrementer')
            }
            """.trimIndent()
        )
        projectDir.resolve(VERSION_YAML).writeText(
            """
            type: Semantic
            value: 0.0.0
            """.trimIndent()
        )
    }

    @AfterEach
    fun afterEach() {
        projectDir.deleteRecursively()
    }

    @Test
    fun `can run printCurrentVersion`() {
        // when
        val result = runTask("printCurrentVersion")
        // then
        assertTrue(result.output.contains("version: 0.0.0"))
    }

    @Test
    fun `can run versioning`() {
        // when
        val result = runVersioning("init", value = "999.999.999-SNAPSHOT")
        // then
        assertTrue(result.output.contains("version: 999.999.999-SNAPSHOT"))
    }

    @Test
    fun `can run versioning repeatable`() {
        // when & then
        runVersioning("init", value = "1.0.0").also {
            assertTrue(it.output.contains("version: 1.0.0"))
        }
        runVersioning("up-major").also {
            assertTrue(it.output.contains("version: 2.0.0"))
        }
        runVersioning("up-minor").also {
            assertTrue(it.output.contains("version: 2.1.0"))
        }
        runVersioning("up-patch").also {
            assertTrue(it.output.contains("version: 2.1.1"))
        }
        runVersioning("append-modifier", modifier = "RC").also {
            assertTrue(it.output.contains("version: 2.1.1-RC"))
        }
        runVersioning("remove-modifier").also {
            assertTrue(it.output.contains("version: 2.1.1"))
        }
    }

    private fun runVersioning(
        action: String,
        modifier: String? = null,
        value: String? = null
    ) = runTask("versioning", action, modifier, value)

    private fun runTask(
        taskName: String,
        action: String? = null,
        modifier: String? = null,
        value: String? = null
    ): BuildResult = GradleRunner.create().also {
        it.forwardOutput()
        it.withPluginClasspath()
        it.withProjectDir(projectDir)
        it.withArguments(
            taskName,
            "-Ptype=semantic",
            "-Paction=$action",
            "-Pmodifier=${modifier ?: ""}",
            "-Pvalue=${value ?: ""}",
            "-PyamlPath=${projectDir.resolve(VERSION_YAML)}"
        )
    }.build()
}
