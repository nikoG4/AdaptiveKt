# Adaptive Core

Purpose: shared breakpoints, tokens, adaptive info, values, and visibility helpers.

Use it when a screen or component needs breakpoint-aware behavior.

Primary API: `AdaptiveBreakpoint`, `AdaptiveInfo`, `AdaptiveContent`, `rememberAdaptiveInfo`, `adaptiveValue`, `AdaptiveTokens`.

Simple example:

```kotlin
AdaptiveContent {
    val columns = adaptiveValue(1, medium = 2, expanded = 3, large = 4)
}
```

Responsive notes: breakpoints are compact, medium, expanded, and large.

Multiplatform notes: common Compose code, with JVM, Android, Wasm, and iOS targets declared where supported.

Limitations: iOS validation requires macOS.
