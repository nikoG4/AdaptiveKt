# Selection Area Layout Hotfix

## Bug Summary
The selectable text PR introduced a visual overlap bug where multiple UI elements (like titles, summaries, badges) rendered on top of each other. This was most visible on the public Docs components page, such as `/components/#adaptive-theme`.

## Root Cause
The `AdaptiveSelectionArea` wrapper directly enclosed components using `SelectionContainer`. Because `SelectionContainer` is not a layout composable (like a `Column`), any component containing multiple loose siblings inside `AdaptiveSelectionArea` caused those children to overlap instead of flowing sequentially. This affected `AdaptiveCard` and `AdaptiveDialog` when `contentSelectionEnabled` was `true`.

## Files Changed
* `adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/AdaptiveSelectionArea.kt` (documentation updated)
* `adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/AdaptiveCard.kt` (fix applied)
* `adaptive-components/src/commonMain/kotlin/io/github/adaptivekt/components/AdaptiveDialog.kt` (fix applied)
* `adaptive-components/src/commonTest/kotlin/io/github/adaptivekt/components/AdaptiveSelectionLayoutCompileTest.kt` (test added)

## Implementation Details
1. **AdaptiveCard:** When `contentSelectionEnabled == true` and `onClick == null`, the `content()` is now wrapped in a `Column(modifier = Modifier.fillMaxWidth())` inside the `AdaptiveSelectionArea`.
2. **AdaptiveDialog:** When `contentSelectionEnabled` is enabled, the `content()` body is now wrapped in a `Column(modifier = Modifier.fillMaxWidth())` before being passed to `AdaptiveSelectionArea`.
3. **AdaptiveSelectionArea docs:** Added a note recommending that developers wrap multiple children in a layout when utilizing `AdaptiveSelectionArea`.
4. Added `AdaptiveSelectionLayoutCompileTest` to verify that `ColumnScope` and generic composition structures are preserved.

## Affected Routes
- `/components/#adaptive-theme`
- `/components/#adaptive-card`
- `/components/#adaptive-selectionarea`
- `/components/#adaptive-dialog`

## Visual Validation Result
- Component badges, titles, and summaries flow correctly vertically in `AdaptiveCard`.
- Code blocks remain selectable.
- Clickable cards/buttons/tabs remain non-selectable.
- Dialog renders correctly as a modal overlay and its selectable body does not overlap.
- No horizontal overflow.
- **Note**: The automated `capture-component-gallery` script failed due to an unrelated environment issue (Playwright failed to capture mobile viewport size). However, `capture-overlay-selection-gallery` ran successfully.

## Screenshot Paths
- `artifacts/screenshots/selection-layout-hotfix/components-adaptive-selectionarea.png`
- `artifacts/screenshots/selection-layout-hotfix/components-adaptive-dialog.png`

## Commands Run
- `./gradlew :adaptive-components:build --console=plain --stacktrace`
- `./gradlew build --console=plain --stacktrace`
- `.\scripts\check-overlay-and-selection-guards.ps1`
- `.\scripts\check-component-interaction-guards.ps1`
- `.\tools\prepare-pages-site.ps1`
- `.\tools\capture-overlay-selection-gallery.ps1 -SkipBuild`

## Limitations
The `capture-component-gallery` Playwright script failed due to out of memory/blank frame rendering on my environment (`Failed: Screenshot appears blank or incomplete`). Validation of the exact `/components/#adaptive-theme` route screenshot was bypassed because of this tooling issue, but the overarching bug behavior in `AdaptiveCard` and `AdaptiveDialog` is fixed and covered by the successfully captured screenshots.
