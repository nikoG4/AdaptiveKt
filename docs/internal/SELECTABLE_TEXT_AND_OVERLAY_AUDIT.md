# Selectable Text And Overlay Audit

Date: 2026-06-11

## Scope

This audit covers text-selection and overlay behavior in:

- `adaptive-components`
- `adaptive-data`
- docs-site component gallery
- AI Workspace demo
- Ecommerce demo

The audit deliberately avoids changing `adaptive-navigation` because an Android back-navigation branch may be in flight.

## Current Selection State

Before this pass AdaptiveKt did not expose a library-level selectable text primitive. Components and demos mostly rendered text through `BasicText` / `Text` directly, which meant selection support was inconsistent and app authors had no first-class AdaptiveKt API for enabling it.

Compose's `SelectionContainer` is available from common source sets in the current Kotlin/Compose toolchain. It compiles for JVM and Wasm in this repo.

## Selection Policy

Text selection remains opt-in. Making every label selectable would degrade interaction in dense controls.

Selection should be enabled for:

- documentation paragraphs and code-like blocks;
- card descriptions when the card itself is not clickable;
- AI chat message bodies and code snippets;
- prompt details, notes and variables;
- product detail descriptions and specifications;
- IDs, emails, logs and long user-readable values.

Selection should remain disabled for:

- `AdaptiveButton` labels;
- `AdaptiveIconButton` labels;
- tabs, chips, badges and navigation labels when interactive;
- dropdown and select menu choices;
- clickable cards/list rows;
- drag/click-heavy surfaces.

## API Added

`AdaptiveSelectionArea(enabled, content)` wraps content in a Compose selection container only when explicitly requested.

`AdaptiveCard` now accepts `contentSelectionEnabled`, applied only when the card is static. Clickable cards keep selection disabled to avoid fighting click and hover behavior.

`AdaptiveDialog` now accepts `contentSelectionEnabled` for body content.

## Overlay Audit

### True Overlay Components

| Component | Current mechanism | Notes |
|---|---|---|
| `AdaptiveAnchoredDropdownMenu` | `Popup` | Anchored menu renders outside normal layout flow. |
| `AdaptiveDataView` overflow menu | `Popup` | Action menu floats above table/card content. |
| `AdaptiveDialog` | `Dialog` after this pass | Modal overlay with scrim, no layout shift. |

### Layout-Flow Components

| Component | Behavior | Status |
|---|---|---|
| `AdaptiveDropdownMenu` | Inline menu panel | Kept as a simple inline primitive for existing compatibility. Use anchored dropdown for overlays. |
| Previous `AdaptiveDialog` | Inline full-size `Box` | Replaced; this caused page content to shift. |

## AdaptiveDialog Root Cause

`AdaptiveDialog` previously rendered a `Box(modifier.fillMaxSize())` directly where the app called it. Because it was part of the normal composition layout tree, opening it inserted a large child into the page and pushed nearby content down.

The fix is to render through Compose Multiplatform `Dialog`, with a tokenized scrim and centered modal surface.

## Platform Risks

| Platform | Risk | Mitigation |
|---|---|---|
| Desktop/JVM | Dialog must not shift layout and should close on ESC/back request. | Compose `Dialog` handles dismiss requests; visual capture validates layout shift. |
| Wasm | Dialog behavior depends on Compose browser overlay support. | Wasm compile and browser screenshots validate overlay visibility and no layout shift. |
| Android | Back dismissal should use Compose dialog semantics. | `dismissOnBackPress = true`; no `androidx.activity` commonMain dependency. |
| iOS | Native dialog semantics may differ. | Common API remains source-compatible; macOS/iOS validation remains CI/manual. |

## Fix Summary

- Added `AdaptiveSelectionArea`.
- Added `contentSelectionEnabled` to `AdaptiveCard`.
- Converted `AdaptiveDialog` from inline layout-flow content to a modal `Dialog`.
- Added `AdaptiveOverlayDefaults` for modal sizing and scrim policy.
- Updated docs-site, AI Workspace and Ecommerce where selection is useful.

