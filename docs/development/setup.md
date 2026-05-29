# Development Setup

AdaptiveKt is validated with JDK 17.

Use JDK 17 for local Gradle builds. Newer JDKs can fail before project compilation starts; for example, JDK 25 can make Kotlin/Gradle fail with `IllegalArgumentException: 25.0.1`.

## Windows JDK 17 Session

Set JDK 17 for the current PowerShell session:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat build --console=plain --stacktrace
```

Do not commit machine-specific JDK paths. Do not set `org.gradle.java.home` in the repository `gradle.properties`.

## Environment Check

Run:

```powershell
.\tools\check-dev-environment.ps1
```

The script prints:

- `JAVA_HOME`
- `java -version`
- whether Java major version is 17
- whether `local.properties` exists, without printing its contents
- Android SDK configuration guidance

It does not modify the system.

## Android SDK

Android library tasks need an Android SDK. Configure one of:

- `ANDROID_HOME`
- `ANDROID_SDK_ROOT`
- local `local.properties` with `sdk.dir=...`

`local.properties` is local machine state and must not be committed.

## CI

GitHub Actions configures Java 17 explicitly with `actions/setup-java`. CI is the source of truth for the supported Java major version.
