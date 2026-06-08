# AI Workspace Demo Wasm Build Hardening

## Problem
The task `wasmJsBrowserDistribution` for the `examples/ai-workspace-demo` project was failing with an Out of Memory error during the Kotlin to WebAssembly optimization phase:
> `java.lang.OutOfMemoryError: GC overhead limit exceeded`
> `Not enough memory to run compilation. Try to increase it via 'gradle.properties'`

## Root Cause
The `wasmJs` optimization pass is notoriously memory-heavy. Without explicit JVM arguments in `gradle.properties`, the Gradle daemon defaults (often 512MB or 1GB) are insufficient to process the entire shared adaptive multiplatform UI layer into the Wasm binary.

## Fix
Added the following to `examples/ai-workspace-demo/gradle.properties`:
```properties
# Memory tuning for Wasm compilation
org.gradle.jvmargs=-Xmx4g -Dfile.encoding=UTF-8
kotlin.daemon.jvmargs=-Xmx3g
kotlin.incremental=true
org.gradle.parallel=true
org.gradle.caching=true
```

## Final Memory Configuration
- Gradle JVM uses a max heap of 4GB (`-Xmx4g`).
- Kotlin Compiler Daemon uses a max heap of 3GB (`-Xmx3g`).
- Parallel processing and caching have been enabled to improve overall performance.

## Results
After applying these settings, running `./gradlew wasmJsBrowserDistribution` successfully completes compilation, producing the optimized `.wasm` and `.js` distribution bundles without OOM exceptions.
