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
    fun `can run versioning`() {
        // when
        val result = runSemanticVersioning("current")
        // then
        assertTrue(result.output.contains("version: 0.0.0"))
    }

    @Test
    fun `can run versioning repeatable`() {
        // when & then
        runSemanticVersioning("init", value = "1.0.0").also {
            assertTrue(it.output.contains("version: 1.0.0"))
        }
        runSemanticVersioning("up-major").also {
            assertTrue(it.output.contains("version: 2.0.0"))
        }
        runSemanticVersioning("up-minor").also {
            assertTrue(it.output.contains("version: 2.1.0"))
        }
        runSemanticVersioning("up-patch").also {
            assertTrue(it.output.contains("version: 2.1.1"))
        }
        runSemanticVersioning("append-suffix", suffix = "RC").also {
            assertTrue(it.output.contains("version: 2.1.1-RC"))
        }
        runSemanticVersioning("remove-suffix").also {
            assertTrue(it.output.contains("version: 2.1.1"))
        }
        runSemanticVersioning("current").also {
            assertTrue(it.output.contains("version: 2.1.1"))
        }
    }

    private fun runSemanticVersioning(
        action: String,
        suffix: String? = null,
        value: String? = null
    ): BuildResult = GradleRunner.create().also {
        it.forwardOutput()
        it.withPluginClasspath()
        it.withProjectDir(projectDir)
        it.withArguments(
            "versioning",
            "-Ptype=semantic",
            "-Paction=$action",
            "-Psuffix=${suffix ?: ""}",
            "-Pvalue=${value ?: ""}",
            "-PyamlPath=${projectDir.resolve(VERSION_YAML)}"
        )
    }.build()
}
