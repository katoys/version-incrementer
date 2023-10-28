package io.github.katoys.version.incrementer

interface VersionRepository {

    fun find(): Version

    fun save(version: Version)
}
