# Ecommerce Shell Recovery

## Root Issues

- Product grids used `AdaptiveGrid` spans as if `span = 1` meant one card per available slot. On expanded layouts this produced skinny 1/12-width strips.
- Compact topbar embedded a text field in a fixed-height header, making controls cramped.
- Cart items used one desktop row layout on compact screens, causing product text to collapse vertically.

## Fixes

- Storefront topbar now keeps compact mode simple: logo plus a Search action.
- Home and product grids now compute practical column counts from `LocalAdaptiveLayoutInfo`.
- Product listing now uses `AdaptiveCollectionView` with shared search, category chips, sort, page size and pagination controls.
- Cart item layout switches to a compact vertical/flowing layout.
- Admin-demo and docs-site are wrapped in `AdaptiveApp` so adaptive layout locals exist at runtime.
- AI Workspace Wasm mounts into `#webApp`, preventing offscreen canvas rendering.

## Visual Outcome

- Ecommerce home buttons no longer truncate.
- Product cards are readable in mobile, tablet, desktop and large viewports.
- Cart compact view no longer squeezes text into a one-character column.
- Pages routes render visible Compose canvases without fatal console errors.
