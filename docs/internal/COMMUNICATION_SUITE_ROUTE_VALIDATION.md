# Communication Suite Route Validation

## Validated Routes
- `/examples/communication-suite/`
- `/examples/communication-suite/#/chat`
- `/examples/communication-suite/#/chat/inbox`
- `/examples/communication-suite/#/chat/conversation/team-alpha`
- `/examples/communication-suite/#/chat/conversation/support-desk`
- `/examples/communication-suite/#/chat/search`
- `/examples/communication-suite/#/mail`
- `/examples/communication-suite/#/mail/inbox`
- `/examples/communication-suite/#/mail/thread/product-launch`
- `/examples/communication-suite/#/mail/thread/security-review`
- `/examples/communication-suite/#/mail/compose`
- `/examples/communication-suite/#/settings`

## Acceptance Criteria Verified
- [x] Route loads
- [x] Expected screen visible
- [x] Canvas/content visible
- [x] No fatal console errors
- [x] No required network failures
- [x] No blank page
- [x] Null response correctly handled for hash navigation (Playwright fixes)

Reports generated in `artifacts/route-validation/communication-suite/`.

## Validation Methodology

Because Compose for Web (Wasm) renders its UI entirely inside an HTML `<canvas>`, Playwright cannot semantically inspect the text or DOM nodes within the UI. To honestly validate the application state, the Demo provides a lightweight Wasm validation bridge exposed at the browser boundary: `window.__adaptiveKtCommunicationRoute`.

The validation script confirms:
1. Expected URL hash exists.
2. The internal validation bridge reports the expected application route.
3. The `<canvas>` is rendered and has meaningful dimensions (>100px).
4. No horizontal overflow exists on the document.
5. No fatal console errors or unhandled network failures occurred.

