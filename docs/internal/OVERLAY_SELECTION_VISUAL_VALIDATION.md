# Overlay Selection Visual Validation

Date: 2026-06-11

This document is generated/updated by `tools/capture-overlay-selection-gallery.ps1`.

## Validation Scope

Routes:

- `/components/`

States:

- selectable text gallery;
- centered dialog open;
- long selectable dialog open;
- destructive dialog open.

Viewports:

- `390x844`
- `768x1024`
- `1280x800`
- `1440x900`

Themes:

- light;
- dark.

## Expected Results

- Dialogs render above the page.
- Opening a dialog does not shift the component page underneath.
- Scrim is visible and tokenized.
- Long dialog content scrolls inside the modal surface.
- Selectable text examples are visible, while controls remain click-first.
- No horizontal overflow, console errors or failed network requests.

## Screenshot Output

Screenshots are written to:

`artifacts/screenshots/overlay-selection-gallery/`

The capture report is written to:

`artifacts/screenshots/overlay-selection-gallery/capture-report.md`

Generated screenshots are intentionally ignored artifacts and are not committed.

## Latest Local Result

Command:

```powershell
.\tools\capture-overlay-selection-gallery.ps1 -SkipBuild
```

Result:

- 32 screenshots generated.
- 4 viewports x 2 themes x 4 states.
- Console errors: 0.
- Network failures: 0.
- Horizontal overflow: no.
- Dialog opened: true for all dialog states.
- Layout shift: no for all dialog states.

Contact sheet:

`artifacts/screenshots/overlay-selection-gallery/contact-sheet-overlay-selection-gallery.png`

Report:

`artifacts/screenshots/overlay-selection-gallery/capture-report.md`
