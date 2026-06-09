# AI Workspace Demo Android Target Decision

## Problem
The `examples/ai-workspace-demo` project failed to build with a standard `./gradlew build` command in environments lacking the Android SDK (such as generic CI environments or desktop-focused developer machines). The error produced was:
> SDK location not found. Define a valid SDK location with an ANDROID_HOME environment variable.

## Chosen Solution (Option A)
The Android target configuration, including the `com.android.application` plugin, `androidTarget`, `androidMain.dependencies`, and the `android {}` block, has been entirely removed from the `examples/ai-workspace-demo/build.gradle.kts`. 

## Rationale
The primary goal of the AI Workspace Demo at this phase is to showcase complex responsive layout features across Desktop and Web (Wasm). Removing the Android target ensures that any developer or CI environment can successfully execute `./gradlew build` on the demo project out of the box without requiring the heavy overhead of the Android SDK. 

## How to Enable Android Later
If Android validation is explicitly requested in the future:
1. Re-apply `id("com.android.application") version "8.5.2"` to the `plugins` block.
2. Re-add `androidTarget { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }` inside `kotlin`.
3. Re-add `androidMain.dependencies` with `compose.preview` and `activity-compose`.
4. Re-add the `android {}` DSL block specifying namespace, compileSdk, minSdk, targetSdk, etc.
5. If added back, you may need to either ensure `local.properties` specifies the `sdk.dir` or gate the android configurations conditionally (`if (hasProperty("android.injected.invoked.from.ide") || System.getenv("ANDROID_HOME") != null)`).
