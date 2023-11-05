# Version Incrementer

## Description

- This is a simple script that increments the version number in a file.
- It is intended to be used in a CI/CD pipeline to increment the version number in a file.
- The script will increment the version number in the file by 1.
- The script will also update the version number in the file if it is already present.
- The script will also create the file if it does not exist.

## Usage plugin

### Installation

- Add the following to your `build.gradle.kts` (Kotlin DSL). For Groovy DSL, please change accordingly.
    ```build.gradle.kts
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
- Append suffix of version.
    ```console
    gradle versioning -Paction=append-suffix -Psuffix=$suffix
    ```
- Remove suffix of version.
    ```console
    gradle versioning -Paction=remove-suffix
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
  versioning.appendSuffix("-SNAPSHOT") // append suffix of version
  versioning.removeSuffix() // remove suffix of version
  ```