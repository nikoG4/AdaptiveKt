# Adaptive Navigation Component Migration

## PR C5 Summary

PR C5 migrates safe internal navigation primitives to `:adaptive-components` while preserving navigation API and responsive behavior.

Principle: default simple code should produce a professional admin UI.

## Dependency Change

`adaptive-navigation` now depends on:

- `:adaptive-core`
- `:adaptive-components`
- Compose Foundation from the existing project setup

No dependency was added in the opposite direction, and no cycle was introduced.

## Audit Findings

Audited files:

- `adaptive-navigation/build.gradle.kts`
- `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/AdaptiveNavigationScaffold.kt`
- `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/NavigationSurfaces.kt`
- `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/AdaptiveNavItem.kt`
- `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/AdaptiveNavigationMode.kt`
- `adaptive-navigation/src/commonTest/kotlin/io/github/adaptivekt/navigation/NavigationModeTest.kt`
- Admin demo shell/topbar usage

Internal visual primitives found:

| Area | Current implementation | PR C5 decision | Reason |
|---|---|---|---|
| Compact menu toggle | Local pill button with raw `"x"`/hamburger text | Migrated to `AdaptiveIconButton` + `AdaptiveIcons` | Removes crude text affordances and reuses button shape/state defaults |
| Topbar divider | Local 1dp border | Migrated to `AdaptiveDivider` | Safe shared primitive with equivalent behavior |
| Sidebar/Rail/Drawer/Bottom surfaces | Local backgrounds, padding, item styling | Kept | `AdaptiveSurface` would add padding/borders that could alter navigation layout |
| Navigation item rendering | Local selected state/glyph/label logic | Kept | Selected behavior and rail/bottom sizing are navigation-specific |
| Fallback nav glyph | Initial letter | Kept | This is an intentional nav-label fallback, not an affordance icon |
| Navigation mode algorithm | Breakpoint mapping | Kept | Public behavior must remain stable |

## Migrated Helpers

- Compact drawer menu button now uses `AdaptiveIconButton`.
- Compact drawer open state now uses `AdaptiveIcons.Menu`.
- Compact drawer close state now uses `AdaptiveIcons.Close`.
- Content and compact topbars now use `AdaptiveDivider`.
- `AdaptiveIcons.Menu` was added as a minimal functional icon for navigation affordances.

## Helpers Kept

- Sidebar, drawer, rail, and bottom navigation surfaces remain local.
- Navigation item selected state and label rendering remain local.
- Rail and bottom navigation sizing/distribution remain unchanged.
- Fallback glyph initials remain local.

These were intentionally left in place because navigation layout is highly sensitive to width, padding, selected state, and compact behavior.

## Public API

No public API changed.

Preserved:

- `AdaptiveNavigationScaffold`
- `AdaptiveNavItem`
- `AdaptiveNavigationMode`
- `navigationModeForBreakpoint`

Breakpoints and navigation mode mapping are unchanged.

## Verification

Commands executed:

```powershell
.\gradlew.bat :adaptive-navigation:jvmTest --console=plain --stacktrace
.\gradlew.bat :admin-demo:build --console=plain --stacktrace
.\gradlew.bat build --console=plain --stacktrace
.\tools\capture-admin-demo.ps1
```

Results:

- `:adaptive-navigation:jvmTest` passed.
- `:admin-demo:build` passed.
- Full `build` passed.
- Visual captures completed: 60 screenshots.

Capture outputs:

- `build/visual-captures`
- `build/visual-captures/visual-capture-report.md`
- `build/adaptivekt-admin-demo-visual-captures.zip`

## Visual Review

Reviewed:

- `dashboard-compact-420x900.png`
- `dashboard-medium-720x900.png`
- `dashboard-expanded-1000x900.png`
- `dashboard-large-1440x900.png`
- `employees-compact-420x900.png`
- `settings-large-1440x900.png`

Observed:

- Compact topbar uses a real menu icon and pill icon button.
- Compact drawer close state uses a real close icon.
- Medium rail remains usable and keeps single-line truncated labels.
- Large/expanded sidebar keeps selected state and spacing.
- Topbar/content padding is stable.
- No double card/panel treatment was introduced in navigation surfaces.

Capture runs still emit SLF4J no-binding warnings from the demo-only Kamel/Ktor image stack, but screenshots are written and Gradle tasks complete successfully.

## Risks Remaining

- Sidebar/rail/drawer surfaces still use local colors and borders; a future theme/token pass can consolidate those safely.
- Navigation item rendering remains local because it is tightly coupled to selected state and responsive layouts.
- `AdaptiveIcons.Menu` expands the minimal icon set, but remains a functional component affordance rather than a general icon pack.

## Next Recommended PR

PR C6: documentation and dependency graph cleanup for the completed component-adoption pass, or a narrow token/color consolidation pass before larger visual refactors.
