# Adaptive Functional Icons

AdaptiveIcons provides minimal functional icons required by AdaptiveKt components. It is not a general-purpose icon pack.

## Objective

`AdaptiveIcons` exists so shared AdaptiveKt components do not need to render raw text glyphs such as `"x"`, `"v"`, `"+"`, `">"`, or `"..."` for common UI affordances.

The icons are embedded in `:adaptive-components`, implemented with Compose `Canvas`, and require no external dependency, icon pack, Material 3, SVG files, or remote assets.

## Icons

Implemented in `io.github.adaptivekt.components.icons.AdaptiveIcons`:

- `Close`
- `ChevronDown`
- `ChevronRight`
- `ChevronLeft`
- `Plus`
- `MoreVertical`
- `Menu`
- `Search`
- `Check`

Each icon follows the same API shape:

```kotlin
@Composable
fun Close(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = Color(0xFF374151),
    contentDescription: String? = null,
)
```

## Accessibility Semantics

Each icon supports optional `contentDescription` for accessibility via Compose `semantics`:

- When `contentDescription` is `null` (default), the icon is treated as decorative and no accessibility semantics are applied.
- When `contentDescription` is provided, it is exposed via `Modifier.semantics` so screen readers can announce the icon's purpose.

Example with accessibility:

```kotlin
AdaptiveIcons.Close(
    contentDescription = "Close search filter"
)
```

Example without accessibility (decorative):

```kotlin
AdaptiveIcons.MoreVertical(
    // icon is decorative, no contentDescription needed
)
```

## Internal Usage

- `AdaptiveSearchField` uses `AdaptiveIcons.Search` as the default leading affordance.
- `AdaptiveSearchField` uses `AdaptiveIcons.Close` for clear actions.
- `adaptive-data` uses `AdaptiveIcons.MoreVertical` for row overflow triggers.
- `adaptive-navigation` uses `AdaptiveIcons.Menu` and `AdaptiveIcons.Close` for the compact drawer toggle.
- `admin-demo` uses `Plus`, `ChevronRight`, `ChevronDown`, `Search`, `Close`, and `Check` in obvious component affordances and showcase examples.

## Developer Icons

AdaptiveKt still exposes icon slots such as `leadingIcon` and `trailingIcon`. Developers can keep using their own domain icons through those slots.

`AdaptiveIcons` should stay limited to component-level affordances. It should not become a domain icon set for profile, settings, edit, delete, reports, products, or app-specific concepts.

## Future Candidates

Possible future additions, only when needed by shared components:

- `Warning`
- `Info`
- `Filter`
- `Sort`

## Out Of Scope

- No Font Awesome dependency.
- No Lucide dependency.
- No Tabler dependency.
- No Material Icons dependency.
- No general-purpose icon pack.
- No app/domain icon catalog.
