# AdaptiveKt — Navigation Patterns

## Purpose
AdaptiveKt provides scaffolded navigation patterns for Compose Multiplatform without depending on a routing library.

## High-level navigation goals

- Compact: drawer/hamburger with optional bottom navigation.
- Medium: drawer or rail with top bar.
- Expanded/Large: persistent sidebar with top bar.

## Adaptive navigation contract

### AdaptiveNavItem

A navigation item representation.

```kotlin
data class AdaptiveNavItem(
    val id: String,
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
    val enabled: Boolean = true,
)
```

### AdaptiveNavigationMode

Defines the scaffold navigation presentation mode.

```kotlin
enum class AdaptiveNavigationMode {
    Drawer,
    BottomNavigation,
    NavigationRail,
    Sidebar,
}
```

### AdaptiveNavigationScaffold

The primary scaffold for adaptive navigation chrome. It chooses presentation automatically by breakpoint and uses slots for optional customization only.

```kotlin
@Composable
fun AdaptiveNavigationScaffold(
    navItems: List<AdaptiveNavItem>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    preferBottomNavigationOnCompact: Boolean = false,
    navigationItemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    navigationDensity: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    topBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
)
```

### Navigation Item Style

Default navigation rows are now flat pill/list rows rather than card-like rows. The card treatment remains available as an opt-in variant.

```kotlin
enum class AdaptiveNavigationItemStyle {
    Pill,
    Card,
    Minimal,
}

enum class AdaptiveNavigationDensity {
    Compact,
    Comfortable,
}
```

- `Pill` is the default admin dashboard style: compact rows, subtle hover state, primary-subtle selected state and an optional left active indicator.
- `Card` preserves the earlier larger card/touch-friendly treatment.
- `Minimal` is suitable for documentation sidebars or dense settings navigation.
- `Comfortable` keeps rows around `44.dp`.
- `Compact` keeps rows around `40.dp`.

## Breakpoint rules

`AdaptiveNavigationScaffold` uses `AdaptiveBreakpoint` to choose presentation:

- `Compact`
  - primary navigation in `Drawer`,
  - optional `BottomNavigation` for primary destinations,
  - `TopBar` includes a hamburger toggle.

- `Medium`
  - primary navigation is a `NavigationRail`,
  - `Sidebar` and `BottomNavigation` are not the default presentation,
  - `TopBar` remains visible for actions and context.

- `Expanded`
  - primary navigation is a persistent `Sidebar`,
  - `BottomNavigation` is hidden,
  - `TopBar` is used for actions and context.

- `Large`
  - primary navigation is a persistent `Sidebar`,
  - `BottomNavigation` is hidden,
  - `TopBar` is used for actions and context.

## Public navigation components

### AdaptiveNavigationTree

`AdaptiveNavigationTree` is a controlled hierarchy primitive for nested admin navigation. It does not replace `AdaptiveNavigationScaffold`; use it inside sidebars, drawers, or settings panels when a navigation group needs children.

```kotlin
AdaptiveNavigationTree(
    items = treeItems,
    selectedItemId = selected,
    onItemSelected = { selected = it.id },
    expandedItemIds = expanded,
    onExpandedItemIdsChange = { expanded = it },
    itemStyle = AdaptiveNavigationItemStyle.Pill,
    density = AdaptiveNavigationDensity.Comfortable,
)
```

It supports badges, disabled rows, `maxDepth`, controlled expansion, the same item style/density knobs, and dark-mode safe selected/hover states.

### Sidebar

```kotlin
@Composable
fun Sidebar(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
)
```

### Drawer

```kotlin
@Composable
fun Drawer(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
)
```

### BottomNavigation

```kotlin
@Composable
fun BottomNavigation(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
)
```

### NavigationRail

```kotlin
@Composable
fun NavigationRail(
    items: List<AdaptiveNavItem>,
    selectedItemId: String?,
    modifier: Modifier = Modifier,
    itemStyle: AdaptiveNavigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    density: AdaptiveNavigationDensity = AdaptiveNavigationDensity.Comfortable,
    onItemSelected: (String) -> Unit,
)
```

## Mobile and desktop behavior

### Compact

- use `Drawer` for primary navigation,
- optionally show `BottomNavigation` for a fixed set of top destinations,
- hide persistent sidebar.

### Medium

- allow `Drawer` or `Sidebar` depending on app needs,
- keep `TopBar` visible,
- use `BottomNavigation` only when the app has few destinations.

### Expanded / Large

- show a persistent `Sidebar` with the app structure,
- `TopBar` remains for global actions,
- drawer and bottom navigation are hidden unless explicitly enabled.

## Design constraints

- The scaffold is callback-based and independent of navigation routing.
- App state controls selection through `selectedItemId`.
- Child navigation components (`Sidebar`, `Drawer`, `BottomNavigation`, `NavigationRail`) are controlled components and require the selected ID.
- The library should not own route strings or navigation stack semantics.

## Example usage

```kotlin
val onItemSelected: (String) -> Unit = { id -> navigateTo(id) }

AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = selectedItem,
    onItemSelected = onItemSelected,
    navigationItemStyle = AdaptiveNavigationItemStyle.Pill,
    topBar = { AdminTopBar() },
) { padding ->
    ScreenContent(Modifier.padding(padding))
}
```

To keep the previous larger row treatment:

```kotlin
AdaptiveNavigationScaffold(
    navItems = navItems,
    selectedItemId = selectedItem,
    onItemSelected = onItemSelected,
    navigationItemStyle = AdaptiveNavigationItemStyle.Card,
)
```

## Compose Multiplatform notes

- On Web/WASM, persistent sidebar layouts are natural for wide viewports.
- On Android, `Drawer` and `BottomNavigation` are the default compact experiences.
- `AdaptiveNavigationScaffold` should not assume gesture parity across platforms.
