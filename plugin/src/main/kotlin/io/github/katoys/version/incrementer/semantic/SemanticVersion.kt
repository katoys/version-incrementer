package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.Version

data class SemanticVersion(
    val element: Element = Element(),
) : Version {
    override val type = Version.Type.Semantic
    override val value = element.run {
        "$major.$minor.$patch${modifier?.trim()?.let { "-$it" } ?: ""}"
    }

    fun upMajor() = copy(element = element.copy(major = element.major + 1, minor = 0, patch = 0))
    fun upMinor() = copy(element = element.copy(minor = element.minor + 1, patch = 0))
    fun upPatch() = copy(element = element.copy(patch = element.patch + 1))
    fun appendModifier(modifier: String?) = copy(element = element.copy(modifier = modifier?.ifEmpty { null }))
    fun removeModifier() = copy(element = element.copy(modifier = null))

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
                    modifier = if (it.size > 3) it[3] else null
                )
            }

        fun from(
            major: Int = 0,
            minor: Int = 0,
            patch: Int = 0,
            modifier: String? = null
        ) = SemanticVersion(
            Element(
                major = major,
                minor = minor,
                patch = patch,
                modifier = modifier?.ifEmpty { null }
            )
        )
    }

    data class Element(
        val major: Int = 0,
        val minor: Int = 0,
        val patch: Int = 0,
        val modifier: String? = null
    ) {
        init {
            if (major < 0) throw IllegalArgumentException("major must be greater than or equal to 0")
            if (minor < 0) throw IllegalArgumentException("minor must be greater than or equal to 0")
            if (patch < 0) throw IllegalArgumentException("patch must be greater than or equal to 0")
            if (modifier?.isEmpty() == true) throw IllegalArgumentException("modifier must be not empty")
        }
    }
}
