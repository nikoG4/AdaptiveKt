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

## Keyboard/IME verification for Select and MultiSelect

- [ ] Enter/Space/Down open a focused collapsed trigger exactly once.
- [ ] KeyUp does not toggle the popup or selected option.
- [ ] Up/Down/Home/End navigate highlighted enabled options without selecting them.
- [ ] Disabled, filtered-out and dynamically replaced options cannot be selected.
- [ ] Search text accepts spaces; Home/End move cursor inside focused text field.
- [ ] Enter selects (or toggles in MultiSelect) exactly once.
- [ ] Escape closes and restores focus to the trigger.
- [ ] The popup and option roles are announced with correct selection state.
- [ ] Touch and IME behavior remain normal on Android and iOS.

See [interaction audit](BETA01_INTERACTION_AUDIT.md) for implementation details
and an explicit list of what is not yet verified.

## Tabs interaction checks

- [ ] LTR Right/Left and RTL Right/Left move the correct direction.
- [ ] Home/End focus and select the respective edge tabs.
- [ ] The selected tab is scrolled into view when needed.
- [ ] KeyUp does not cause an additional selection.

## Premium dashboard visual QA (separate stacked PR)

- [ ] At 1280 px viewport the Modern dashboard shows four KPI cards per row.
- [ ] At medium width it shows two KPI cards, compact shows one per row.
- [ ] Modern shows welcome banner, switchable revenue chart, activities, invoices
  and inventory, while Classic retains its earlier compact layout.
- [ ] 7/30/90 day period buttons update both bar series and monetary total.
- [ ] Report button opens a functional modal; close, outside dismiss and Escape work.
- [ ] Modern/Classic dashboard comparisons are available at 390 and 1280 px,
  light and dark; screenshot manifest contains 20 scenes in total.
- [ ] No clipped/shrunk chart labels, overflowing chips or cut-off actions.
- [ ] Dialog surfaces do not appear as fake actionable buttons to screen readers.
- [ ] CTA text contrast is acceptable across palettes and disabled states.

## Component routing smoke-test hardening

The previous component route validation incorrectly looked for DOM text from
Compose/Wasm canvas rendering, which produced 22 false failures. The browser
now checks the actual Compose-resolved component id and title (published in
page metadata after composition), that the canvas exists, and that the URL
hasn't fallen back to another hash. Route validation is a blocking CI step.

This does **not** prove that the component title was visibly painted or that
overlay interactions work. Keep screenshot inspection and manual interaction
checks separate from these route state smoke tests.
