# Layout API Audit

| API | Module | Purpose | Defaults | Override Support | Uses LocalAdaptiveLayoutInfo? | App Dev Manual Measurement? | Status | Notes |
|---|---|---|---|---|---|---|---|---|
| `AdaptiveApp` | `adaptive-core` | Root configuration provider | Sane library defaults | Yes (via `config`) | N/A | No | Stable | Clear naming |
| `AdaptiveConfig` | `adaptive-core` | Data class for config | Sensible breakpoints/layout info | Yes | N/A | No | Stable | Good defaults |
| `AdaptiveBreakpoints` | `adaptive-core` | Threshold definitions | compact: 600, medium: 840, expanded: 1200 | Yes | N/A | No | Stable | |
| `AdaptiveLayoutInfo` | `adaptive-core` | Resolved adaptive state | Evaluated from screen width | Yes | N/A | No | Stable | |
| `LocalAdaptiveLayoutInfo` | `adaptive-core` | CompositionLocal for info | `error("Not provided")` | Yes | N/A | Allowed for specific variants | Stable | Should not be used for generic layout |
| `AdaptiveContainer` | `adaptive-layout` | Centers content with max width | Derived from `LocalAdaptiveLayoutInfo` | Yes (`maxWidth`, `contentPadding`) | Yes | No | Stable | Content max width correctly handled |
| `AdaptiveGrid` | `adaptive-layout` | Responsive LazyVerticalGrid | Columns derived from breakpoint | Yes (`columns`) | Yes | No | Stable | `columns` is nullable, falling back to layout policy |
| `AdaptivePage` | `adaptive-layout` | Base page shell with padding | Padding derived from breakpoint | Yes (`contentPadding`) | Yes | No | Stable | Flexible foundation |
| `AdaptiveColumnPage` | `adaptive-layout` | Vertically stacked page | SpacedBy derived from breakpoint | Yes | Yes | No | Stable | Perfect for non-scrollable or fixed layouts |
| `AdaptiveScrollablePage` | `adaptive-layout` | Scrolling vertical page | Inherits `AdaptiveColumnPage` defaults | Yes | Yes | No | Stable | Core primitive for dashboard screens |
| `AdaptiveSection` | `adaptive-layout` | Formatted grouping | Title styling and spacing | Yes | Yes | No | Stable | Eliminates raw Column usage |
| `AdaptiveActionBar` | `adaptive-layout` | Top/Action bar structure | Adapts arrangement to width | Yes | Yes | No | Stable | Handles filter rows nicely |
| `AdaptiveTwoPane` | `adaptive-layout` | Side-by-side or stacked view | Stacks on compact, rows otherwise | Yes (`weights`, `collapse`) | Yes | No | Stable | Greatly reduces boilerplate in Cart/Detail screens |
| `AdaptiveNavigationScaffold` | `adaptive-navigation`| Responsive nav placement | BottomBar (Compact) / Rail (Med) / Drawer (Exp) | Yes | Yes | No | Stable | |
