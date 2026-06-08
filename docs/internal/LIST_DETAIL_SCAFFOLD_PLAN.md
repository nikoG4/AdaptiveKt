# AdaptiveListDetailScaffold Plan

## 1. Overview
The `AdaptiveListDetailScaffold` is a generic, state-hoisted layout primitive that manages the common list/detail pattern across different breakpoints.

## 2. Chosen API
Located in: `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/AdaptiveListDetailScaffold.kt`

```kotlin
public data class AdaptivePaneSpec(
    val weight: Float = 1f,
    val minWidth: Dp = Dp.Unspecified,
    val preferredWidth: Dp = Dp.Unspecified,
    val maxWidth: Dp = Dp.Unspecified
)

public enum class AdaptiveListDetailCompactBehavior {
    ShowListUntilSelection,
    AlwaysShowList,
    AlwaysShowDetail
}

public enum class AdaptiveListDetailPaneMode {
    ListOnly,
    DetailOnly,
    ListAndDetail
}

public enum class AdaptiveListDetailResolvedMode {
    ListOnly,
    DetailOnly,
    ListAndDetail
}

public data class AdaptiveListDetailBehavior(
    val compact: AdaptiveListDetailCompactBehavior = AdaptiveListDetailCompactBehavior.ShowListUntilSelection,
    val medium: AdaptiveListDetailPaneMode = AdaptiveListDetailPaneMode.ListAndDetail,
    val expanded: AdaptiveListDetailPaneMode = AdaptiveListDetailPaneMode.ListAndDetail,
    val large: AdaptiveListDetailPaneMode = AdaptiveListDetailPaneMode.ListAndDetail,
    val showBackButtonOnCompactDetail: Boolean = true
)
```

## 3. Relation to AdaptiveTwoPane
`AdaptiveListDetailScaffold` is a higher-level state-managed component. It will internally delegate to `AdaptiveTwoPane` when the resolved mode is `ListAndDetail`. Since the scaffold explicitly manages the compact-mode transitions (rendering either a single list or single detail pane), we will invoke `AdaptiveTwoPane(collapseOnCompact = false)` so it enforces a side-by-side layout precisely when requested.

## 4. Default Behavior
- **Compact:** Resolves to `ListOnly` if no item is selected, and `DetailOnly` when an item is selected.
- **Medium, Expanded, Large:** Resolves to `ListAndDetail` by default, rendering both side-by-side.
- **Empty State:** Since `adaptive-layout` doesn't depend on `adaptive-feedback` (where `AdaptiveEmptyState` resides), we'll implement a clean internal default empty state using standard Compose primitives (`Text`, `Box`).
- **Compact Back Header:** We will provide a simple internal back header (a `Row` with a clickable `< Back` text/icon) that automatically triggers `onBackToList` when `DetailOnly` mode is active and `compactDetailHeader` is null.

## 5. Test Strategy
Tests will be implemented in `adaptive-layout/src/commonTest/kotlin/io/github/adaptivekt/layout/AdaptiveListDetailScaffoldTest.kt`.
It will thoroughly test the `resolveAdaptiveListDetailMode` pure function across all permutations of breakpoints and behaviors.

## 6. Compatibility Notes
This component doesn't introduce any breaking changes to existing APIs. It sits cleanly on top of `AdaptiveTwoPane` and `LocalAdaptiveLayoutInfo`. It avoids any manual usage of `BoxWithConstraints`.
