package io.github.katoys.version.incrementer.semantic

import io.github.katoys.version.incrementer.Version
import java.util.regex.Pattern

data class SemanticVersion(
    val element: Element = Element(),
) : Version {
    override val type = Version.Type.Semantic
    override val value = element.run {
        "$major.$minor.$patch${modifier?.let { "-${it.value}" } ?: ""}"
    }

    fun upMajor() = copy(element = element.copy(major = element.major + 1, minor = 0, patch = 0))
    fun upMinor() = copy(element = element.copy(minor = element.minor + 1, patch = 0))
    fun upPatch() = copy(element = element.copy(patch = element.patch + 1))
    fun appendModifier(modifier: String?) = copy(element = element.copy(modifier = Modifier.fromOrNull(modifier)))
    fun nextModifierSeq() = copy(element = element.copy(modifier = element.modifier?.nextSeq()))
    fun removeModifier() = copy(element = element.copy(modifier = null))

    companion object {

        private val pattern = Pattern.compile("""^(?<major>\d+)\.(?<minor>\d+)\.(?<patch>\d+)(-(?<modifier>.+))?$""")

        fun from(value: String) = value
            .let { pattern.matcher(it) }
            .also { if (!it.find()) throw IllegalArgumentException("invalid version format: $value") }
            .let {
                from(
                    major = it.group("major").toInt(),
                    minor = it.group("minor").toInt(),
                    patch = it.group("patch").toInt(),
                    modifier = it.group("modifier")
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
                modifier = Modifier.fromOrNull(modifier)
            )
        )
    }

    data class Element(
        val major: Int = 0,
        val minor: Int = 0,
        val patch: Int = 0,
        val modifier: Modifier? = null
    ) {
        init {
            if (major < 0) throw IllegalArgumentException("major must be greater than or equal to 0")
            if (minor < 0) throw IllegalArgumentException("minor must be greater than or equal to 0")
            if (patch < 0) throw IllegalArgumentException("patch must be greater than or equal to 0")
        }
    }

    data class Modifier(
        val body: String,
        val sequence: Int? = null
    ) {
        val value: String
            get() = if (sequence == null) body else "$body.$sequence"

        init {
            if (body.isEmpty()) throw IllegalArgumentException("modifier body must be not empty")
            if (sequence != null && sequence < 0) throw IllegalArgumentException("modifiers sequential number must be greater than or equal to 0")
        }

        fun nextSeq() = copy(sequence = (sequence ?: 0) + 1)

        companion object {

            private val patternHasSeq = Pattern.compile("""^(?<body>.+)(\.(?<sequence>\d+))$""")
            private val patternNoSeq = Pattern.compile("""^(?<body>.+)$""")

            fun fromOrNull(value: String?) = if (value.isNullOrEmpty()) null else from(value)

            private fun from(value: String): Modifier {
                val hasSeq = patternHasSeq.matcher(value)
                if (hasSeq.find()) {
                    return Modifier(
                        body = hasSeq.group("body"),
                        sequence = hasSeq.group("sequence")?.toInt()
                    )
                }
                val noSeq = patternNoSeq.matcher(value)
                if (noSeq.find()) {
                    return Modifier(
                        body = noSeq.group("body")
                    )
                }
                throw IllegalArgumentException("invalid version modifier format: $value")
            }
        }
    }
}
