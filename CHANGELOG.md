# Changelog

## Unreleased

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

### Changed
- Prepared conditional Gradle signing support for future Maven Central release workflows.
- Added a manual `publish-release.yml` workflow for safe manual publish preparation without remote publishing.

### Fixed
- Mobile Wasm admin-demo content now scrolls normally on compact/touch viewports.
- Default sidebar navigation no longer renders primary items as large card rows.
