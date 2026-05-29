# Dark Mode

AdaptiveKt supports dark mode through the shared theme foundation in `:adaptive-core`.

## API

```kotlin
AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultDark()) {
    App()
}
```

`defaultDark()` maps every field in `AdaptiveColorScheme`, including surfaces, borders, text, primary, semantic tones, disabled state, focus ring, and overlay.

## Toggle Example

```kotlin
var dark by remember { mutableStateOf(false) }

AdaptiveTheme(
    colorScheme = if (dark) {
        AdaptiveColorSchemes.defaultDark()
    } else {
        AdaptiveColorSchemes.defaultLight()
    },
) {
    AdminApp()
}
```

## Demo And Capture Routes

Docs-site:

```text
/?theme=dark
/components/?theme=dark
```

Admin demo:

```text
/?screen=dashboard&theme=dark
/?screen=components-selects&theme=dark
/?screen=components-multiselects&theme=dark
```

Desktop capture entry point:

```powershell
.\gradlew.bat :admin-demo:run --args="--capture --screen dashboard --theme dark --width 1440 --height 900 --output build/visual-captures/dark/dashboard-dark-large-1440x900.png"
```

## Current Status

- `:adaptive-components`, `:adaptive-feedback`, `:adaptive-forms`, `:adaptive-data`, and `:adaptive-navigation` consume `AdaptiveTheme` tokens.
- `:docs-site` and `:admin-demo` expose light/dark toggles.
- Web capture tooling includes dark smoke screenshots for the docs site and selected admin-demo screens.

## Limitations

- Platform presets are planned for T4.
- Brand preset APIs are not implemented yet.
- Demo-only illustrative colors may remain local to examples.
- Automated pixel-diff baselines are still future work.
