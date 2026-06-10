# AI Workspace premium visual validation

Date: 2026-06-10
Branch: `refactor/ai-workspace-premium-adaptive-ui`

Generated with:

```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\prepare-pages-site.ps1
powershell.exe -ExecutionPolicy Bypass -File .\tools\capture-ai-workspace-premium.ps1 -SkipBuild
```

Output directory:

`artifacts/screenshots/ai-workspace-premium-refactor/`

The capture script validates:

- Compose canvas exists and is non-trivial.
- Route loads successfully.
- Console error count is zero.
- Network failure count is zero.
- Horizontal overflow is false.
- Light and dark color schemes render.

## Screenshot Matrix

| Route | Viewport | Theme | Screenshot | Console | Network | Overflow | Result | Visual notes |
|---|---|---|---|---:|---:|---|---|---|
| dashboard | 390x844 | light | `mobile-dashboard-light.png` | 0 | 0 | no | pass | Compact layout shows top bar, content, and bottom navigation without clipping. |
| chats | 390x844 | light | `mobile-chats-light.png` | 0 | 0 | no | pass | First conversation is visible; bottom nav does not cover list content. |
| prompts | 390x844 | light | `mobile-prompts-light.png` | 0 | 0 | no | pass | Prompt list and filter fit compact width. |
| settings | 390x844 | light | `mobile-settings-light.png` | 0 | 0 | no | pass | Settings sections remain readable. |
| dashboard | 768x1024 | light | `tablet-dashboard-light.png` | 0 | 0 | no | pass | Rail labels are short and not truncated. |
| chats | 768x1024 | light | `tablet-chats-light.png` | 0 | 0 | no | pass | List/detail panes share the canvas predictably. |
| prompts | 768x1024 | light | `tablet-prompts-light.png` | 0 | 0 | no | pass | Library/detail layout stays stable. |
| settings | 768x1024 | light | `tablet-settings-light.png` | 0 | 0 | no | pass | Dense form content uses available width. |
| dashboard | 1280x800 | light | `desktop-dashboard-light.png` | 0 | 0 | no | pass | Metric grid and main cards use the desktop canvas. |
| chats | 1280x800 | light | `desktop-chats-light.png` | 0 | 0 | no | pass | Conversation list is a dense inbox-style list; detail pane fills remaining width. |
| prompts | 1280x800 | light | `desktop-prompts-light.png` | 0 | 0 | no | pass | Prompt list and empty detail pane are aligned. |
| settings | 1280x800 | light | `desktop-settings-light.png` | 0 | 0 | no | pass | Settings page is composed, not a narrow column. |
| dashboard | 1440x900 | light | `large-dashboard-light.png` | 0 | 0 | no | pass | Large viewport has no abandoned canvas. |
| chats | 1440x900 | light | `large-chats-light.png` | 0 | 0 | no | pass | List/detail policy fills the available work area. |
| prompts | 1440x900 | light | `large-prompts-light.png` | 0 | 0 | no | pass | Detail pane is a full panel, not a floating card. |
| settings | 1440x900 | light | `large-settings-light.png` | 0 | 0 | no | pass | Form sections maintain hierarchy. |
| dashboard | 390x844 | dark | `mobile-dashboard-dark.png` | 0 | 0 | no | pass | Compact dark mode has readable surfaces and badges. |
| chats | 390x844 | dark | `mobile-chats-dark.png` | 0 | 0 | no | pass | First conversation is visible and contrast is acceptable. |
| prompts | 390x844 | dark | `mobile-prompts-dark.png` | 0 | 0 | no | pass | Prompt list remains readable. |
| settings | 390x844 | dark | `mobile-settings-dark.png` | 0 | 0 | no | pass | Controls preserve contrast. |
| dashboard | 768x1024 | dark | `tablet-dashboard-dark.png` | 0 | 0 | no | pass | Rail and cards render cleanly. |
| chats | 768x1024 | dark | `tablet-chats-dark.png` | 0 | 0 | no | pass | List/detail panes remain predictable. |
| prompts | 768x1024 | dark | `tablet-prompts-dark.png` | 0 | 0 | no | pass | Prompt detail pane fills its side. |
| settings | 768x1024 | dark | `tablet-settings-dark.png` | 0 | 0 | no | pass | Settings controls remain legible. |
| dashboard | 1280x800 | dark | `desktop-dashboard-dark.png` | 0 | 0 | no | pass | Desktop dark dashboard uses full width. |
| chats | 1280x800 | dark | `desktop-chats-dark.png` | 0 | 0 | no | pass | No huge white/dark gap between list and detail. |
| prompts | 1280x800 | dark | `desktop-prompts-dark.png` | 0 | 0 | no | pass | Empty prompt detail appears inside a full detail pane. |
| settings | 1280x800 | dark | `desktop-settings-dark.png` | 0 | 0 | no | pass | Settings layout remains structured. |
| dashboard | 1440x900 | dark | `large-dashboard-dark.png` | 0 | 0 | no | pass | Large dark view looks intentionally composed. |
| chats | 1440x900 | dark | `large-chats-dark.png` | 0 | 0 | no | pass | Detail pane fills remaining width. |
| prompts | 1440x900 | dark | `large-prompts-dark.png` | 0 | 0 | no | pass | Prompt detail pane uses the full available side. |
| settings | 1440x900 | dark | `large-settings-dark.png` | 0 | 0 | no | pass | Large settings view remains balanced. |

## Hard-Fail Checklist

| Check | Result |
|---|---|
| Huge gap between list and detail | pass |
| Detail pane floating instead of filling remaining space | pass |
| Clipped first item on mobile | pass |
| Truncated tablet rail labels | pass |
| User-facing Material Button/Card/TextField | pass |
| Horizontal overflow | pass |
| Light/dark coverage | pass |

## Notes

- Screenshots are generated artifacts and should not be committed.
- `AdaptiveListDetailPanePolicy` is the library-level fix for desktop list/detail sizing.
- AI Workspace no longer passes local `listPaneSpec`, `detailPaneSpec`, `BoxWithConstraints`, `breakpointForWidth`, or `widthIn` hacks for the primary chat/prompt layout.
- AI Workspace uses configurable `AdaptiveNavigationScaffold` title/subtitle parameters so the sidebar branding is app-specific rather than inherited admin-demo copy.
