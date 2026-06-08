# Public API Inventory

This document tracks all new and modified public APIs introduced in the `AdaptiveKt` hardening and layout primitives updates.

## 1. `adaptive-core`

### Config & Environment
| API | Package | Purpose | Intended For |
|---|---|---|---|
| `AdaptiveApp` | `io.github.adaptivekt.core` | Root layout bounds measurer and composition provider. | App Developers |
| `AdaptiveConfig` | `io.github.adaptivekt.core` | Root configuration struct holding policy delegates. | App Developers |
| `LocalAdaptiveConfig` | `io.github.adaptivekt.core` | CompositionLocal for `AdaptiveConfig`. | Internal / Advanced |
| `AdaptiveLayoutInfo` | `io.github.adaptivekt.core` | Resolved viewport data (isCompact, width, breakpoint). | App Developers |
| `LocalAdaptiveLayoutInfo` | `io.github.adaptivekt.core` | CompositionLocal for `AdaptiveLayoutInfo`. | Internal / App Developers |

### Policies & Breakpoints
| API | Package | Purpose | Intended For |
|---|---|---|---|
| `AdaptiveBreakpoints` | `io.github.adaptivekt.core` | Defines physical Dp boundaries for Compact/Medium/Expanded/Large. | App Developers |
| `AdaptiveLayoutPolicy` | `io.github.adaptivekt.core` | Defines Grid column counts and max width defaults. | App Developers |
| `AdaptiveNavigationPolicy` | `io.github.adaptivekt.core` | Maps breakpoints to `AdaptiveNavigationMode` (BottomBar, Rail, Drawer). | App Developers |
| `AdaptiveSpacingPolicy` | `io.github.adaptivekt.core` | Maps breakpoints to standard page padding values. | App Developers |
| `AdaptiveResponsive` | `io.github.adaptivekt.core` | Generic struct to hold distinct values per breakpoint (e.g. `AdaptiveResponsive<Dp>`). | App Developers |

## 2. `adaptive-layout`

### Core Primitives
| API | Package | Purpose | Intended For |
|---|---|---|---|
| `AdaptivePage` | `io.github.adaptivekt.layout` | Applies padding to generic content without scroll. | App Developers |
| `AdaptiveColumnPage` | `io.github.adaptivekt.layout` | Convenience wrapper for `Column` inside an `AdaptivePage`. | App Developers |
| `AdaptiveScrollablePage` | `io.github.adaptivekt.layout` | Wraps content in a vertical scroll container with correct paddings. | App Developers |
| `AdaptiveSection` | `io.github.adaptivekt.layout` | Defines headers, descriptions, and action regions for bounded content. | App Developers |
| `AdaptiveActionBar` | `io.github.adaptivekt.layout` | Bottom/Top action bar wrapping strategy based on viewport width. | App Developers |
| `AdaptiveTwoPane` | `io.github.adaptivekt.layout` | Fundamental structural view managing a primary/secondary pane split. | App Developers |
| `AdaptiveContainer` | `io.github.adaptivekt.layout` | Limits width of content to `contentMaxWidth`. | App Developers |
| `AdaptiveGrid` | `io.github.adaptivekt.layout` | Auto-scaling lazy grid based on `AdaptiveLayoutPolicy.columnsFor`. | App Developers |

### List-Detail Pattern
| API | Package | Purpose | Intended For |
|---|---|---|---|
| `AdaptiveListDetailScaffold` | `io.github.adaptivekt.layout` | High-level pattern converting List+Detail into panes or stacked nav based on selection. | App Developers |
| `AdaptivePaneSpec` | `io.github.adaptivekt.layout` | Weight and width constraints for a single pane. | App Developers |
| `AdaptiveListDetailBehavior` | `io.github.adaptivekt.layout` | Policy mapping breakpoints to List/Detail Pane modes. | App Developers |
| `AdaptiveListDetailCompactBehavior` | `io.github.adaptivekt.layout` | Defines what to show on Compact viewports (List until selected, Always List, Always Detail). | App Developers |
| `AdaptiveListDetailPaneMode` | `io.github.adaptivekt.layout` | Configuration enum (ListOnly, DetailOnly, ListAndDetail). | App Developers |
| `AdaptiveListDetailResolvedMode` | `io.github.adaptivekt.layout` | The runtime evaluated state of the panes. | Internal |
| `resolveAdaptiveListDetailMode` | `io.github.adaptivekt.layout` | Core evaluation function for the ListDetail behavior. | Internal |

## 3. `adaptive-navigation`

| API | Package | Purpose | Intended For |
|---|---|---|---|
| `AdaptiveNavigationMode` | `io.github.adaptivekt.navigation` | Enum indicating UI navigation state. (Moved to `adaptive-core` but retains package to preserve backwards compatibility). | App Developers |
| `AdaptiveNavigationScaffold` | `io.github.adaptivekt.navigation` | Main scaffolding component modified to consume `AdaptiveNavigationMode` from `LocalAdaptiveLayoutInfo`. | App Developers |

## Summary
All newly introduced APIs successfully avoid `BoxWithConstraints` entirely, relying on the ambient `LocalAdaptiveLayoutInfo` calculated once at the `AdaptiveApp` level.
