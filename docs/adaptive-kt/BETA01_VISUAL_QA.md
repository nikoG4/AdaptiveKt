# Beta01 visual and interaction QA checklist

This is a manual acceptance sheet for the UI Kit and shipped demos.

## Matrix

| Target | Compact | Medium | Expanded | Light | Dark | Keyboard |
| --- | --- | --- | --- | --- | --- | --- |
| Desktop/JVM | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| Web/Wasm | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| Android | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |
| iOS (macOS runner/device) | [ ] | [ ] | [ ] | [ ] | [ ] | [ ] |

Don't mark cells as passed without captured evidence or a reproducible test.
Retain screenshots, platform details, and the tested commit SHA.

## Scenarios

- [ ] Cards: None, Low, Medium, High and interactive hover focus.
- [ ] Buttons: primary/secondary/ghost/danger, enabled/disabled/hover/press/focus.
- [ ] Tabs and chips: selected/unselected, keyboard switch, theme contrast.
- [ ] Text fields: error, empty, filled, disabled, focus and screen reader label.
- [ ] Checkbox: Off/On/Indeterminate, size and accessible toggle state.
- [ ] Accordion: expand/collapse, chevron, keyboard and reduced motion.
- [ ] Dropdowns/selects: viewport edges, keyboard dismissal, popup stacking.
- [ ] Dialog: narrow widths, background dismiss policy, focus traversal and
      reduced-motion entrance. No exit animation is promised in this phase.
- [ ] All interactive states use a visually perceivable focus affordance.
- [ ] No clipped shadows, horizontal overflow, lost scrolling or blank Wasm canvas.
- [ ] Text contrast guard passes and real rendered text is reviewed.

## Capture workflow

The repository includes Windows tooling for the component gallery:

```powershell
.\tools\prepare-pages-site.ps1
.\tools\capture-component-gallery.ps1 -SkipBuild
```

Use the existing capture tooling once on the reviewed commit, not repeatedly
per minor UI tweak. In CI, draft PRs skip expensive visual/demo jobs. Mark the PR
ready for review only when the source changes have been consolidated and a full
visual validation run is justified.

## Modern / Classic visual comparison

The admin demo defaults to Modern; expanded navigation top bars expose a
Modern/Classic switch. The UI Kit Cards route and main component gallery
render side-by-side independent preview components under the corresponding
AdaptiveTheme. Use these as comparison baselines for both color modes.

- [ ] Modern vs Classic has distinct card shape and button shape.
- [ ] Modern palette uses intended background/surface hierarchy in Light and Dark.
- [ ] Cards/elevated dropdowns cast shadows without clipping against adjacent panels.
- [ ] Primary, secondary and danger states remain readable on hover/press.
- [ ] Input labels, section headers and panel headings have consistent hierarchy.
- [ ] Controls remain reachable on compact layouts (gallery rows scroll horizontally).
- [ ] Theme toggling does not switch to an inconsistent elevation scheme.

Record screenshot hashes and the tested commit SHA; the visual capture job is
advisory, so human inspection remains a gate before merging.

## Automated admin-demo visual evidence

The previous screenshot script covered docs-site component pages, not the real
admin-demo Modern/Classic style comparison. The existing capture workflow now
also runs `capture-beta-ui-kit.js` against the compiled `/demo/app/` route.

The 16 targeted screenshots cover:
- Cards: Modern and Classic, Light and Dark, compact and desktop
- Dashboard: Modern, Light and Dark, compact and desktop
- Open dialog: Modern, Light and Dark, compact and desktop

Each screenshot uses explicit URL parameters `screen`, `theme`, and `style`.
The capture saves PNGs and a JSON/Markdown manifest under
`artifacts/screenshots/component-gallery/beta-ui-kit/`.

A nonblank canvas and browser-runtime error checks are automatic, but screenshots
still need a person to inspect visual consistency. Only a successful reviewed
capture is evidence for the Web/Wasm visual QA gate.
