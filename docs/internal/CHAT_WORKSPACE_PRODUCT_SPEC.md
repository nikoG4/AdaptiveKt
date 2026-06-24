# Adaptive Chat Product Specification

## Product Purpose
Adaptive Chat is a serious, responsive messaging workspace built to showcase the power and flexibility of AdaptiveKt. It demonstrates how a complex, realistic productivity application can seamlessly adapt across mobile, tablet, desktop, and ultrawide layouts using Kotlin Multiplatform and Compose Multiplatform.

## Target Users
- Developers evaluating AdaptiveKt for building complex, multi-pane applications.
- Designers looking for reference implementations of adaptive layouts and responsive navigation.

## Information Architecture
The top-level structure consists of four primary destinations:
1. **Chats**: A robust messaging experience including direct messages, groups, and channels.
2. **Contacts**: A directory of users and their presence/profiles.
3. **Calls**: A mock audio/video calling history and active call interface.
4. **Settings**: User profile, preferences, appearance controls (true dark mode), privacy, and developer settings.

## Navigation Model
The navigation automatically adapts to the available screen size:
- **Mobile Navigation Model (Compact)**: Uses a bottom navigation bar. Inside detailed flows (e.g., viewing a specific conversation or active call), the bottom bar may be hidden to provide a native full-screen chat flow, with top app bars providing navigation back to the primary list.
- **Tablet (Medium)**: Uses a side navigation rail. Enables list/detail views simultaneously if space permits.
- **Desktop (Expanded/Large)**: Uses a full sidebar containing navigation labels.

## Desktop Pane Model
On Expanded and Large layouts, the application employs a multi-pane architecture:
- Left: Navigation Sidebar
- Center-Left: Conversation/Contact List
- Center: Main content (conversation history, contact details)
- Right (Optional): An additional, dismissible details pane for showing extended info (e.g., members, shared files, settings specific to the chat).

## Visual Language
The product uses a modern, calm, and professional aesthetic tailored for productivity. It relies on subtle surfaces, clear information hierarchy, and consistent token-driven design (using AdaptiveTheme tokens) to provide a premium feel. It includes a true dark mode.

## Mock-Only Limitations
Adaptive Chat is a pure frontend demonstration. It explicitly relies on in-memory mock state and data.
- **No Backend**: No real network connections, authentication, or cloud storage.
- **No Real WebRTC**: Calling interfaces are simulated UI components, they do not request camera/mic permissions or establish actual media streams.

## AdaptiveKt Components Demonstrated
The application relies heavily on AdaptiveKt primitives, including but not limited to:
- `AdaptiveNavigationScaffold`, `AdaptiveNavigationBehavior`
- `AdaptiveListDetailScaffold`, `AdaptiveTwoPane`
- `AdaptiveCard`, `AdaptiveButton`, `AdaptiveIconButton`
- `AdaptiveSearchField`, `AdaptiveBadge`, `AdaptiveDialog`
- `AdaptiveLoadingState`, `AdaptiveEmptyState`, `AdaptiveErrorState`

## Explicitly Deferred Features
- **Mail functionality**: Completely removed from this demonstration to be handled as an independent showcase (`examples/mail-client-demo`).
- Complex custom message rendering beyond the mocked types (e.g., no live markdown editors, advanced rich text formatting).
- Real-time network sync.
