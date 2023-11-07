import io.github.katoys.version.incrementer.semantic.SemanticVersioning

plugins {
    id("io.github.katoys.version-incrementer") version "1.0.0"
}

group = "io.github.katoys"
version = SemanticVersioning().current().value

println("version is $version")
