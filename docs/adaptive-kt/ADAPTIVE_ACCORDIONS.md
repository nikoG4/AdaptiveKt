# AdaptiveAccordion

`AdaptiveAccordion` is an expandable disclosure panel in `:adaptive-components`.

## Contract

- Renders a header with a title and optional subtitle.
- Supports controlled expansion via `expanded` / `onExpandedChange`.
- Defaults to an uncontrolled state with `defaultExpanded`.
- Animates content visibility with a toggle chevron.

## Example

```kotlin
AdaptiveAccordion(
    title = "Account settings",
    subtitle = "Manage notification and billing preferences.",
    defaultExpanded = true,
) {
    Text("Change your password, email, and user preferences.")
}
```

## Use cases

- Disclosure of settings panels
- FAQs and help content
- Inline details in admin forms
