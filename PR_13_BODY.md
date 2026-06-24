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
- Android

## Actual Routes
- #/chat/inbox
- #/chat/conversation/c-123
- #/chat/conversation/c-group
- #/chat/search
- #/contacts
- #/contacts/favorites
- #/contacts/u-456
- #/calls
- #/calls/missed
- #/calls/incoming/call-789
- #/calls/active/call-012
- #/settings
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
- **CI Run**: 28124403210 (Commit 58a510bd3c0209962c59024017d3dc9c81dd0138)
- **Artifacts**: Uploaded visual screenshots and validation output effectively demonstrating the Adaptive Chat product transformation.

## Known Limitations
- Purely frontend UI state. Data does not persist between reloads.
- No real network transport.

## Final Polish Additions
- **Conditional BottomBar Policy**: Dynamically hides the BottomBar on compact screens when a conversation is opened (`isNavigationVisible` abstraction), maintaining an immersive chat view. The Sidebar and Rail always remain visible on larger screens.
- **Crash & Bug Fixes**: Replaced unstable `backStack.removeLast()` with bounds-safe `removeAt(backStack.lastIndex)`. Fixed compilation errors in AdaptiveIconButton.kt.
- **UI/UX Cleanup**: Resolved UTF-8 mojibake (\u2022) in CallsArea.kt, abstracted Active Call action buttons into a cohesive CallControl, fully stripped leftover unicode placeholders, aligned Developer Options buttons, and addressed Android application ID discrepancies.
- **Multiplatform Compatibility**: Verified builds and UI consistency across JVM, Wasm, and Android targets.
