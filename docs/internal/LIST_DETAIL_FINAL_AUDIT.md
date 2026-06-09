# Final API Audit - AdaptiveListDetailScaffold

## Evaluation

1. **Is `AdaptiveListDetailScaffold` generic and domain-neutral?**
   Yes. It only uses a generic `T` parameter and is completely decoupled from chat, tickets, or email specifics.

2. **Is the minimal usage actually minimal?**
   Yes. The simplest call requires just `selectedItem`, `onBackToList`, `listPane`, and `detailPane`. Default padding, pane sizing, and fallback behavior automatically take over.

3. **Can developers configure pane weights?**
   Yes. `listPaneSpec` and `detailPaneSpec` accept `AdaptivePaneSpec`, allowing customized weights, min, max, and preferred widths.

4. **Can developers configure compact behavior?**
   Yes. `AdaptiveListDetailBehavior` accepts `AdaptiveListDetailCompactBehavior`, allowing an app to force `AlwaysShowList` or `AlwaysShowDetail` regardless of selection, although the default accurately mirrors standard OS transitions (`ShowListUntilSelection`).

5. **Can developers override empty detail?**
   Yes. The `emptyDetail` parameter is a generic composable slot with a clean fallback.

6. **Can developers override/disable compact back header?**
   Yes. The default header can be disabled via `behavior.showBackButtonOnCompactDetail = false` or cleanly overridden using `compactDetailHeader`.

7. **Does it avoid `LocalAdaptiveLayoutInfo` in app code?**
   Yes. `AdaptiveListDetailScaffold` intrinsically uses `LocalAdaptiveLayoutInfo` so app-level consumers don't have to write custom breakpoint-switching logic.

8. **Does it reuse `AdaptiveTwoPane` or at least align with it?**
   Yes. When resolved to `ListAndDetail`, it wraps the components in `AdaptiveTwoPane` (bypassing normal compact stacking via `collapseOnCompact = false`, as the scaffold has a custom list-only/detail-only compact override).

9. **Are pure resolver tests passing?**
   Yes. The `AdaptiveListDetailScaffoldTest` exhaustively covers the resolution logic.

10. **Are docs clear about when to use it vs `AdaptiveTwoPane`?**
    Yes. The README explicitly outlines that `AdaptiveListDetailScaffold` is for selectable list state, whereas `AdaptiveTwoPane` is for static side-by-side structures.

## Static Usage Report
A final sweep verifies no instances of `BoxWithConstraints` entered application code during this change. The `ecommerce-demo` remains untouched to avoid routing fragmentation, adhering strictly to constraints.
