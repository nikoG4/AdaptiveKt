# Adaptive Forms Component Migration

## PR C3 Summary

PR C3 migrates the safest duplicated visual primitive in `:adaptive-forms` to `:adaptive-components` while preserving the public forms API.

Principle: default simple code should produce a professional admin UI.

## Dependency Change

`adaptive-forms` now depends on:

- `:adaptive-core`
- `:adaptive-layout` (existing dependency used by `AdaptiveGrid`)
- `:adaptive-components`
- Compose Foundation from the existing project setup

No dependency was added to `adaptive-components`, and no cycle was introduced.

## Audit Findings

Audited files:

- `adaptive-forms/build.gradle.kts`
- `adaptive-forms/src/commonMain/kotlin/io/github/adaptivekt/forms/AdaptiveFormLayout.kt`
- `adaptive-forms/src/commonMain/kotlin/io/github/adaptivekt/forms/AdaptiveFormTypes.kt`
- `adaptive-forms/src/commonTest/kotlin/io/github/adaptivekt/forms/AdaptiveFormHelpersTest.kt`
- Admin demo settings usage

Internal visual primitives found:

| Area | Current implementation | PR C3 decision | Reason |
|---|---|---|---|
| Section divider | Local `Box` with fixed height/background | Migrated to `AdaptiveDivider` | Safe shared primitive with equivalent behavior |
| Action buttons | User-provided composable slots | Kept | Forms does not generate buttons internally |
| Text fields | User-provided field slots | Kept | Replacing slots would change composition behavior |
| Section containers | Unframed layout with headings and fields | Kept | Wrapping in cards/surfaces would alter layout and settings screenshots |
| Validation text | Local `BasicText` styling | Kept | No shared validation-message primitive exists yet |
| Sticky compact actions | Local compact action row container | Kept | Preserves current sticky behavior and avoids layout regression |

## Migrated Helpers

- `FormSection` now uses `AdaptiveDivider` for section separation.

## Helpers Kept

- Form action slots remain caller-owned.
- Field content remains caller-owned.
- Section layout remains unframed.
- Validation message rendering remains local.
- Compact sticky action behavior remains local.

These were intentionally left in place because `AdaptiveFormLayout` is a composition framework rather than a full field/button generator.

## Public API

No public API changed.

Preserved:

- `AdaptiveFormLayout`
- `AdaptiveFormScope`
- `AdaptiveFormSectionScope`
- `AdaptiveFormActionsScope`
- `FieldSpan`
- `LabelPosition`
- `AdaptiveValidationMessage`

## Verification

Commands executed:

```powershell
.\gradlew.bat :adaptive-forms:jvmTest --console=plain --stacktrace
.\gradlew.bat :admin-demo:build --console=plain --stacktrace
.\gradlew.bat build --console=plain --stacktrace
.\tools\capture-admin-demo.ps1
```

Results:

- `:adaptive-forms:jvmTest` passed.
- `:admin-demo:build` passed.
- Full `build` passed.
- Visual captures completed: 48 screenshots.

Capture outputs:

- `build/visual-captures`
- `build/visual-captures/visual-capture-report.md`
- `build/adaptivekt-admin-demo-visual-captures.zip`

## Visual Review

Reviewed:

- `settings-compact-420x900.png`
- `settings-large-1440x900.png`
- `dashboard-large-1440x900.png`
- `components-fields-large-1440x900.png`

Observed:

- Settings compact keeps a single column and actions do not cover fields.
- Settings large preserves the existing reasonable form layout.
- Dividers remain subtle and consistent with shared component styling.
- Components showcase remains stable.

Capture runs still emit SLF4J no-binding warnings from the demo-only Kamel/Ktor image stack, but screenshots are written and Gradle tasks complete successfully.

## Risks Remaining

- `adaptive-forms` still has local validation text styling. A future shared `AdaptiveValidationMessage` could migrate this safely.
- Form actions cannot be migrated centrally until the forms module owns a default action-button API.
- Field visuals cannot be migrated centrally without a separate opt-in default field component or field factory.

## Next Recommended PR

PR C4: controlled migration of `adaptive-feedback` visual primitives to `:adaptive-components`, focusing on buttons, surfaces, dividers, and embedded functional icons where feedback states own the visuals.
