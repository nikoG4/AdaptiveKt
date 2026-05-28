# Module Dependency Graph

PR C6 records the current module graph after the component-migration PRs. PR P0/P1 updates this document with platform-target context; dependency edges remain unchanged.

## Current Graph

```text
:adaptive-core

:adaptive-components
  -> :adaptive-core

:adaptive-layout
  -> :adaptive-core

:adaptive-feedback
  -> :adaptive-core
  -> :adaptive-components

:adaptive-navigation
  -> :adaptive-core
  -> :adaptive-components

:adaptive-forms
  -> :adaptive-core
  -> :adaptive-layout
  -> :adaptive-components

:adaptive-data
  -> :adaptive-core
  -> :adaptive-layout
  -> :adaptive-feedback
  -> :adaptive-components

:admin-demo
  -> :adaptive-core
  -> :adaptive-layout
  -> :adaptive-navigation
  -> :adaptive-forms
  -> :adaptive-data
  -> :adaptive-feedback
  -> :adaptive-components
  -> media.kamel:kamel-image:0.7.3
  -> io.ktor:ktor-client-cio:2.3.4
```

## Confirmed Consumers Of `:adaptive-components`

- `:admin-demo`
- `:adaptive-data`
- `:adaptive-forms`
- `:adaptive-feedback`
- `:adaptive-navigation`

`adaptive-components` still depends only on `adaptive-core` plus the existing Compose Foundation/Runtime/UI setup. That keeps it usable as the shared primitive layer without pulling feature modules upward.

## Platform Target Context

The library modules now declare JVM, Android, and iOS targets:

- `jvm()`
- `androidTarget()`
- `iosX64()`
- `iosArm64()`
- `iosSimulatorArm64()`

The target declarations do not change dependency direction. PR U0/U1 upgrades to Kotlin `2.1.21` and Compose Multiplatform `1.8.2`, so library modules now also declare `wasmJs { browser() }`.

## Dependency Rules

Allowed:

- Feature modules may depend on `:adaptive-core`.
- Feature modules may depend on `:adaptive-components` for shared visual primitives.
- `:adaptive-data` currently depends on `:adaptive-layout` and `:adaptive-feedback`.
- `:adaptive-forms` currently depends on `:adaptive-layout`.
- `:admin-demo` may depend on all local modules.
- Demo-only runtime dependencies such as Kamel/Ktor are allowed only in `:admin-demo`.

Prohibited:

- `:adaptive-components` must not depend on data, forms, feedback, navigation, layout, or admin-demo.
- Library modules must not depend on `:admin-demo`.
- Image loading libraries must not move into core/components/data/forms/feedback/navigation without a dedicated media design PR.
- Material 3, icon-pack dependencies, and backend dependencies remain out of scope.
- Web/Wasm target enablement remains a build-target concern, not a dependency graph change. `:admin-demo` Web/Wasm remains a future PR.

## Cycle Risk

No direct cycle is present in the audited Gradle files. The main risk area is future migration work that might make `:adaptive-components` import feature-specific APIs. Keep components primitive and slot-based to avoid cycles.

## Demo-Only Runtime Dependencies

Remote image loading remains scoped to `:admin-demo`:

- `media.kamel:kamel-image:0.7.3`
- `io.ktor:ktor-client-cio:2.3.4`

These are used for demo employee avatars and product thumbnails only. AdaptiveKt core/components remain independent from image loading libraries.
