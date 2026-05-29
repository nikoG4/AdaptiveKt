# Development

## Local Requirements

- JDK 17
- Android SDK with platform 34 for Android targets
- Node/Yarn managed by the Kotlin Gradle plugin for Wasm tasks

See [Development Setup](setup.md) for JDK 17 shell setup and diagnostics.

## Verification

```powershell
.\gradlew.bat build --console=plain --stacktrace
.\gradlew.bat :admin-demo:wasmJsBrowserDistribution --console=plain --stacktrace
```

Desktop capture uses Robot/AWT and should stay JVM-only. Web capture uses Playwright and should stay tooling-only.
