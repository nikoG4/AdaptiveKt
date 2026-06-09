# Test Coverage Gap Analysis

An audit was performed across `adaptive-core`, `adaptive-layout`, and the demo apps to ensure logical coverage of the layout policy system.

## Existing Test Coverage
- **`AdaptiveConfigTest`**: Verifies Breakpoint evaluation (`300.dp` -> `Compact`), LayoutInfo composition, and LayoutPolicy overriding (`compactColumns = 2`).
- **`AdaptiveListDetailScaffoldTest`**: Verifies 13 unique `AdaptiveListDetailBehavior` state intersections (e.g. `Compact` + Selection = `DetailOnly`).
- **`AiRouteCodecTest`**: Verifies 9 distinct deep link path bindings and the `fallbackToDashboard` behavior on invalid paths.
- **`LayoutGuardTest`**: Abstract compiler hook verifying that `BoxWithConstraints` is never used inside `ecommerce-demo`.
- **`verify-ai-workspace.ps1`**: Static powershell AST search ensuring `material-icons-extended` and `breakpointForWidth` are not leaked.

## Coverage Gaps Identified & Addressed
- **Gap:** `AdaptiveSpacingPolicy` resolution was not directly tested for default edge limits.
- **Fix:** Added `AdaptivePolicyTest.kt` covering `pagePaddingFor` bounds validation.
- **Gap:** `AdaptiveNavigationPolicy` resolution lacked an explicit unit test tracking the `Drawer` -> `Rail` -> `Sidebar` transitions.
- **Fix:** Added `AdaptivePolicyTest.kt` covering `resolve(breakpoint)`.

## Remaining Risks
- The library does not use Compose Screenshot Tests (`paparazzi` or `roborazzi`). This is an intentional choice for a multiplatform framework, as Compose Multiplatform rendering differences between JVM desktop and Wasm JS can cause extreme flakiness. All rendering logic depends purely on `dp` math and breakpoint enum comparisons, which are heavily unit tested in `commonTest`.

## Conclusion
The logic driving the `AdaptiveKt` framework is highly deterministic and well-tested. App developers do not need to rely on Compose instrumentation to verify that their panes will stack correctly; the unit tests already prove `resolveAdaptiveListDetailMode` works flawlessly.
