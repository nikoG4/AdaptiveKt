# AI Workspace Pane List Width Audit

Date: 2026-06-10

## Affected Screens

- AI Workspace Chats: `#/chats` and `#/chats/c1`
- AI Workspace Prompt Library: `#/prompts` and `#/prompts/p1`

## Files Inspected

- `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/AdaptiveListDetailScaffold.kt`
- `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/AdaptivePage.kt`
- `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/AdaptiveSection.kt`
- `adaptive-data/src/commonMain/kotlin/io/github/adaptivekt/data/AdaptiveCollectionView.kt`
- `examples/ai-workspace-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/aiworkspace/ui/screens/chat/ChatWorkspaceScreen.kt`
- `examples/ai-workspace-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/aiworkspace/ui/screens/prompts/PromptLibraryScreen.kt`

## Root Cause

The list/detail pane sizing policy was already giving Chats and Prompt Library a usable list pane width, but the content inside the list pane was composed with page-level containers.

`AdaptiveScrollablePage` applies full page padding based on the current layout info. That is correct for full-page screens, but inside a side pane it consumes too much horizontal space and makes list items look like narrow content floating inside a wider pane.

Chats also grouped rows inside a local surface that did not explicitly fill the pane width. Prompt Library used card-style collection items inside the side pane, creating a card-inside-pane feel where prompt items were visually narrower than the useful pane width.

## Why This Belongs In AdaptiveKt

The problem is not specific to AI Workspace. Any app using `AdaptiveListDetailScaffold` can need a pane-aware list container that behaves differently from a full page:

- fills the available pane width;
- uses pane-appropriate padding;
- avoids page max-width behavior;
- lets rows/items naturally fill the useful content width;
- works in compact and side-by-side list/detail modes.

Adding this to `adaptive-layout` keeps demos declarative. The demo says “this is a list inside a pane”; the library owns the spacing and fill behavior.

## Final Fix Chosen

Added pane-list primitives to `adaptive-layout`:

- `AdaptivePaneDefaults`
- `AdaptivePaneList`
- `AdaptivePaneListGroup`
- `AdaptivePaneListItem`

Chats now uses `AdaptivePaneList` plus `AdaptivePaneListGroup` for the conversation inbox. Each row uses `AdaptivePaneListItem` and fills the useful width.

Prompt Library now uses `AdaptivePaneList` for pane-level scrolling and `AdaptiveCollectionView` in list mode, with each prompt rendered as an `AdaptivePaneListItem`. This removes the narrow card look while preserving selection, tags and detail-pane behavior.

## Guardrails

The fix does not add:

- `BoxWithConstraints` in `examples/ai-workspace-demo`;
- `breakpointForWidth` in `examples/ai-workspace-demo`;
- hardcoded screen-width calculations;
- route-specific pane width values;
- spacer hacks;
- Material `Button`, `Card`, `TextField`, or `OutlinedTextField` for user-facing UI.

The remaining generic `Row`, `Column`, `Box`, `Spacer`, and `Text` usages are internal composition primitives inside AdaptiveKt-facing components.
