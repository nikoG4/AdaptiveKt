# AdaptiveDialog

Purpose: modal dialog surfaces for confirmation and dismiss workflows.

Use it when you need a focused prompt, warning, or decision dialog with actions.

Primary API: `AdaptiveDialog(onDismissRequest, title, confirmButton, dismissButton, content)`.

Example:

```kotlin
AdaptiveDialog(
    onDismissRequest = { showDialog = false },
    title = "Confirm action",
    dismissButton = {
        AdaptiveButton(text = "Cancel", variant = AdaptiveButtonVariant.Ghost, onClick = { showDialog = false })
    },
    confirmButton = {
        AdaptiveButton(text = "Confirm", onClick = { showDialog = false })
    },
) {
    Text("Are you sure you want to continue?")
}
```

Notes:

- Renders a full-screen overlay and centered surface.
- Provides a dismiss overlay tap and explicit action buttons.
- Ideal for destructive confirmations or modal forms.
