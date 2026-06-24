# Communication Suite Demo Plan

## Goal
Build a massive, polished, end-to-end showcase called "Communication Suite" demonstrating AdaptiveKt primitives through a realistic multiplatform Chat and Mail application.

## Modules and Files to Create
- `examples/communication-suite-demo/`: Standalone Gradle multiplatform project.
  - `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`
  - `src/commonMain/kotlin/.../`
    - `model/`: `UserProfile`, `Conversation`, `Message`, `MailThread`, `MailMessage`, etc.
    - `data/MockCommunicationData.kt`: Rich deterministic mock data.
    - `state/CommunicationState.kt`: View model holding selection, UI state, routing, dialog state.
    - `ui/`:
      - `CommunicationApp.kt`: Root shell, adaptive navigator.
      - `shell/`: Global navigation, title, search, layout structure.
      - `chat/`: Inbox, Detail, Composer, Dialogs.
      - `mail/`: Inbox, Thread List, Reading Pane, Compose.
      - `settings/`: Theme/density controls.
  - `src/wasmJsMain/`: Wasm entry point `main.kt` and `index.html`.
  - `src/desktopMain/`: JVM desktop entry point.

## AdaptiveKt Components to Showcase
- **Foundation**: `AdaptiveApp`, `AdaptiveTheme`, `AdaptiveBadge`, `AdaptiveAvatar`, `AdaptiveCard`.
- **Layouts**: `AdaptiveNavigationScaffold`, `AdaptiveTwoPane`, `AdaptiveListDetailScaffold` (if applicable), `AdaptiveColumnPage`, `AdaptiveSection`, `AdaptiveGrid`.
- **Navigation**: `AdaptiveTabs`, `AdaptiveBreadcrumbs`.
- **Inputs**: `AdaptiveButton`, `AdaptiveIconButton`, `AdaptiveTextField`, `AdaptiveSearchField`, `AdaptiveSelect`, `AdaptiveMultiSelect`, `AdaptiveFormLayout`.
- **Overlays/Feedback**: `AdaptiveDialog`, `AdaptiveEmptyState`, `AdaptiveLoadingState`, `AdaptiveErrorState`, `AdaptiveSelectionArea`.

## Routes & Screens
Hash-based routing via `AdaptiveNavigator`:
- `/examples/communication-suite/` (redirects to `/chat` or last area)
- `/chat`, `/chat/inbox`, `/chat/conversation/{id}`, `/chat/search`
- `/mail`, `/mail/inbox`, `/mail/thread/{id}`, `/mail/compose`
- `/settings`

## Responsive Behavior
- **390x844 (Mobile)**: Single pane. List or detail showing. Modals full width-ish. Composer stays visible above keyboard area.
- **768x1024 (Tablet)**: Two pane where possible (e.g., mail folders + thread list, chat inbox + detail).
- **1280x800 & Desktop**: Three panes (Sidebar + List + Detail). Navigation rail/sidebar, persistent action bars, optimal reading widths.

## No-Backend Data Strategy
- In-memory deterministically generated state.
- Rich mock content (12+ chat conversations, 80+ chat messages, 20+ mail threads, 60+ mail messages).
- Simulated states like loading, unread badges, typing indicators, pinned threads, draft persistence locally. No network calls or persistence to disk.

## Visual Validation Plan
- `validate-communication-suite-routes.ps1`: Launch Playwright to navigate hashes. Check for crashes and console errors.
- `capture-communication-suite.ps1`: Take screenshots across 390x844, 768x1024, 1280x800, 1440x900 viewports in both light and dark themes. Ensure no overlapping text or horizontal scrollbars.

## Limitations
- No backend/cloud sync.
- No real-time transport (WebSockets).
- No actual email sending.
- Local temporary storage only (state dies on refresh unless we mock sessionStorage/localStorage).
- No real authentication or file upload capabilities.
