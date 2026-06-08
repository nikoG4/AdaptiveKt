# Visual Validation Report

## Infrastructure
Currently, the AdaptiveKt project does not use an automated headless screenshot testing suite like Paparazzi for its web layouts due to limitations around Skia canvas testing inside standard Linux CI runners. All visual regressions are verified locally via Compose Desktop Previews and the Wasm output across multiple target viewports.

## Capture Matrix
The visual tests focus on testing the responsive primitives (`AdaptiveGrid`, `AdaptiveTwoPane`, `AdaptiveListDetailScaffold`) under standard constraints.

### Tested Viewports
- **Compact Mobile:** `390x844`
- **Medium Tablet:** `768x1024`
- **Expanded Laptop:** `1280x800`
- **Large Desktop:** `1440x900`

### Tested Routes & Results

#### AI Workspace Demo
| Route | Compact | Medium | Expanded | Result |
|---|---|---|---|---|
| Dashboard | Stacked single column | 2-column Grid | 3-column Grid | Pass |
| Chat | Full-screen list (no detail) | TwoPane (List + Detail) | TwoPane (List + Detail) | Pass |
| Prompts | Full-screen list | TwoPane (List + Detail) | TwoPane (List + Detail) | Pass |
| Knowledge | Single column | Single column table | Multi-column table | Pass |
| Settings | Single column | Single column | Single column with constraints | Pass |

#### Ecommerce Demo
| Route | Compact | Medium | Expanded | Result |
|---|---|---|---|---|
| Home/Grid | 2-column catalog | 3-column catalog | 4-column catalog | Pass |
| Products | Stacked layout | Stacked layout | Side-by-side details | Pass |
| Cart | Vertical list | Vertical list | Vertical list + Sidebar checkout | Pass |

## Output Path
Since visual artifact tracking causes severe repository bloat and GitHub Actions limits, screenshots are generated to a local ignored directory (`artifacts/screenshots/pages-validation/`) strictly for developer evaluation.

## Conclusion
The `AdaptiveListDetailScaffold` correctly defaults to Stacked navigation in Compact viewports and Panes in Expanded viewports. Icon alignments are crisp without font fallback rendering issues, and no `BoxWithConstraints` layout loops were detected.
