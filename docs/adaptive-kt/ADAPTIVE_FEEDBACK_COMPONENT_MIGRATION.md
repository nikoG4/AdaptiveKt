# Adaptive Feedback Component Migration

## PR C4 Summary

PR C4 migrates safe internal visuals in `:adaptive-feedback` to `:adaptive-components` while preserving the public feedback API.

Principle: default simple code should produce a professional admin UI.

## Dependency Change

`adaptive-feedback` now depends on:

- `:adaptive-core`
- `:adaptive-components`
- Compose Foundation from the existing project setup

No dependency was added in the opposite direction, and no cycle was introduced.

## Audit Findings

Audited files:

- `adaptive-feedback/build.gradle.kts`
- `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/AdaptiveEmptyState.kt`
- `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/AdaptiveErrorState.kt`
- `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/AdaptiveLoadingState.kt`
- `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/FeedbackStateLayout.kt`
- Admin demo invoice states

Internal visual primitives found:

| Area | Current implementation | PR C4 decision | Reason |
|---|---|---|---|
| Shared state container | Local centered column with padding/max width | Migrated to `AdaptiveSurface` | Gives Empty/Loading/Error a shared professional panel |
| Empty default glyph | Raw text symbol | Migrated to `AdaptiveIcons.Search` | Removes crude text-as-icon default |
| Error default glyph | Raw `!` symbol | Migrated to `AdaptiveIcons.Close` | Removes crude text-as-icon default using existing icon set |
| Loading indicator | Local Foundation-only indicator | Kept | Later animated in UI-ANIM-1 with Compose Multiplatform animation APIs, still without Material or external dependencies |
| Actions | User-provided slots | Kept | `action` and `retryAction` are caller-owned composables |
| Text rendering | Local `BasicText` helper | Kept | No shared body/title text primitive exists yet |

## Migrated Helpers

- `FeedbackStateLayout` now uses `AdaptiveSurface`.
- `AdaptiveEmptyState` default icon now uses `AdaptiveIcons.Search`.
- `AdaptiveErrorState` default icon now uses `AdaptiveIcons.Close`.

## Helpers Kept

- `SimpleLoadingIndicator` remains local, Foundation-only, and animated.
- `SimpleText` remains local.
- `action` and `retryAction` remain slots controlled by the caller.

These were intentionally left in place to preserve public API and avoid forcing component choices into caller-owned slots.

## Public API

No public API changed.

Preserved:

- `AdaptiveEmptyState`
- `AdaptiveLoadingState`
- `AdaptiveErrorState`

`FeedbackStateLayout` remains internal.

## Capture Coverage

PR C4 adds focused invoice capture screens so feedback states can be reviewed without manual clicks:

- `invoices-empty`
- `invoices-loading`
- `invoices-error`

The standard capture script now includes those states in compact, medium, expanded, and large breakpoints.

## Verification

Commands executed:

```powershell
.\gradlew.bat :adaptive-feedback:jvmTest --console=plain --stacktrace
.\gradlew.bat :admin-demo:build --console=plain --stacktrace
.\gradlew.bat build --console=plain --stacktrace
.\tools\capture-admin-demo.ps1
```

Results:

- `:adaptive-feedback:jvmTest` passed (`NO-SOURCE`, module compiles).
- `:admin-demo:build` passed.
- Full `build` passed.
- Visual captures completed: 60 screenshots.

Capture outputs:

- `build/visual-captures`
- `build/visual-captures/visual-capture-report.md`
- `build/adaptivekt-admin-demo-visual-captures.zip`

## Visual Review

Reviewed:

- `invoices-empty-compact-420x900.png`
- `invoices-loading-compact-420x900.png`
- `invoices-error-compact-420x900.png`
- `invoices-empty-large-1440x900.png`
- `invoices-loading-large-1440x900.png`
- `invoices-error-large-1440x900.png`

Observed:

- Empty/Error states no longer show raw text glyphs.
- Feedback panels are centered and keep a reasonable max width on large screens.
- Compact feedback states fit without overflow.
- Loading remains simple, presentable, and animated.
- Action slots still render the caller-provided buttons.

Capture runs still emit SLF4J no-binding warnings from the demo-only Kamel/Ktor image stack, but screenshots are written and Gradle tasks complete successfully.

## Risks Remaining

- Error uses the existing `Close` icon because `AdaptiveIcons` does not yet include a dedicated warning/error icon.
- Loading indicator remains local; a shared progress primitive may be useful later.
- Text styles remain local until a shared typography/theme layer exists.

## Next Recommended PR

PR C5: controlled migration of safe `adaptive-navigation` visuals to `:adaptive-components`, focusing on shared surfaces/dividers/icons only where it does not change navigation API.
