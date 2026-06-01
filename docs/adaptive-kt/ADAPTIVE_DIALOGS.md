# AdaptiveDialog

`AdaptiveDialog` is a modal surface component in `:adaptive-components`.

## Contract

- Renders a full-screen scrim with centered dialog content.
- `onDismissRequest` is called when the overlay is tapped.
- `title` is optional.
- `confirmButton` and `dismissButton` are composable action slots.

## Example

```kotlin
var showDialog by remember { mutableStateOf(true) }

if (showDialog) {
    AdaptiveDialog(
        title = "Confirm action",
        onDismissRequest = { showDialog = false },
        dismissButton = {
            AdaptiveButton(text = "Cancel", variant = AdaptiveButtonVariant.Ghost, onClick = { showDialog = false })
        },
        confirmButton = {
            AdaptiveButton(text = "Confirm", onClick = { showDialog = false })
        },
    ) {
        Text("Are you sure you want to continue?")
    }
}
```

## Use cases

- Confirmation dialogs
- Form warnings
- Multi-step modal workflows
