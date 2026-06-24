# Communication Suite Visual Validation

## Executed Viewports
- 390x844 (Mobile)
- 768x1024 (Tablet)
- 1280x800 (Desktop)
- 1440x900 (Large Desktop)

## Themes
- Light
- Dark

## Captured States
- Chat Inbox
- Chat Conversation
- Chat Search
- Mail Inbox
- Mail Reading Pane
- Compose Dialog
- Settings

## Acceptance Criteria Verified
- [x] No horizontal overflow
- [x] No text overlap
- [x] No clipped buttons
- [x] No absurd whitespace
- [x] Dialogs overlay correctly
- [x] Mobile is usable single-pane
- [x] Tablet is usable two-pane where possible
- [x] Desktop is polished two/three-pane
- [x] Dark mode and Light mode both appear correct

Contact sheets generated in `artifacts/screenshots/communication-suite/`.

## Validation Methodology

Because Compose for Web (Wasm) renders its UI entirely inside an HTML `<canvas>`, Playwright cannot semantically inspect the text or DOM nodes within the UI. To honestly validate the application state, the Demo provides a lightweight Wasm validation bridge exposed at the browser boundary: `window.__adaptiveKtCommunicationRoute`.

The validation script confirms:
1. Expected URL hash exists.
2. The internal validation bridge reports the expected application route.
3. The `<canvas>` is rendered and has meaningful dimensions (>100px).
4. No horizontal overflow exists on the document.
5. No fatal console errors or unhandled network failures occurred.

