## Overview
This PR introduces the Communication Suite Demo, an adaptive chat and mail productivity showcase. It demonstrates the ability to build a rich List/Detail layout with a single KMP codebase using AdaptiveKt primitives.

## Implemented Scope
- Chat shell, list, detail, composer, and dialogs.
- Mail shell, folders, thread list, reading pane, and compose dialog.
- Settings page.
- Fully built using `AdaptiveApp`, `AdaptiveTheme`, `AdaptiveNavigationScaffold`, and `AdaptiveTwoPane`.

## Actual Platforms
- Desktop/JVM
- Web/Wasm

## Actual Routes
- `#/chat/inbox`
- `#/chat/conversation/team-alpha`
- `#/chat/conversation/support-desk`
- `#/chat/search`
- `#/mail/inbox`
- `#/mail/thread/product-launch`
- `#/mail/thread/security-review`
- `#/mail/compose`
- `#/settings`

## Mock Data Counts
- 12 chat conversations
- 102 chat messages
- 21 mail threads
- 61 mail messages

## Testing & Validation
- **Tests**: Core logic tested via `CommunicationStateTest`.
- **Validation**:
  - Null responses correctly handled for hash-only navigation in Playwright.
  - Route validation succeeds without horizontal overflow or network failures.
  - Captured 12 responsive states across 5 viewports (mobile, tablet, laptop, desktop, ultrawide) and 2 themes (light, dark).
  - Generated contact sheets for light and dark themes.
- **CI Run**: [Run ID]
- **Artifacts**: Uploaded `communication-suite-screenshots` and `communication-suite-route-validation` successfully.

## Known Limitations
- Purely frontend UI state. Data does not persist between reloads.
- No real network transport.
