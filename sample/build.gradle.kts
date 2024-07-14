import io.github.katoys.version.incrementer.semantic.SemanticVersioning

plugins {
    id("io.github.katoys.version-incrementer") version "1.2.0"
}

group = "io.github.katoys"
version = SemanticVersioning(project.rootDir.resolve("sample/version.yml").path).current().value

println("version is $version")
