# Ecommerce LayoutInfo Allowlist

The fundamental philosophy of `AdaptiveKt` is that the library measures and provides sensible layout defaults through structural primitives like `AdaptiveContainer`, `AdaptiveGrid`, and `AdaptiveTwoPane`. Developers should not be writing manual `BoxWithConstraints` or breakpoint checks in typical application screens.

However, certain **app-specific structural variants** (like completely swapping navigation hierarchies or fundamentally different semantic layouts) require explicit knowledge of the current adaptive state.

The following usages of `LocalAdaptiveLayoutInfo.current` are explicitly allowed in the `ecommerce-demo` app, as they drive intended structural app variants.

| File Path | Code | Reason Allowed |
|---|---|---|
| `HomeScreen.kt` | `val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current` | Drives the display/hiding of the semantic Hero section image on mobile vs desktop. This is a design decision (marketing banner), not a spacing default. |
| `ProductScreens.kt` | `val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current` | Explicitly decides whether the filter bar is an inline side-column (desktop) or a hidden modal sheet triggered by an Action Bar (mobile). |
| `CartScreens.kt` | `val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current` | Not technically strictly necessary anymore, as `AdaptiveTwoPane` consumes it internally, but acceptable if the screen wishes to conditionally render a modal drawer for Checkout depending on space. |
| `AppShell.kt` | `val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current` | Applies the layout's calculated safe `pagePadding` directly to the `AdaptiveNavigationScaffold` top bar, and controls the display of the desktop-specific universal search bar. |
| `WishlistScreen.kt` | `val layoutInfo = io.github.adaptivekt.core.LocalAdaptiveLayoutInfo.current` | Configures the grid span based on compact mode. |

**No other screens should inject or read `LocalAdaptiveLayoutInfo`.** Use `AdaptivePage` and `AdaptiveTwoPane` directly instead.
