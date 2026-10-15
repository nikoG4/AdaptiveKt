# 0.2.0-beta01 release gates

Supporting documents: [compatibility proposal](BETA01_COMPATIBILITY.md),
[visual QA matrix](BETA01_VISUAL_QA.md), and
[release runbook](BETA01_RELEASE_RUNBOOK.md).

This is a verification checklist, not a statement that AdaptiveKt is beta-ready.
Prefer hardening existing public APIs over expanding the component catalog.

## API contract
- [ ] Review public naming, slots, defaults, overloads, and module boundaries.
- [ ] Publish a beta compatibility/migration policy and KDoc for core API usage.

## Interaction and accessibility
- [ ] Audit focus, keyboard, touch and semantics in selects, menus, dialogs, forms and navigation.
- [ ] Verify reduced-motion behavior for each migrated animation.

## Platform support
- [ ] Validate consumer builds and rendering on Desktop/JVM, Android and Web/Wasm.
- [ ] Validate iOS publication AND native app rendering on macOS before claiming support.
- [ ] Document known unsupported platform functionality.

## Regression and docs
- [ ] Review light/dark screenshot baselines at compact, medium and expanded sizes.
- [ ] Graduate critical interaction/visual checks to release blockers.
- [ ] Check install docs, end-to-end examples and common customizations.

## Release
- [ ] One clean CI validation of the beta commit and a clean consumer smoke test.
- [ ] Verify signed artifacts published on Maven Central.
- [ ] Tag the beta with changelog and migration notes.

## Minimal Actions policy

Accumulate related changes before pushing. Prefer static/local verification,
run CI once for a cohesive PR, inspect failed jobs before any rerun, and avoid
manually triggering publishing or Pages pipelines during development.

Existing infrastructure: published alpha, seven public modules, several demos,
documentation site, release workflow, and CI. This milestone starts with visual
elevation/motion tokens; none of the remaining gates are marked as verified yet.

## Implemented source work awaiting build + visual verification

- [x] Define theme-driven elevation and motion values.
- [x] Apply visual states to buttons, chips, tabs, checkboxes, inputs, cards and overlays.
- [x] Add keyboard roles and selected semantics for representative controls.
- [x] Show elevated surface variants in admin-demo UI Kit and label its icon controls.
- [x] Pass explicit theme mode with the demo's manual dark/light toggle.
- [ ] Confirm compilation and smoke test behavior on actual supported targets.
- [ ] Check dialog stacking, keyboard traversal, and screen reader behavior.
- [ ] Capture visual baselines before declaring the interaction pass complete.

## CI infrastructure follow-up

The first draft-PR CI attempt failed before compilation because the previous
Android setup action tried to install the removed SDK `tools` package.
The workflow now uses android-actions/setup-android@v4, which avoids the
removed package. Build validation remains **pending** until a new CI run passes.
