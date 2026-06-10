# Changelog

## Unreleased

### Added
- `AdaptiveCollectionView` for generic list/grid/card collections.
- Shared `AdaptiveQueryState`, filter, sort and pagination controls for data and collection views.
- Explicit `AdaptiveDataDisplayMode` and `AdaptiveCollectionDisplayMode` resolvers.
- Configurable responsive navigation behavior with placements for sidebar, rail, bottom bar, drawer and hidden custom navigation.
- `AdaptiveThemeMode` with System, Light and Dark modes; `AdaptiveTheme {}` now follows the platform color scheme by default when no explicit color scheme is provided.
- Storefront navigation preset for demos and ecommerce-style apps.
- Grant readiness documentation, repository community files, GitHub issue templates, and pull request template.
- README installation guidance for the published Maven Central alpha.

### Changed
- Ecommerce product listing now dogfoods `AdaptiveCollectionView` with search, category filters, sorting and pagination.
- Improved ecommerce showcase navigation, theme handling and product visuals.
- Repositioned README around AdaptiveKt's alpha Maven Central publication and grant-readiness roadmap.

## 0.1.0-alpha01

### Added
- AdaptiveTheme foundation.
- Light and dark color schemes.
- Platform theme presets and PlatformDefault.
- AdaptiveSelect and AdaptiveMultiSelect.
- AdaptiveCarousel with animated slide/fade/scale transitions.
- AdaptiveNavigationTree.
- Animated feedback loading indicators.
- Navigation item style and density defaults for pill, card and minimal sidebar rows.
- AdaptiveKt SVG brand assets and docs-site logo integration.
- Rebuilt docs-site experience with a product home page, rendered docs pages, component examples, copyable code blocks, API tables, and responsive docs navigation.
- Responsive layout, forms, data, navigation and feedback modules.
- Docs-site and admin demo.
- Visual verification tooling.
- Local Maven publishing dry-run configuration for library modules.
- Maven Central publication for library modules under `io.github.nikog4.adaptivekt`.

### Changed
- Prepared conditional Gradle signing support for future Maven Central release workflows.
- Added a guarded manual `publish-release.yml` workflow for release preparation.

### Fixed
- Pages local build now uses a local `/` base path while GitHub Pages injects `/AdaptiveKt/`.
- Docs-site, admin demo and AI Workspace no longer render blank Wasm canvases in the public Pages bundle.
- Ecommerce cart compact layout no longer squeezes product text into a one-character column.
- Mobile Wasm admin-demo content now scrolls normally on compact/touch viewports.
- Default sidebar navigation no longer renders primary items as large card rows.
