# Version Incrementer

[![build plugin](https://github.com/katoys/version-incrementer/actions/workflows/build-plugin.yml/badge.svg?branch=main)](https://github.com/katoys/version-incrementer/actions/workflows/build-plugin.yml)

## Description

- This is a simple gradle plugin that increments the version number in a YAML.
- It is intended to be used in a CI/CD pipeline to increment the version number in a YAML.
- The plugin supports semantic versioning. ( `MAJOR.MINOR.PATCH` )
- THe plugin supports version modifiers such as SNAPSHOT, GA.
- YAML sample )
  ```yaml
  type: Semantic
  value: 1.2.3-RELEASE
  ```

## Installation

- Add the following to your `build.gradle.kts` (Kotlin DSL). For Groovy DSL, please change accordingly.
    ```
    plugins {
        id("io.github.katoys.version-incrementer") version "1.0.0"
    }
    ```

## Usage

- Create YAML in the root of your project. (default `version.yml`)
    ```console
    gradle versioning -Paction=init -Pvalue=$value
    ```
- Increment semantic version.
    ```console
    gradle versioning -Paction=$action
    ```
  | $action       | description             | e.g. current version 1.2.3 |
  |---------------|-------------------------|----------------------------|
  | up-major      | increment major version | result is `2.0.0`          |
  | up-minor      | increment minor version | result is `1.3.0`          |
  | up-patch      | increment patch version | result is `1.2.4`          |

  - Increment version with modifier.
      ```console
      gradle versioning -Paction=$action -Pmodifier=modifier
      ```
- Append version modifier only. (version does not increment)
    ```console
    gradle versioning -Paction=append-modifier -Pmodifier=modifier
    ```
- Remove version modifier only. (version does not increment)
    ```console
    gradle versioning -Paction=remove-modifier
    ```
- Print current version. (from YAML)
    ```console
    gradle printCurrentVersion
    ```
- YAML path can be specified.
    ```console
    gradle printCurrentVersion -PyamlPath=your/path/to/version.yml
    gradle versioning -Paction=$action -PyamlPath=your/path/to/version.yml
    ```
