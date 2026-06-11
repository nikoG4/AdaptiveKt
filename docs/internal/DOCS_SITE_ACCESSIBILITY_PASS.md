# Docs Site Accessibility Pass

This document details the accessibility state of the AdaptiveKt docs site, focusing on keyboard navigation, contrast, and focus visibility in Compose Wasm.

## Findings

### Keyboard Navigation
- **Navigation Sidebar:** The sidebar `AdaptiveNavigationTree` can receive focus, but arrow key navigation inside the tree relies on Compose Multiplatform's default focus traversal, which may not fully map to ARIA treeview semantics natively on the web.
- **Top Bar & Footer:** Standard links and theme toggles are reachable via Tab.
- **TOC (Right Sidebar):** Fake click affordances were removed. Keyboard traversal skips the TOC items properly now, which is the desired behavior since they do not currently scroll.
- **Component Articles:** Buttons, text fields, and dialog triggers can be accessed via Tab. Space/Enter correctly triggers clicks.

### Focus Visibility
- **Action Components:** `AdaptiveButton` and `AdaptiveIconButton` show subtle state changes on focus/hover (using `AdaptiveTheme.states.hover/pressed`), but an explicit, high-contrast focus ring (like `outline: 2px solid`) is not currently implemented globally in AdaptiveTheme.
- **Recommendation:** Implement a global `Modifier.focusable` wrapper in `AdaptiveTheme` that renders a distinct ring (e.g., `AdaptiveTheme.colors.primary`) when `isFocused` is true and interaction is from a keyboard.

### Color Contrast
- **Light Theme:** Primary text (`SiteInk`) against background (`SiteSoft`) maintains high contrast (> 4.5:1). Neutral badges and muted text (`SiteMuted`) have sufficient contrast for secondary content.
- **Dark Theme:** Similar results. The component gallery ensures dark mode backgrounds and inverted text colors are readable. 

### Screen Reader Support
- **Compose Semantics:** Compose 1.6+ exports semantics to a hidden DOM tree. Elements with text (e.g., `SiteText`, `AdaptiveButton`) are naturally readable.
- **Missing Semantics:** Interactive icons (e.g., `AdaptiveIcons.Search()`) inside `AdaptiveIconButton` need `contentDescription` properties to be fully accessible. Currently, many `AdaptiveIconButton` usages in `SiteComponentsPage.kt` do not pass explicit content descriptions.
- **Dialogs & Overlays:** Compose handles `Dialog` trapping natively, but `AdaptiveSelectionArea` might interfere with screen reader cursors if not careful.

## Next Steps
1. Add explicit `contentDescription` attributes to all icon buttons in the gallery.
2. Implement a global keyboard focus ring in `AdaptiveTheme` (Future issue).
3. Wait for Compose Multiplatform to improve Wasm/JS ARIA tree exports for `AdaptiveNavigationTree`.
