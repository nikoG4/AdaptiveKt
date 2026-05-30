# Local Publishing

PUBLISH-0B adds a local Maven publishing dry-run for AdaptiveKt library modules.

This does not publish to Maven Central, does not publish snapshots remotely, does not sign artifacts, and does not require secrets.

## Coordinates

The single source for group and version is `gradle.properties`:

```properties
GROUP=io.github.nikog4.adaptivekt
VERSION_NAME=0.1.0-alpha01
```

Publishable modules:

- `io.github.nikog4.adaptivekt:adaptive-core:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-layout:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-feedback:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-navigation:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-forms:0.1.0-alpha01`
- `io.github.nikog4.adaptivekt:adaptive-data:0.1.0-alpha01`

Non-publishable modules:

- `admin-demo`
- `docs-site`

Those modules stay in `settings.gradle.kts`, but they do not receive `maven-publish` configuration.

## Publish Locally

Run:

```powershell
.\gradlew.bat publishAllPublicationsToLocalTestRepository --console=plain --stacktrace
```

Output:

```text
build/local-maven/
```

## Consume From The Local Repository

In a temporary consumer build, add:

```kotlin
repositories {
    maven {
        url = uri("path/to/AdaptiveKt/build/local-maven")
    }
    google()
    mavenCentral()
}
```

Then depend on the module you want:

```kotlin
dependencies {
    implementation("io.github.nikog4.adaptivekt:adaptive-components:0.1.0-alpha01")
}
```

Kotlin Multiplatform metadata resolves the appropriate platform artifact for the consumer target.

## Verify With A Consumer Smoke Test

Run:

```powershell
.\tools\verify-local-publishing-consumer.ps1
```

The script:

- recreates `build/local-consumer-smoke`;
- creates a standalone Gradle project outside the main build;
- uses `build/local-maven` as a Maven repository;
- declares AdaptiveKt dependencies by Maven coordinates;
- does not use `project(":...")`;
- does not use `includeBuild`;
- compiles JVM and Wasm JS source sets with:

```powershell
compileKotlinJvm compileKotlinWasmJs
```

The smoke imports and compiles references to:

- `AdaptiveBreakpoint`, `AdaptiveTheme`, `AdaptiveColorSchemes`;
- `AdaptiveButton`, `AdaptiveCard`;
- `AdaptiveGrid`;
- `EmptyState`;
- `AdaptiveNavigationScaffold`, `AdaptiveNavItem`;
- `AdaptiveFormLayout`;
- `AdaptiveDataView`, `AdaptiveDataColumn`, `AdaptiveDataContent`.

This verifies that the local Maven artifacts are consumable by an external Kotlin Multiplatform project for JVM and Wasm compilation.

## What This Does Not Do

- It does not publish to Maven Central.
- It does not configure Sonatype Central Portal.
- It does not configure signing.
- It does not use secrets.
- It does not create a release or tag.
- It does not publish `admin-demo` or `docs-site`.
- It does not validate iOS publication consumption; that must run on macOS.

## Maven Central Follow-Up

PUBLISH-1 should add:

- final namespace confirmation;
- signing configuration that is disabled unless credentials are present;
- POM validation for Maven Central requirements;
- docs/source artifact strategy;
- a manual-only GitHub Actions publish workflow;
- Central Portal dry-run/release process documentation.
