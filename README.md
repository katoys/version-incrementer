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

### Gradle task execution

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

  - Increment version and append modifier.
      ```console
      gradle versioning -Paction=$action -Pmodifier=$modifier
      ```
  - Increment version, append modifier and sequential number.
      ```console
      gradle versioning -Paction=$action -Pmodifier=$modifier -PaddModifiersSeq=$naturalNumber
      ```
      - `addModifiersSeq` is optional natural number. If present, it adds to modifier.
        ```console
        $ gradle printCurrentVersion -q
        1.0.0
        $ gradle versioning -Paction=up-minor -Pmodifier=alpha -PaddModifiersSeq=1
        1.0.0-alpha.1
        ```
- Append version modifier only. (version does not increment)
    ```console
    gradle versioning -Paction=append-modifier -Pmodifier=$modifier
    ```
    - `addModifiersSeq` is optional. Default is `false`.
- Increment modifier sequential number. (version does not increment)
    ```console
    gradle versioning -Paction=up-modifier-seq
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

### Call function in `build.gradle.kts`

```kotlin
import io.github.katoys.version.incrementer.semantic.SemanticVersioning

val versioning = SemanticVersioning()
versioning.init("0.0.1") // init YAML -> 0.0.1
versioning.upMajor() // increment major version -> 1.0.0
versioning.upMinor() // increment minor version -> 1.1.0
versioning.upPatch() // increment patch version -> 1.1.1
versioning.modifier("alpha") // append 'alpha' as modifier -> 1.1.1-alpha
versioning.addModifierSeq(1) // append sequential number to alpha -> 1.1.1-alpha.1
versioning.upModifierSeq() // increment modifiers sequential number -> 1.1.1-alpha.2
versioning.modifier() // remove modifier -> 1.1.1
versioning.current() // get current version
```
