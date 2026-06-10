# AI Workspace AdaptiveKt component usage

Date: 2026-06-10

The AI Workspace demo is intended to demonstrate AdaptiveKt as the main UI vocabulary for a professional adaptive SaaS/productivity app.

| Component | Where used | Why used | Screenshot route |
|---|---|---|---|
| `AdaptiveApp` | App root | Provides AdaptiveKt application composition root. | all routes |
| `AdaptiveTheme` | App root | Uses system theme mode without app-level expect/actual. | all routes |
| `AdaptiveNavigationScaffold` | App shell | Responsive sidebar/rail/bottom navigation with overflow. | dashboard, all routes |
| `AdaptiveActionBar` | Shell and pages | Page-level context, badges, primary actions. | dashboard, chats, prompts, settings |
| `AdaptiveScrollablePage` | Dashboard, chats, prompts, settings, secondary routes | Page-level scroll and spacing. | all required routes |
| `AdaptiveSection` | Dashboard, prompts, settings, secondary routes | Semantic sections for admin/SaaS density. | dashboard, prompts, settings |
| `AdaptiveGrid` | Dashboard metrics/content, settings, prompts variables/runs | Responsive cards that avoid narrow desktop stacks. | dashboard, settings, prompts |
| `AdaptiveTwoPane` | Playground and internal list/detail implementation | Wide productivity layout and split workflows. | playground |
| `AdaptiveListDetailScaffold` | Chats, Prompts, Assistants, Knowledge, Tools, Evaluations | List/detail behavior across compact and desktop; pane sizing is now driven by library policy rather than demo width patches. | chats, prompts |
| `AdaptiveCard` | Metrics, hero, chat messages, prompt cards, settings panels | Consistent visual surfaces and clickable cards. | dashboard, chats, prompts, settings |
| `AdaptiveButton` | CTAs, page actions, chat send, tool toggles, settings actions | Replaces Material buttons with AdaptiveKt actions. | dashboard, chats, prompts, settings |
| `AdaptiveBadge` | Status, tags, model health, categories, metadata | Compact semantic status markers. | dashboard, chats, prompts, settings |
| `AdaptiveAvatar` | Top bar, chat list/detail, assistants | Identity visuals and centered initials. | dashboard, chats |
| `AdaptiveTabs` | Prompt detail | Shows multi-section detail content. | prompts |
| `AdaptiveSelect` | Prompt category filter, settings model picker | Adaptive dropdown/select coverage. | prompts, settings |
| `AdaptiveEmptyState` | Empty list/detail states | Replaces weak text-only empty panes. | chats, prompts |
| `AdaptiveCollectionView` | Dashboard prompt recommendations, prompt browser | Collection/list/card rendering without local layout systems. | dashboard, prompts |

## Generic Compose usage that remains

`Row`, `Column`, `Box`, `Spacer`, `Text`, and a few `LazyColumn` instances remain as small internal composition primitives. They are not used as the main page-level layout strategy for the recovered dashboard, chats, prompts, or settings screens.

The guard scripts fail on:

- `BoxWithConstraints`
- `breakpointForWidth`
- Material `Button`
- Material `Card`
- Material `TextField`
- Material `OutlinedTextField`

The guard scripts warn and count generic primitives so future changes can be reviewed without banning normal Compose composition.

## Library abstractions added during recovery

`AdaptiveListDetailPanePolicy` now lives in `adaptive-layout` and gives `AdaptiveListDetailScaffold` predictable side-by-side sizing:

- list min/preferred/max width;
- detail min/preferred/max width;
- pane gap;
- optional max content width;
- `fillAvailableWidth` for dashboard/workspace layouts that should consume the canvas.

AI Workspace consumes the scaffold defaults instead of passing manual `listPaneSpec`, `detailPaneSpec`, `BoxWithConstraints`, `breakpointForWidth`, or local width calculations.

`AdaptiveNavigationScaffold`, `Sidebar`, and `Drawer` now expose configurable navigation title/subtitle text. AI Workspace declares its shell branding through those parameters instead of inheriting the default admin-demo copy from the library.
