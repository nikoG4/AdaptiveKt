# Visual Primitives Remaining Audit

PR C6 inventories duplicated or local visual primitives that remain after the controlled migrations. This document does not migrate them; it only classifies future work.

| Helper | Module | File | Why It Stays Local | Migrate Later? | Requires New Component? |
|---|---|---|---|---|---|
| `DemoThumbnail` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt` | PR C7 changed this to a thin wrapper around `AdaptiveThumbnail` to preserve old demo call sites. | Candidate elimination | No |
| `DemoRemoteThumbnail` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoRemoteImages.kt` | Demo-only remote image loader wrapper around Kamel. PR C7 uses `AdaptiveThumbnail` as its fallback. | No, keep demo-only | No |
| `DemoRemoteAvatar` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoRemoteImages.kt` | Demo-only remote image loading; falls back to `AdaptiveAvatar`. | No, keep demo-only | No |
| `DemoToggleChip` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt` | PR C7 changed this to a thin wrapper around `AdaptiveChip` to preserve old demo call sites. | Candidate elimination | Maybe segmented control later |
| `DemoStatusText` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt` | Used for compact status text where badge styling would be too heavy. | Maybe | Text/tone helper |
| `DemoBadge` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt` | Delegates to `AdaptiveBadge`, keeping old demo call sites small. | Candidate elimination | No |
| `DemoCard` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt` | KPI-specific composite built on `AdaptiveCard`. | Keep as demo composite | No |
| `DemoPanel` | `:admin-demo` | `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt` | Demo-specific panel composite built on `AdaptiveCard`. | Keep as demo composite | No |
| `DefaultStatusBadge` | `:adaptive-data` | `adaptive-data/src/commonMain/kotlin/io/github/adaptivekt/data/AdaptiveDataView.kt` | Handles generated status cells where column content can be custom composable. `AdaptiveBadge` is text-based. | Yes | Badge/content-slot strategy |
| `DefaultOverflowMenu` | `:adaptive-data` | `adaptive-data/src/commonMain/kotlin/io/github/adaptivekt/data/AdaptiveDataView.kt` | Current menu is anchored as a popup; `AdaptiveDropdownMenu` is inline/simple. | Yes | Anchored dropdown/menu API |
| Data table surface constants | `:adaptive-data` | `adaptive-data/src/commonMain/kotlin/io/github/adaptivekt/data/AdaptiveDataView.kt` | DataView table/card layout still owns table-specific colors and spacing. | Yes | Table tokens |
| `SimpleLoadingIndicator` | `:adaptive-feedback` | `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/LoadingState.kt` | Small Foundation-only indicator; no shared progress primitive exists. | Maybe | `AdaptiveProgress` / spinner |
| `SimpleText` | `:adaptive-feedback` | `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/EmptyState.kt` | Local text helper until typography tokens/components exist. | Maybe | Typography/text primitive |
| Form labels and validation visuals | `:adaptive-forms` | `adaptive-forms/src/commonMain/kotlin/io/github/adaptivekt/forms/AdaptiveFormLayout.kt` | Fields/actions are user slots; validation has no shared primitive yet. | Yes | `AdaptiveValidationMessage` |
| Navigation item rendering | `:adaptive-navigation` | `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/NavigationSurfaces.kt` | Sidebar, rail, drawer, and bottom nav have layout-specific selected states and width constraints. | Maybe | Navigation tokens, not generic cards |
| Navigation surfaces | `:adaptive-navigation` | `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/NavigationSurfaces.kt` | `AdaptiveSurface` padding/border behavior would risk layout regressions. | Maybe | Navigation-specific surface tokens |

## Summary

Most remaining local primitives are intentionally feature-specific. The next reusable components worth designing are:

- Anchored `AdaptiveDropdownMenu`
- `AdaptiveValidationMessage`
- Table/navigation/media tokens

None of these were implemented in PR C6.

PR C7 implemented `AdaptiveThumbnail` and `AdaptiveChip`. Remaining work is wrapper cleanup, direct adoption in more call sites, and deciding whether a separate segmented control is needed.
