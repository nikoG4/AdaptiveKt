# Adaptive Button

## Purpose

`AdaptiveButton` is a versatile button component that supports multiple visual styles for different contexts: primary actions, secondary options, destructive actions, and ghost (minimal) buttons.

## When to Use

| Variant | Use Case | Example |
|---------|----------|---------|
| **Primary** | Main action on a screen | "Save", "Submit", "Create" |
| **Secondary** | Alternative action | "Cancel", "Close", "Discard" |
| **Ghost** | Tertiary action | "Skip", "Learn More", "Help" |
| **Danger** | Destructive action | "Delete", "Remove", "Unsubscribe" |

## API Signature

```kotlin
@Composable
fun AdaptiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AdaptiveButtonVariant = Primary,
    size: AdaptiveButtonSize = Medium,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
)
```

### Variants

- `Primary` — Solid fill, primary brand color
- `Secondary` — Outlined, neutral color
- `Ghost` — Text only, minimal visual weight
- `Danger` — Solid fill, danger/error color

### Sizes

- `Small` — Compact buttons (e.g., inline actions)
- `Medium` — Standard buttons (default)
- `Large` — Prominent buttons (e.g., CTAs)

## Simple Example

```kotlin
@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    
    AdaptiveButton(
        text = "Sign In",
        onClick = { login(email) },
        variant = Primary,
    )
}
```

## Advanced Example

```kotlin
@Composable
fun DataActions() {
    var isDeleting by remember { mutableStateOf(false) }
    
    Column {
        // Primary action
        AdaptiveButton(
            text = "Create",
            onClick = { navigateToCreate() },
            leadingIcon = { Icon(Icons.Filled.Add, "Create") },
        )
        
        // Secondary with trailing icon
        AdaptiveButton(
            text = "Export",
            onClick = { export() },
            variant = Secondary,
            trailingIcon = { Icon(Icons.Filled.Download, "Export") },
        )
        
        // Destructive action
        AdaptiveButton(
            text = if (isDeleting) "Deleting..." else "Delete All",
            onClick = { delete() },
            variant = Danger,
            enabled = !isDeleting,
        )
    }
}
```

## Responsive Behavior

Buttons adapt their size based on available space:

```kotlin
@Composable
fun ResponsiveButtons() {
    val adaptiveInfo = rememberAdaptiveInfo()
    
    val buttonSize = when (adaptiveInfo.breakpoint) {
        Compact -> Small      // Compact on phones
        Medium -> Medium
        else -> Large         // Larger on desktop
    }
    
    AdaptiveButton(
        text = "Action",
        onClick = {},
        size = buttonSize,
    )
}
```

In form layouts, buttons often stack vertically on compact screens:

```kotlin
@Composable
fun FormActions() {
    val adaptiveInfo = rememberAdaptiveInfo()
    val arrangement = if (adaptiveInfo.breakpoint == Compact) {
        Arrangement.Vertical
    } else {
        Arrangement.Horizontal
    }
    
    Row(horizontalArrangement = arrangement) {
        AdaptiveButton(text = "Cancel", onClick = {}, variant = Secondary)
        AdaptiveButton(text = "Save", onClick = {}, variant = Primary)
    }
}
```

## Multiplatform Notes

| Platform | Notes |
|----------|-------|
| **JVM/Desktop** | Full support; click states are animated |
| **Android** | Full support |
| **iOS** | Target declared; needs macOS validation |
| **Wasm** | Full support in browser |

## Icon Integration

Buttons support leading and trailing icon slots for flexibility:

```kotlin
// Leading icon (e.g., action icon)
AdaptiveButton(
    text = "Download",
    onClick = { download() },
    leadingIcon = { Icon(Icons.Filled.Download, null) },
)

// Trailing icon (e.g., dropdown indicator)
AdaptiveButton(
    text = "Options",
    onClick = { showOptions() },
    trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
)

// Icon-only button (use AdaptiveIconButton instead)
AdaptiveIconButton(
    onClick = { action() },
    icon = Icons.Filled.Close,
)
```

## Disabled State

```kotlin
val isLoading by remember { mutableStateOf(false) }

AdaptiveButton(
    text = if (isLoading) "Saving..." else "Save",
    onClick = { save() },
    enabled = !isLoading,  // Disable while loading
)
```

## Known Limitations

- ⚠️ Icon slots accept composable lambdas; no direct `@DrawableRes` support yet
- ⚠️ Custom colors not yet supported (uses theme defaults)
- ⚠️ Loading state is consumer-driven (no built-in progress indicator)

## Related Components

- [`AdaptiveIconButton`](adaptive-icon-button.md) — Icon-only buttons
- [`AdaptiveTextField`](adaptive-text-field.md) — Form inputs paired with buttons
- [`AdaptiveCard`](adaptive-card.md) — Containers for buttons

## See Also

- [Component Showcase](../../admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/screens/ComponentsScreen.kt)
- [Button Design Patterns](../../adaptive-kt/DESIGN_PRINCIPLES.md#buttons)
