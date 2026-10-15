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
