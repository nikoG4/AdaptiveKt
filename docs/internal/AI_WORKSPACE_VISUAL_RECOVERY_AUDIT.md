# AI Workspace visual recovery audit

Date: 2026-06-10
Branch: `refactor/ai-workspace-premium-adaptive-ui`

## Scope

Target audited:

- `examples/ai-workspace-demo/src/commonMain`
- AI Workspace shell and navigation
- Dashboard / Home
- Chats
- Prompts
- Settings
- Secondary routes: Assistants, Playground, Knowledge, Tools, Evaluations

This audit records the visual recovery work before the AI Workspace demo is presented as a public AdaptiveKt showcase.

## Current screens

| Screen | Route | Primary pattern |
|---|---|---|
| Dashboard | `/` | `AdaptiveScrollablePage`, `AdaptiveActionBar`, `AdaptiveGrid`, `AdaptiveCard` |
| Chats | `#/chats` and `#/chats/{id}` | `AdaptiveListDetailScaffold` |
| Prompts | `#/prompts` and `#/prompts/{id}` | `AdaptiveListDetailScaffold`, `AdaptiveTabs`, `AdaptiveSelect` |
| Settings | `#/settings` | `AdaptiveScrollablePage`, `AdaptiveSection`, `AdaptiveGrid`, `AdaptiveCard` |
| Assistants | `#/assistants` | `AdaptiveListDetailScaffold` |
| Playground | `#/playground` | `AdaptiveTwoPane` |
| Knowledge | `#/knowledge` | `AdaptiveListDetailScaffold` |
| Tools | `#/tools` | `AdaptiveListDetailScaffold` |
| Evaluations | `#/evaluations` | `AdaptiveListDetailScaffold` |

## Screenshots reviewed

The visual recovery was based on real screenshots where the demo technically rendered but looked below showcase quality:

- Desktop dashboard with narrow stacked cards and wasted canvas.
- Tablet/medium navigation showing truncated labels such as `Dashbo`, `Assist`, and `Playgr`.
- Mobile chat list with top padding/clipping issues.
- Chats and Prompts detail panes that looked empty or abandoned.

Final screenshots are recorded separately in `docs/internal/AI_WORKSPACE_PREMIUM_VISUAL_VALIDATION.md`.

## AdaptiveKt component usage after recovery

Counts from `examples/ai-workspace-demo/src/commonMain/kotlin`:

| Component | Count |
|---|---:|
| `AdaptiveNavigationScaffold` | 1 |
| `AdaptiveActionBar` | 22 |
| `AdaptiveScrollablePage` | 18 |
| `AdaptiveSection` | 30 |
| `AdaptiveGrid` | 17 |
| `AdaptiveTwoPane` | 3 |
| `AdaptiveListDetailScaffold` | 9 |
| `AdaptiveCard` | 37 |
| `AdaptiveButton` | 59 |
| `AdaptiveBadge` | 91 |
| `AdaptiveAvatar` | 9 |
| `AdaptiveTabs` | 2 |
| `AdaptiveSelect` | 5 |
| `AdaptiveEmptyState` | 6 |
| `AdaptiveCollectionView` | 5 |

## Generic component usage counts

Generic Compose primitives remain for internal composition only:

| Primitive | Count | Notes |
|---|---:|---|
| `Row(` | 52 | Internal layout inside Adaptive cards, list rows and panes. |
| `Column(` | 32 | Internal stacking inside Adaptive page sections. |
| `Box(` | 13 | Icons, empty-state visuals, previews. |
| `Spacer(` | 39 | Local spacing inside components. |
| `Text(` | 133 | Text rendering uses Material typography but AdaptiveTheme colors. |
| `LazyColumn(` | 4 | Secondary legacy list/detail screens only. |
| `LazyRow(` | 0 | Not used. |
| `LocalAdaptiveLayoutInfo.current` | 3 | Shell/dashboard adaptivity; no manual breakpoint math. |

Hard-banned patterns are covered by `scripts/check-ai-workspace-layout-guards.ps1` and `.sh`.

## Root causes

### Wasted width

The dashboard relied on narrow card stacks and did not intentionally compose a wide SaaS dashboard. The recovery replaces that with a metric grid plus main/aside cards so 1280px and 1440px viewports use the canvas.

List/detail routes also exposed a library-level pane issue: the older `AdaptiveListDetailScaffold` used weighted outer boxes with an inner preferred-width list pane, leaving empty space between the list and detail on wide screens. The fix moved into `adaptive-layout` via `AdaptiveListDetailPanePolicy`, so demos no longer patch pane widths locally.

### Vertical-only dashboard

Cards were placed in a mostly single-column flow. The recovery uses `AdaptiveGrid` spans to produce one column on compact, two columns on medium, and four metrics across on expanded/large.

### Clipped mobile content

The shell ignored `AdaptiveNavigationScaffold` content padding. The recovery applies scaffold padding around routed content so the top bar and bottom bar do not cover the first list item.

### Truncated rail labels

Navigation labels were long while the rail renderer intentionally compacted labels. The recovery uses short product labels (`Home`, `Chats`, `Prompts`, `Agents`, `Lab`, `Files`, `Tools`, `Evals`, `Config`), More-menu overflow behavior, and the library no longer slices rail labels to six characters.

### Weak empty states

Chats and Prompts rendered plain text for empty detail panes. The recovery uses `AdaptiveEmptyState` with glyphs and actions.

### Non-showcase generic UI

User-facing Material `Button`, `Card`, `TextField`, and `OutlinedTextField` usage made the demo feel generic. The recovery swaps those for AdaptiveKt primitives and adds guards to prevent regressions.
