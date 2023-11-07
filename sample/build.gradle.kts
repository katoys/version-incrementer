import io.github.katoys.version.incrementer.semantic.SemanticVersioning

plugins {
    id("io.github.katoys.version-incrementer") version "1.0.0"
}

group = "io.github.katoys"
version = if (project.properties.containsKey("yamlPath")) {
    SemanticVersioning(project.properties["yamlPath"].toString())
} else {
    SemanticVersioning()
}.current().value

println("version is $version")
