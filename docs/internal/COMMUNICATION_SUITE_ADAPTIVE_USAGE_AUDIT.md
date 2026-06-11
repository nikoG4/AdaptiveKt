# Communication Suite Adaptive Usage Audit

This document summarizes how `AdaptiveKt` primitives are utilized across the Communication Suite Demo and justifies remaining manual Layout composables.

## Component Usage

| Adaptive Primitive | Where Used | Purpose |
|--------------------|------------|---------|
| `AdaptiveApp` | `CommunicationApp.kt` | Root application scope |
| `AdaptiveTheme` | `CommunicationApp.kt` | Theming engine |
| `AdaptiveNavigationScaffold` | `CommunicationApp.kt` | App shell navigation (rail/bottom-bar) |
| `AdaptiveTwoPane` | `ChatArea.kt`, `MailArea.kt` | Main split layouts for list/detail |
| `AdaptiveActionBar` | `ChatArea.kt`, `MailArea.kt`, `SettingsArea.kt` | Header actions |
| `AdaptiveSection` | `ChatArea.kt`, `SettingsArea.kt` | Content grouping |
| `AdaptiveCard` | `ChatArea.kt`, `MailArea.kt`, `SettingsArea.kt` | Content surface elevation |
| `AdaptiveTextField` | `ChatArea.kt`, `MailArea.kt`, `SettingsArea.kt` | Search and composer inputs |
| `AdaptiveSelect` | `SettingsArea.kt` | Theme and density dropdowns |
| `AdaptiveButton` | `ChatArea.kt`, `MailArea.kt`, `SettingsArea.kt` | Form actions |
| `AdaptiveIconButton` | `ChatArea.kt`, `MailArea.kt` | Toolbar icon actions |
| `AdaptiveBadge` | `ChatArea.kt`, `MailArea.kt`, `CommunicationApp.kt` | Unread counts, labels, mock icons |
| `AdaptiveAvatar` | `ChatArea.kt`, `MailArea.kt` | Participant identities |
| `AdaptiveEmptyState` | `ChatArea.kt`, `MailArea.kt` | Fallback when no item selected |
| `AdaptiveSelectionArea` | `MailArea.kt` | Message body text selection |

## Manual Layout Composable Usage

- **Count:** ~15 explicit uses of `Row`, `Column`, `Box`, `Spacer`.
- **Why Acceptable:** AdaptiveKt provides high-level layouts (`AdaptiveTwoPane`, `AdaptiveNavigationScaffold`) but low-level internal content structure (e.g. aligning an avatar, text, and badge horizontally within a list row) strictly requires `Row`/`Column`.
- **Violations:** No raw `BoxWithConstraints` or manual breakpoint arithmetic (`breakpointForWidth`) is used in the application code. All responsive decisions are deferred to `AdaptiveNavigationScaffold` and `AdaptiveTwoPane`.

## Recommendations for Future Components
- **AdaptiveAvatarGroup:** A primitive to show overlapping avatars for `ConversationType.Channel`.
- **AdaptiveComposer:** A dedicated chat input block primitive.
