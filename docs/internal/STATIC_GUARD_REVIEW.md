# Static Guard Review

This document audits the static checks ensuring developers adhere to the "Library measures, developer configures" philosophy.

## Existing Guards

1. **`LayoutGuardTest.kt`**
   - Automatically runs during `./gradlew :adaptive-layout:jvmTest`.
   - Protects `ecommerce-demo`.
   - Checks for `BoxWithConstraints` and `breakpointForWidth`.
   - Allows `LocalAdaptiveLayoutInfo.current` strictly via an allowlist restricted to the root app shells.

2. **`check-ai-workspace-layout-guards.ps1`**
   - Script invoked by `verify-ai-workspace.ps1`.
   - Protects `ai-workspace-demo`.
   - Checks for `BoxWithConstraints` and `LocalAdaptiveLayoutInfo.current` (0 exceptions).

## Enhancements Applied

- **Material Icons Prevention:** Updated `check-ai-workspace-layout-guards.ps1` to reject `Icons.Default` and `material.icons` to enforce our strict < 12MB Wasm bundle size limits (since importing `material-icons-extended` blows up the binary).
- **Emoji Prevention:** Updated the guard to reject emoji surrogates (`🏠`, `➡️`, `➕`) in favor of SVG paths inside `AiGlyph.kt`.

## Git Tracking Defenses
- `.gitignore` successfully masks `tmp-gh-pages-root/` and `site-dist/`, preventing transient CI artifacts from entering source control.

## Conclusion
The repository has robust CI-enforced gates preventing feature creep, bloated icon sets, and manual layout measurement regressions.
