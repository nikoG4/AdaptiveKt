# Source Compatibility Review

This review audits the risk of compilation errors for consumers updating to the new `AdaptiveKt` primitives.

## 1. `AdaptiveGrid`
**Change:** Signature changed from `columns: Int = 0` to `columns: Int? = null`.
**Impact:** 100% source-compatible.
- Existing calls `AdaptiveGrid()` continue to work.
- Existing calls `AdaptiveGrid(columns = 2)` continue to work because `Int` implicitly boxes into `Int?`.
- **Reasoning:** `0` was an unintuitive default for "auto". `null` cleanly indicates "defer to `AdaptiveLayoutPolicy`".

## 2. `AdaptiveContainer`
**Change:** Defaults changed to consume `LocalAdaptiveLayoutInfo.current.contentMaxWidth`.
**Impact:** 100% source-compatible.
- Existing calls `AdaptiveContainer(maxWidth = 1200.dp)` continue to work as explicit overrides.
- Existing calls `AdaptiveContainer()` will now automatically map to the ambient configuration bounds rather than a hardcoded default.
- **Risk:** Slight visual change if developers previously relied on the exact hardcoded default without configuring the `AdaptiveConfig` limits. This is a deliberate and expected behavior improvement.

## 3. `AdaptiveNavigationMode` Location Move
**Change:** The file was physically moved from `adaptive-navigation/src/.../navigation` to `adaptive-core/src/.../navigation`.
**Impact:** 100% source-compatible.
- The `package io.github.adaptivekt.navigation` statement was deliberately preserved in the `adaptive-core` copy.
- Existing consumer imports like `import io.github.adaptivekt.navigation.AdaptiveNavigationMode` do not need to change.
- `adaptive-navigation` exposes `api(project(":adaptive-core"))` so gradle resolution graph boundaries are unharmed.

## 4. `AdaptiveNavigationScaffold`
**Change:** Reads `LocalAdaptiveLayoutInfo.current.navigationMode` by default instead of computing its own.
**Impact:** 100% source-compatible.
- Applications using the scaffold implicitly receive the globally configured policy definitions. No code changes are required for existing consumers.

## Existing Call Sites Audit
- `admin-demo`: Successfully compiled during tests.
- `ecommerce-demo`: Successfully compiles and relies extensively on `AdaptiveGrid(columns = null)`.
- `ai-workspace-demo`: Brand new demo natively built with these APIs.
- `docs-site`: Successfully compiles the gallery and layout catalog.

## Summary
No breaking API changes were introduced to existing layouts. The core abstractions (`AdaptiveApp`, `AdaptiveLayoutInfo`) are additive. Apps currently using `BoxWithConstraints` manually will not break, but they are highly encouraged to migrate to `AdaptivePage`.
