package io.github.katoys.version.incrementer

interface Version {
    val type: Type
    val value: String

    enum class Type {
        Semantic
    }
}
