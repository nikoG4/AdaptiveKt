# AdaptiveAccordion

Purpose: expandable disclosure sections for nested details and settings panels.

Use it to hide secondary content behind a title row while still exposing it on demand.

Primary API: `AdaptiveAccordion(title, subtitle, expanded, defaultExpanded, onExpandedChange, content)`.

Example:

```kotlin
AdaptiveAccordion(
    title = "Account settings",
    subtitle = "Manage permissions and billing",
    defaultExpanded = false,
) {
    Text("Panel content goes here.")
}
```

Notes:

- Supports controlled and uncontrolled expansion.
- Text and icon affordances indicate the disclosure state.
- Good for settings, help panels, or short content sections.
