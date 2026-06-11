# AdaptiveDialog Modal Recovery

Date: 2026-06-11

## Original Issue

`AdaptiveDialog` behaved like embedded page content. When opened, it appeared inside the normal layout flow and pushed page content downward instead of floating above it as a modal.

## Root Cause

The previous implementation used an inline `Box` with `fillMaxSize()` at the call site. That is visually similar to an overlay, but it is still a child in the current page layout, so it participates in measurement and placement.

## Selected Implementation

`AdaptiveDialog` now uses Compose Multiplatform `Dialog`.

The dialog content is rendered inside a modal overlay:

- tokenized scrim from `AdaptiveTheme.colors.overlay`;
- centered adaptive surface;
- max width from `AdaptiveOverlayDefaults.DialogMaxWidth`;
- max height with internal scrolling;
- compact viewport margin;
- source-compatible parameters plus new optional behavior controls.

## Dismissal Behavior

- `onDismissRequest` is called for platform dismiss requests.
- Backdrop click dismissal is controlled by `dismissOnBackdropClick`.
- Desktop/Wasm ESC and Android back are delegated to Compose `Dialog` via `dismissOnBackPress = true`.
- The dialog surface consumes its own clicks so backdrop dismissal only happens outside the surface.

## Selection Behavior

Dialog body text can opt into selection through `contentSelectionEnabled = true`.

Controls in the footer remain outside that selection wrapper so buttons stay click-first.

## Platform Behavior

| Platform | Behavior |
|---|---|
| JVM/Desktop | Modal overlay with scrim; no page layout shift. |
| Wasm | Modal overlay through Compose browser dialog implementation; validated with Playwright capture. |
| Android | Uses Compose dialog semantics and back dismiss behavior. |
| iOS | Uses common Compose dialog API; final native behavior should be validated on macOS/iOS CI. |

## Known Limitations

- Focus trapping is delegated to Compose Dialog. AdaptiveKt does not implement a custom focus manager in this pass.
- A full overlay stack manager is intentionally out of scope.
- Inline `AdaptiveDropdownMenu` remains inline for compatibility; overlay dropdown behavior belongs to `AdaptiveAnchoredDropdownMenu`.

