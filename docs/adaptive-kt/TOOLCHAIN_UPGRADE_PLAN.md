# Toolchain Upgrade Plan

PR U0/U1 upgrades the Kotlin/Compose/Android toolchain enough to enable Wasm targets for AdaptiveKt library modules.

## Starting Versions

| Tool | Previous version |
|---|---:|
| Gradle wrapper | `8.13` |
| Kotlin Gradle Plugin | `1.9.10` |
| Compose Multiplatform Gradle Plugin | `1.5.1` |
| Android Gradle Plugin | `8.2.2` |
| Compose compiler plugin | Not applied separately |
| Java target | 17 |
| Android compileSdk | 34 |
| Android minSdk | 23 |
| Demo-only Kamel | `media.kamel:kamel-image:0.7.3` |
| Demo-only Ktor CIO | `io.ktor:ktor-client-cio:2.3.4` |

## Selected Versions

| Tool | New version | Reason |
|---|---:|---|
| Gradle wrapper | `8.13` | Already modern enough for this controlled upgrade. |
| Kotlin Gradle Plugin | `2.1.21` | Enables `wasmJs` DSL and matches the resolved Kotlin/Wasm stdlib patch level. |
| Compose Multiplatform Gradle Plugin | `1.8.2` | Stable Compose Multiplatform release aligned with Kotlin `2.1.x`. |
| Compose compiler plugin | `2.1.21` | Required with Kotlin 2.x Compose builds; version matches Kotlin. |
| Android Gradle Plugin | `8.5.2` | Minimal AGP bump from 8.2.x while keeping Java 17 and compileSdk 34. |

The first attempt used Kotlin `2.1.20`, which compiled but emitted a Kotlin/Wasm warning that the stdlib was `2.1.21-release-317` while the compiler was `2.1.20`. The patch-level bump to `2.1.21` removed that mismatch.

## Scope

Implemented:

- Toolchain version upgrade.
- Compose compiler plugin application.
- `wasmJs { browser() }` target declarations for library modules.
- Wasm compile/test verification for library modules.
- Minimal source compatibility fix in `adaptive-feedback`.

Not implemented:

- `admin-demo` Wasm entry point.
- Playwright screenshots.
- Dark mode.
- `AdaptiveSelect`.
- New public APIs.
- Kamel/image loading in library modules.

## Wasm Target Scope

Wasm was enabled only in:

- `:adaptive-core`
- `:adaptive-components`
- `:adaptive-layout`
- `:adaptive-feedback`
- `:adaptive-navigation`
- `:adaptive-forms`
- `:adaptive-data`

`:admin-demo` remains JVM/Desktop-only.

## Errors Encountered

### Missing Wasm DSL Before Upgrade

With Kotlin `1.9.10` and Compose `1.5.1`, adding `wasmJs { browser() }` failed because `ExperimentalWasmDsl`, `wasmJs`, and `browser` were unresolved.

### Kotlin Daemon OOM

After enabling Wasm and Native tasks, the first build failed with:

```text
Not enough memory to run compilation.
kotlin.daemon.jvmargs=-Xmx<size>
```

Resolution:

```properties
org.gradle.jvmargs=-Xmx3g -Dfile.encoding=UTF-8
kotlin.daemon.jvmargs=-Xmx3g
```

### Compose/Kotlin Source Compatibility

`adaptive-feedback` failed after the upgrade because `TextStyle.textAlign` expects a non-null `TextAlign` in the newer Compose API.

Resolution:

- Changed internal `SimpleText` default from `TextAlign? = null` to `TextAlign = TextAlign.Start`.
- No public API changed.

## Remaining Warnings

- iOS targets are declared but skipped on Windows because Kotlin/Native Apple target compilation requires macOS.
- `admin-demo` still has a deprecated `withJava()` call.
- `captureVisuals` still uses deprecated Gradle `javaexec`; this is existing desktop tooling.
- Wasm npm configurations emit Gradle configuration-time resolution warnings.
- Full build emits Gradle 9 deprecation warnings.

## Decision

Use Kotlin `2.1.21`, Compose Multiplatform `1.8.2`, Compose compiler plugin `2.1.21`, AGP `8.5.2`, and Gradle `8.13` for U0/U1.

This is intentionally not the newest possible stack. It is the smallest stable-looking jump that enables Wasm for the library modules while preserving Desktop/JVM and Android builds.
