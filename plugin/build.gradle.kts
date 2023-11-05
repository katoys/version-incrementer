plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "io.github.katoys"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("io.mockk:mockk:1.13.8")
}

gradlePlugin {
    website = "https://github.com/katoys/version-incrementer"
    vcsUrl = "https://github.com/katoys/version-incrementer"

    val versioning by plugins.creating {
        id = "io.github.katoys.version-incrementer"
        displayName = "Version Incrementer"
        description = "simple gradle plugin that increments the version number"
        tags = listOf("versioning", "semantic-versioning", "kotlin")
        implementationClass = "io.github.katoys.version.incrementer.VersionIncrementerPlugin"
    }
}

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
