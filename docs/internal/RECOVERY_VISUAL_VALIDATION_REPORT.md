# Recovery Visual Validation Report

Generated with `tools/recovery-pages-audit.mjs` against `http://localhost:8080`.

Screenshots are stored under `artifacts/screenshots/recovery-validation/` and are not intended to be committed.

## Summary

- Route/viewport combinations audited: 56.
- Passed: 56.
- Failed: 0.
- Viewports: 390x844, 768x1024, 1280x800, 1440x900.
- Fatal console/resource checks: no `WebAssembly.Exception`, missing Wasm/JS, missing compose resources, MIME errors, or failed required network requests.

## Root And Docs

- `artifacts/screenshots/recovery-validation/mobile-root.png`
- `artifacts/screenshots/recovery-validation/tablet-root.png`
- `artifacts/screenshots/recovery-validation/desktop-root.png`
- `artifacts/screenshots/recovery-validation/large-root.png`
- `artifacts/screenshots/recovery-validation/large-docs.png`
- `artifacts/screenshots/recovery-validation/large-components.png`
- `artifacts/screenshots/recovery-validation/large-demo.png`

Result: visible, nonblank docs UI with no fatal console errors.

## Admin Demo

- `artifacts/screenshots/recovery-validation/mobile-admin-demo.png`
- `artifacts/screenshots/recovery-validation/tablet-admin-demo.png`
- `artifacts/screenshots/recovery-validation/desktop-admin-demo.png`
- `artifacts/screenshots/recovery-validation/large-admin-demo.png`

Result: visible dashboard after adding the missing `AdaptiveApp` root wrapper.

## Ecommerce

- `artifacts/screenshots/recovery-validation/mobile-ecommerce-home.png`
- `artifacts/screenshots/recovery-validation/mobile-ecommerce-shop.png`
- `artifacts/screenshots/recovery-validation/mobile-ecommerce-cart.png`
- `artifacts/screenshots/recovery-validation/mobile-ecommerce-checkout.png`
- `artifacts/screenshots/recovery-validation/mobile-ecommerce-login.png`
- `artifacts/screenshots/recovery-validation/large-ecommerce-home.png`
- `artifacts/screenshots/recovery-validation/large-ecommerce-shop.png`
- `artifacts/screenshots/recovery-validation/large-ecommerce-cart.png`
- `artifacts/screenshots/recovery-validation/large-ecommerce-checkout.png`
- `artifacts/screenshots/recovery-validation/large-ecommerce-login.png`

Result: storefront renders with readable product cards, usable collection controls, bottom navigation on compact, and no blank routes.

## AI Workspace

- `artifacts/screenshots/recovery-validation/mobile-ai-workspace-home.png`
- `artifacts/screenshots/recovery-validation/mobile-ai-workspace-chats.png`
- `artifacts/screenshots/recovery-validation/mobile-ai-workspace-prompts.png`
- `artifacts/screenshots/recovery-validation/mobile-ai-workspace-settings.png`
- `artifacts/screenshots/recovery-validation/large-ai-workspace-home.png`
- `artifacts/screenshots/recovery-validation/large-ai-workspace-chats.png`
- `artifacts/screenshots/recovery-validation/large-ai-workspace-prompts.png`
- `artifacts/screenshots/recovery-validation/large-ai-workspace-settings.png`

Result: dashboard and sub-routes render in the visible viewport after mounting Compose into `#webApp`.

## Auditor Hardening

The Playwright audit now checks:

- page errors as fatal;
- visible canvas count, not just canvas existence;
- failed network requests and 4xx responses;
- generated script/resource paths.
