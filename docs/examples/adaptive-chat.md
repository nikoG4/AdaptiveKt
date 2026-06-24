# Adaptive Chat showcase

Adaptive Chat is a frontend-only communication workspace built from shared Compose Multiplatform UI. It demonstrates how AdaptiveKt combines responsive navigation, list-detail layouts, theme-aware components, and deterministic mock state in one application.

- [Open the live Web/Wasm demo](https://nikog4.github.io/AdaptiveKt/examples/communication-suite/)
- Source: [`examples/communication-suite-demo`](../../examples/communication-suite-demo)

## What the showcase includes

- Chat inbox, message search, direct and group conversations.
- Contacts with list, favorites, pending, blocked, and detail states.
- Recent, missed, incoming, and active call states.
- Profile, appearance, notification, privacy, data, developer, and help settings.
- Light, dark, compact, medium, expanded, and large layout validation.

## Responsive application navigation

`AdaptiveNavigationScaffold` resolves the navigation surface from the active breakpoint. Adaptive Chat requests bottom navigation on compact screens while allowing the library to use rail and sidebar surfaces when more width is available.

```kotlin
AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = selectedAreaId,
    onItemSelected = ::selectArea,
    preferBottomNavigationOnCompact = true,
    navigationTitle = "Adaptive Chat",
    navigationSubtitle = "Communication Suite",
    isNavigationVisible = { placement ->
        placement != AdaptiveNavigationPlacement.BottomBar ||
            state.activeArea != AppArea.Chat ||
            state.selectedConversationId == null
    },
) { padding ->
    CommunicationContent(Modifier.padding(padding))
}
```

The `isNavigationVisible` callback runs after the responsive placement has been resolved. In this demo it hides only the compact bottom bar while a conversation is open. Rail and sidebar navigation remain visible on wider layouts.

## Responsive list-detail flow

The chat workspace uses `AdaptiveListDetailScaffold` instead of calculating widths or breakpoints in the demo.

```kotlin
AdaptiveListDetailScaffold(
    selectedItem = state.selectedConversationId,
    onBackToList = { state.selectedConversationId = null },
    listPane = {
        ChatInbox(state)
    },
    detailPane = { selectedId ->
        val conversation = state.conversations.find { it.id == selectedId }
        if (conversation != null) {
            ChatDetail(state, conversation)
        } else {
            AdaptiveEmptyState(
                title = "No conversation selected",
                description = "Choose a conversation to start chatting.",
            )
        }
    },
)
```

On compact screens the scaffold shows the list until a conversation is selected, then switches to the detail pane with a back header. On larger screens it renders list and detail side by side according to the shared pane policy.

## Platforms

| Target | Status |
| --- | --- |
| Android | Application target enabled; minimum SDK 24 and target SDK 35. |
| Desktop/JVM | Runnable desktop application and native distribution configuration. |
| Web/Wasm | Runnable browser application and the publicly hosted demo. |
| iOS | Not configured for this standalone showcase. |

## Run locally

From the repository root:

```powershell
.\gradlew.bat -p examples/communication-suite-demo desktopRun
.\gradlew.bat -p examples/communication-suite-demo wasmJsBrowserDevelopmentRun
.\gradlew.bat -p examples/communication-suite-demo assembleDebug
```

## Validation

The repository CI builds the standalone demo and its Wasm distribution. Visual tooling validates representative routes across mobile, tablet, laptop, desktop, and ultrawide viewports in light and dark themes.

## Deliberate limitations

- All conversations, contacts, calls, and settings use local mock data.
- State is not persisted across reloads or application restarts.
- There is no backend, authentication service, push notification system, or real network transport.
- The showcase demonstrates UI architecture and AdaptiveKt integration rather than production chat infrastructure.
