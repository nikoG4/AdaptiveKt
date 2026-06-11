# Docs Site Component Gallery Audit

## Overview
This audit assesses the AdaptiveKt component gallery across multiple routes, viewports, and themes. It highlights known layout and regression issues based on the current implementation in `DocsUi.kt` and `SiteComponentsPage.kt`.

## Findings

| Route Tested | Viewport | Theme | Console | Network | Visual Status | Suspected Issue | Confirmed Issue | Fix / TODO |
|--------------|----------|-------|---------|---------|---------------|-----------------|-----------------|------------|
| `/components/#adaptive-theme` | 390x844 (Mobile) | Light | OK | OK | Fail | Overlap / Overflow | `AdaptiveCard` children overlap when selection is enabled without safe wrappers | Wrapped `ComponentDocArticle` internal content with safe layout column in PR #11 |
| `/components/#adaptive-dialog` | Desktop | Dark | OK | OK | Fail | Overlap | Same as above | Wrapped `AdaptiveDialog` body with safe layout column in PR #11 |
| `/components/#adaptive-selectionarea` | Tablet | Light | OK | OK | Fail | Overlap | Same as above | Fixed in PR #11 |
| `All /components/ routes` | Desktop | Light/Dark | OK | OK | Risk | Code Block horizontal overflow | Long Kotlin code inside `DocsCodeBlock` may stretch containers on narrow viewports | Ensure horizontal scroll container does not dictate parent width |
| `All /components/ routes` | Desktop | Light/Dark | OK | OK | Risk | Parameter Table squished | `DocsParameterTable` description column on medium viewports compresses to unreadable width | Switch table to card-layout at a higher breakpoint or allow horizontal scroll |
| `All /components/ routes` | Mobile | Light | OK | OK | Pass | Left navigation changes to compact | Compact nav bar correctly shown | Verify interaction |
| `All /components/ routes` | Desktop | Light | OK | OK | Risk | Right TOC interaction | "On this page" TOC clicks do not scroll the page to the section. | Implement section anchors or remove click affordance. |

## Next Steps
1. Make `ComponentDocArticle` header bulletproof (prevent long titles from breaking the card layout).
2. Fix parameter table breakpoints.
3. Fix code block horizontal scrolling boundaries.
4. Add section anchors for TOC.
5. Create automated route validation script.
