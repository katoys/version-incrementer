# Version Incrementer

## Description

- This is a simple gradle plugin that increments the version number in a YAML.
- It is intended to be used in a CI/CD pipeline to increment the version number in a YAML.
- The plugin supports semantic versioning. ( `MAJOR.MINOR.PATCH` )
- THe plugin supports version modifiers such as SNAPSHOT, GA.

## Usage plugin

### Installation

- Add the following to your `build.gradle.kts` (Kotlin DSL). For Groovy DSL, please change accordingly.
    ```
    plugins {
        id("io.github.katoys.version-incrementer") version "1.0.0"
    }
    
    apply(plugin = "io.github.katoys.version-incrementer")
    ```

### Execute gradle task

- Create `version.yml` in the root of your project.
    ```console
    gradle versioning -Paction=init -Pvalue=0.0.1
    ```
- Get current version. (from `version.yml`)
    ```console
    gradle versioning -Paction=current
    ```
- Increment semantic version.
    ```console
    gradle versioning -Paction=$action
    ```
  | $action       | description             |
  |---------------|-------------------------|
  | up-major      | increment major version |
  | up-minor      | increment minor version |
  | up-patch      | increment patch version |
- Append supplement of version.
    ```console
    gradle versioning -Paction=append-supplement -Psuffix=supplement
    ```
- Remove supplement of version.
    ```console
    gradle versioning -Paction=remove-supplement
    ```
- YAML path can be specified.
    ```console
    gradle versioning -Paction=$action -PyamlPath=your/path/to/version.yml
    ```

## Usage function

### Installation

- Add the following to your `build.gradle.kts` (Kotlin DSL). For Groovy DSL, please change accordingly.
    ```build.gradle.kts
    dependencies {
        implementation("io.github.katoys:version-incrementer:1.0.0")
    } 
    ```

### Execute function

- Following is sample code in Kotlin. For Groovy, please change accordingly.
  ```
  import io.github.katoys.version.incrementer.semantic.SemanticVersionIncrementer
  ```
  ```
  val versioning = SemanticVersionIncrementer()
  versioning.init("0.0.1") // create version.yml
  versioning.current() // get current version
  versioning.upMajor() // increment major version
  versioning.upMinor() // increment minor version
  versioning.upPatch() // increment patch version
  versioning.appendModifier("-SNAPSHOT") // append modifier of version
  versioning.removeModifier() // remove modifier of version
  ```