# AdaptiveKt — Responsive Forms

## Purpose
Adaptive forms in v0.1-alpha provide a predictable responsive form layout for Compose Multiplatform apps.

The API lets developers express mobile-first forms while enabling desktop multi-column structure.

## Core form API

### AdaptiveFormLayout

A responsive form container that adapts field placement based on breakpoint and developer preferences.

```kotlin
@Composable
fun AdaptiveFormLayout(
    modifier: Modifier = Modifier,
    columns: AdaptiveFormColumns = AdaptiveFormColumns.Auto,
    labelPosition: LabelPosition = LabelPosition.Top,
    sectionSpacing: Dp = AdaptiveTokens.Spacing.Medium,
    stickyActionsOnCompact: Boolean = true,
    content: @Composable AdaptiveFormScope.() -> Unit,
)
```

### AdaptiveFormScope

A form builder scope that exposes sections, fields, and action slots.

```kotlin
interface AdaptiveFormScope {
    fun section(
        title: String,
        description: String? = null,
        content: @Composable AdaptiveFormSectionScope.() -> Unit,
    )

    fun actions(
        content: @Composable AdaptiveFormActionsScope.() -> Unit,
    )
}

interface AdaptiveFormSectionScope {
    fun field(
        label: String,
        fieldSpan: FieldSpan = FieldSpan.Full,
        labelPosition: LabelPosition? = null,
        validationMessage: ValidationMessage? = null,
        content: @Composable () -> Unit,
    )
}

interface AdaptiveFormActionsScope {
    fun primary(content: @Composable () -> Unit)
    fun secondary(content: @Composable () -> Unit)
}
```

### FormSection / FormField / FormActions

Within `AdaptiveFormLayout`, developers build forms using `section {}`, `field {}`, and `actions {}`.

```kotlin
AdaptiveFormLayout {
    section(title = "Empleado") {
        field(label = "Nombre", fieldSpan = FieldSpan.Full) {
            TextField(...)
        }
    }

    actions {
        primary { Button(onClick = save) { Text("Guardar") }
        secondary { TextButton(onClick = cancel) { Text("Cancelar") }
    }
}
```

### FieldSpan

Defines width occupancy in a multi-column form.

```kotlin
sealed interface FieldSpan {
    object Full : FieldSpan
    object Half : FieldSpan
    object Third : FieldSpan
    object TwoThirds : FieldSpan
    data class Columns(val count: Int) : FieldSpan
}
```

### AdaptiveFormColumns

Controls the target column layout.

```kotlin
enum class AdaptiveFormColumns {
    Auto,
    One,
    Two,
    Three,
}
```

### LabelPosition

Controls the label location relative to field content.

```kotlin
enum class LabelPosition {
    Top,
    Inline,
}
```

### ValidationMessage

Encapsulates inline field validation.

```kotlin
data class ValidationMessage(
    val message: String,
    val isError: Boolean = true,
)
```

## Responsive behavior

### Compact

- Single-column layout by default.
- Labels are rendered above fields using `LabelPosition.Top`.
- `FormActions` can stay sticky at the bottom of the screen when `stickyOnCompact = true`.

### Medium

- Two-column forms are supported.
- `field(fieldSpan = FieldSpan.Half)` yields side-by-side inputs.
- `LabelPosition` may remain `Top` or switch to `Inline` for compactness.

### Expanded / Large

- Three-column or wider layouts are supported when `columns = AdaptiveFormColumns.Auto`.
- `FieldSpan.TwoThirds` can be used for larger inputs.
- Inline labels are acceptable for dense forms.

## Concrete examples

### Full-width field

```kotlin
AdaptiveFormLayout {
    section(title = "Datos personales") {
        field(label = "Nombre completo", fieldSpan = FieldSpan.Full) {
            TextField(value = fullName, onValueChange = onChange)
        }
    }
}
```

### Two-column desktop field

```kotlin
AdaptiveFormLayout {
    section(title = "Empleado") {
        field(label = "Departamento", fieldSpan = FieldSpan.Half) {
            DropdownMenu(...)
        }
        field(label = "Cargo", fieldSpan = FieldSpan.Half) {
            TextField(...)
        }
    }
}
```

### Label behavior by breakpoint

```kotlin
AdaptiveContent {
    val adaptiveLabel = adaptiveValue(
        compact = LabelPosition.Top,
        medium = LabelPosition.Top,
        expanded = LabelPosition.Inline,
        large = LabelPosition.Inline,
    )

    AdaptiveFormLayout(labelPosition = adaptiveLabel) {
        section(title = "Contacto") {
            field(label = "Email") { ... }
        }
    }
}
```

### Inline validation error

```kotlin
AdaptiveFormLayout {
    section(title = "Contacto") {
        field(
            label = "Correo electrónico",
            validationMessage = ValidationMessage("Formato inválido")
        ) {
            TextField(...)
        }
    }
}
```

The validation message is displayed below the field content in compact and expanded layouts.

### Mobile sticky submit footer

```kotlin
AdaptiveFormLayout {
    section(title = "Enviar") {
        field(label = "Comentarios") {
            TextField(...)
        }
    }

    actions {
        primary { Button(onClick = save) { Text("Guardar") } }
        secondary { TextButton(onClick = cancel) { Text("Cancelar") } }
    }
}
```

On `Compact`, the action row remains visible at the bottom of the screen while form content scrolls.

## Developer guidance

- Use `FieldSpan.Full` for fields that should span the entire form width.
- Use `FieldSpan.Half` for two-column groupings.
- Use `FieldSpan.TwoThirds` for larger controls in three-column layouts.
- Use `AdaptiveFormColumns.Auto` to let the form decide the best column count based on breakpoint.
- Keep `labelPosition` set to `Top` on compact screens for readability.
