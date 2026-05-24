# Adaptive Components API

## Objective

`:adaptive-components` is the shared visual primitives module for AdaptiveKt. It provides Foundation-only admin UI building blocks that can later be adopted by `adaptive-data`, `adaptive-forms`, `adaptive-navigation`, and `admin-demo` without creating dependency cycles.

Principle: default simple code should produce a professional admin UI.

## Dependency Rule

The module depends only on:

- `:adaptive-core`
- Compose Foundation/Runtime/UI supplied by the existing Compose Multiplatform setup

It does not depend on layout, navigation, data, forms, feedback, admin-demo, Material 3, icon packs, backend code, Web, JS, or Wasm.

## Implemented Components

- `AdaptiveButton`
- `AdaptiveIconButton`
- `AdaptiveBadge`
- `AdaptiveAvatar`
- `AdaptiveCard`
- `AdaptiveSurface`
- `AdaptiveDropdownMenu`
- `AdaptiveMenuItem`
- `AdaptiveTextField`
- `AdaptiveSearchField`
- `AdaptiveSectionHeader`
- `AdaptiveDivider`

## Examples

```kotlin
AdaptiveButton(
    text = "Save changes",
    onClick = ::save,
)

AdaptiveBadge("Active", tone = AdaptiveBadgeTone.Success)

AdaptiveAvatar(name = "Alicia Romero")

AdaptiveCard {
    AdaptiveSectionHeader(
        title = "Team",
        description = "Manage employees and access.",
    )
}

AdaptiveTextField(
    value = companyName,
    onValueChange = { companyName = it },
    label = "Company name",
    placeholder = "Acme Corp",
)
```

## Button API

- `AdaptiveButtonVariant`: `Primary`, `Secondary`, `Ghost`, `Danger`
- `AdaptiveButtonSize`: `Small`, `Medium`, `Large`
- Slots: `leadingIcon`, `trailingIcon`

Button hover and pressed states are painted inside the clipped pill shape. The implementation avoids rectangular external hover overlays.

## Avatar API

`AdaptiveAvatar` renders the supplied image slot inside the requested shape, or falls back to initials.

`initialsForName(name)` rules:

- `"Alicia Romero"` -> `"AR"`
- `"David"` -> `"D"`
- blank input -> `"?"`
- maximum two initials

## Dropdown API

`AdaptiveDropdownMenu` is a simple Foundation menu panel containing `AdaptiveMenuItem` entries. It is intentionally minimal and inline in the current API, avoiding unanchored popups until an anchored menu contract exists.

## Not Included Yet

- `AdaptiveSelect`
- `AdaptiveMultiSelect`
- DataView v2
- Dark mode/theme system
- Icon packs
- Complex anchored menu positioning
- Massive migration of existing modules

## Future Adoption

Future PRs can gradually replace duplicated internal primitives:

- `adaptive-data`: action buttons, badges, mobile card surface, overflow menu
- `adaptive-forms`: form actions and text fields
- `adaptive-navigation`: account/menu primitives where appropriate
- `admin-demo`: remaining demo composites such as product thumbnails, KPI cards, panels, and toggle chips

B1+B2 intentionally stops before that migration to keep risk low.

## PR B3 Showcase

`admin-demo` now includes a `UI Kit` screen that depends on `:adaptive-components` and renders the real primitives in a visual smoke-test page.

The showcase exercises:

- Button variants, sizes, disabled state, and icon slots
- Icon buttons
- Badge tones
- Avatar initials, blank fallback, rounded shape, and image slot
- Surface and card containers
- Dropdown/menu items
- Text fields and search field
- Section header and divider

Capture mode accepts the full showcase:

```powershell
.\gradlew.bat :admin-demo:run --args="--capture --screen components --width 1440 --height 900 --output build/visual-captures/large/components-large-1440x900.png --delayMs 1500"
```

## PR B3.1 Section Captures

The showcase also supports focused capture screens so every component family can be reviewed without manual scrolling:

- `components`
- `components-buttons`
- `components-badges`
- `components-avatars`
- `components-cards`
- `components-dropdowns`
- `components-fields`

Batch capture supports a component-only matrix:

```powershell
.\tools\capture-admin-demo.ps1 -ComponentsOnly -OutputDir build\visual-captures-components -ZipPath build\adaptivekt-components-showcase-captures.zip
```

The standard batch capture script includes these screens in the main matrix as well.

## PR C1 Admin Demo Dogfooding

`admin-demo` now uses `:adaptive-components` outside the UI Kit showcase.

Migrated directly:

- `DemoButton` -> `AdaptiveButton`
- `DemoButtonVariant` -> `AdaptiveButtonVariant`
- `DemoAvatar` -> `AdaptiveAvatar`
- `DemoTextField` -> `AdaptiveTextField` and `AdaptiveSearchField`
- `DemoSectionTitle` -> `AdaptiveSectionHeader`
- `AccountMenuItem` -> `AdaptiveMenuItem`

Small component correction:

- `AdaptiveSearchField` no longer renders a text pseudo-icon before the placeholder. This keeps default usage from showing duplicated wording such as "Search Search employees".

Delegated or composed:

- `DemoBadge` delegates status tone rendering to `AdaptiveBadge`.
- `DemoCard` remains a KPI composite but uses `AdaptiveCard` as its surface.
- `DemoPanel` remains a demo panel composite but uses `AdaptiveCard` as its surface.

Kept intentionally:

- `DemoThumbnail`, because product thumbnails need a future shared media/thumbnail primitive.
- `DemoStatusText`, because data views already control status badge/card presentation and this helper avoids nested badge rendering.
- `DemoToggleChip`, because there is no shared chip or segmented control yet.

This PR does not migrate `adaptive-data`, `adaptive-navigation`, `adaptive-forms`, or `adaptive-feedback`.

## PR C2 Adaptive Data Adoption

`adaptive-data` now depends on `:adaptive-components` and uses shared primitives internally while preserving its public API.

Adopted by `AdaptiveDataView`:

- `AdaptiveCard` for table surfaces and generated mobile card containers
- `AdaptiveButton` for row/card action buttons
- `AdaptiveIconButton` for overflow triggers
- `AdaptiveMenuItem` for overflow menu rows
- `AdaptiveDivider` for table row dividers

Kept local for now:

- Status badge wrappers remain local because they accept arbitrary composable cell content, while `AdaptiveBadge` is text-based.
- The anchored overflow `Popup` remains local because `AdaptiveDropdownMenu` is currently inline/simple and would be a UX regression for row action menus.

No DataView public API changed in PR C2.

## Limitations

- Colors are local component defaults for now; a full theme/color-token system is future work.
- Dropdown rendering is intentionally simple and inline for B3, avoiding unanchored popups while the public API remains minimal.
- B3.1 validates the current top viewport of each focused section; deeper interaction-state screenshots are future work.
