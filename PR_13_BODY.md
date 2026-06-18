## Overview
This PR transforms the Communication Suite Demo into the Adaptive Chat product showcase. It demonstrates the ability to build a rich premium adaptive chat/calling workspace with a single KMP codebase using AdaptiveKt primitives, fully stripped of placeholder unicode glyphs and rebranded.

## Implemented Scope
- Chat Area (inbox, search, detail).
- Contacts Area (list, favorites, detail).
- Calls Area (recent, missed, incoming, active).
- Settings Area (profile, appearance, notifications, privacy, data, developer, help).
- Fully built using AdaptiveApp, AdaptiveTheme, AdaptiveNavigationScaffold, and AdaptiveTwoPane.
- Fully migrates off Unicode glyphs onto a bespoke DemoIcons scalable vector implementation.

## Actual Platforms
- Desktop/JVM
- Web/Wasm

## Actual Routes
- #/chat/inbox
- #/chat/detail/direct/u-456
- #/chat/detail/group/g-789
- #/chat/search
- #/contacts/list
- #/contacts/favorites
- #/contacts/detail/u-123
- #/calls/recent
- #/calls/missed
- #/calls/incoming
- #/calls/active/c-555
- #/settings/home
- #/settings/profile
- #/settings/appearance
- #/settings/notifications
- #/settings/privacy
- #/settings/data
- #/settings/developer
- #/settings/help

## Mock Data Counts
- 12 chat conversations
- 102 chat messages
- 40 contacts
- 15 call logs

## Testing & Validation
- **Tests**: Core logic tested via CommunicationStateTest.
- **Validation**:
  - Route validation succeeds without horizontal overflow or network failures.
  - Captured responsive states across 5 viewports (mobile, tablet, laptop, desktop, ultrawide) and 2 themes (light, dark) for a comprehensive visual suite.
  - Generated fully responsive visual contact sheets for light and dark themes.
- **CI Run**: [Run ID]
- **Artifacts**: Uploaded visual screenshots and validation output effectively demonstrating the Adaptive Chat product transformation.

## Known Limitations
- Purely frontend UI state. Data does not persist between reloads.
- No real network transport.
