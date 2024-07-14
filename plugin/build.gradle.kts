plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "io.github.katoys"
version = "1.2.0"
val artifactName = "version-incrementer"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testImplementation("io.mockk:mockk:1.13.10")
}

/**
 * functional test
 */

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

/**
 * plugin settings
 */

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("com.gradle.publish:plugin-publish-plugin:1.2.1")
    }
}

apply(plugin = "com.gradle.plugin-publish")

gradlePlugin {
    website.set("https://github.com/katoys/version-incrementer")
    vcsUrl.set("https://github.com/katoys/version-incrementer")

    val versioning by plugins.creating {
        id = "${project.group}.${artifactName}"
        displayName = "Version Incrementer"
        description = "simple gradle plugin that increments the version number"
        tags.set(listOf("versioning", "semantic-versioning", "kotlin"))
        implementationClass = "io.github.katoys.version.incrementer.VersionIncrementerPlugin"
    }
}
