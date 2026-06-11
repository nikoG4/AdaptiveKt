# Component Gallery Visual Validation

Date: 2026-06-10

## Route

- `/components/`

## Capture Tooling

- PowerShell: `tools/capture-component-gallery.ps1`
- Playwright runner: `tools/docs-site-capture/capture-component-gallery.js`
- Output: `artifacts/screenshots/component-gallery/`

## Viewports

- Mobile: `390x844`
- Tablet: `768x1024`
- Desktop: `1280x800`
- Large: `1440x900`

## Themes

- Light
- Dark

## Required Output Files

- `component-gallery-mobile-light.png`
- `component-gallery-mobile-dark.png`
- `component-gallery-tablet-light.png`
- `component-gallery-tablet-dark.png`
- `component-gallery-desktop-light.png`
- `component-gallery-desktop-dark.png`
- `component-gallery-large-light.png`
- `component-gallery-large-dark.png`
- `contact-sheet-component-gallery.png`
- `capture-report.md`

## Acceptance

The capture fails on console errors, network failures, horizontal overflow, missing canvas, or blank route. Manual review should verify obvious contrast issues in dark mode and component state coverage.

