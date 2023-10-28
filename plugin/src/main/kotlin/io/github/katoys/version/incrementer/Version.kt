package io.github.katoys.version.incrementer

interface Version {
    val value: String
}

data class SemanticVersion(
    val element: Element = Element(),
) : Version {
    override val value = element.run {
        "$major.$minor.$patch${suffix?.trim()?.let { "-$it" } ?: ""}"
    }

    fun upMajor() = copy(element = element.copy(major = element.major + 1, minor = 0, patch = 0))
    fun upMinor() = copy(element = element.copy(minor = element.minor + 1, patch = 0))
    fun upPatch() = copy(element = element.copy(patch = element.patch + 1))
    fun appendSuffix(suffix: String?) = copy(element = element.copy(suffix = suffix?.ifEmpty { null }))
    fun removeSuffix() = copy(element = element.copy(suffix = null))

    companion object {

        private val regex = Regex("""\d+\.\d+\.\d+(-.+)?""")

        fun from(value: String) = value
            .also { if (!regex.matches(it)) throw IllegalArgumentException("invalid version format: $it") }
            .split(".", "-")
            .let {
                from(
                    major = it[0].toInt(),
                    minor = it[1].toInt(),
                    patch = it[2].toInt(),
                    suffix = if (it.size > 3) it[3] else null
                )
            }

        fun from(
            major: Int = 0,
            minor: Int = 0,
            patch: Int = 0,
            suffix: String? = null
        ) = SemanticVersion(
            Element(
                major = major,
                minor = minor,
                patch = patch,
                suffix = suffix?.ifEmpty { null }
            )
        )
    }

    data class Element(
        val major: Int = 0,
        val minor: Int = 0,
        val patch: Int = 0,
        val suffix: String? = null
    ) {
        init {
            if (major < 0) throw IllegalArgumentException("major must be greater than or equal to 0")
            if (minor < 0) throw IllegalArgumentException("minor must be greater than or equal to 0")
            if (patch < 0) throw IllegalArgumentException("patch must be greater than or equal to 0")
            if (suffix?.isEmpty() == true) throw IllegalArgumentException("suffix must be not empty")
        }
    }
}
