package io.github.katoys.version.incrementer

interface Version {
    val type: Type
    val value: String

    enum class Type {
        Semantic;

        companion object {
            private val DEFAULT = Semantic

            fun from(value: String?): Type = value?.let {
                when (value.lowercase()) {
                    "semantic" -> Semantic
                    else -> throw IllegalArgumentException("'$value' is unknown type")
                }
            } ?: DEFAULT
        }
    }
}
