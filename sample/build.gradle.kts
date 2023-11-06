import io.github.katoys.version.incrementer.semantic.SemanticVersioning

plugins {
    id("io.github.katoys.version-incrementer") version "0.9.0"
}

group = "io.github.katoys"
version = "1.0.0"

tasks.register("functionSample") {
    doLast {
        val value = project.properties["init"].toString()
        val versioning = SemanticVersioning()

        versioning.init(value) // init version
        versioning.upMajor() // increment major version
        versioning.upMinor() // increment minor version
        versioning.upPatch() // increment patch version
        versioning.modifier("SNAPSHOT") // append 'SNAPSHOT' as modifier
        versioning.modifier() // remove modifier
        versioning.current() // get current version
        println("version: ${SemanticVersioning().current().value}")
    }
}
