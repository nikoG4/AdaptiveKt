# Diseño de acciones declarativas en AdaptiveDataView

## Modelos
- AdaptiveDataAction<T>
- AdaptiveActionPriority (Primary, Secondary, Overflow, Destructive)

## Desktop
- Acciones visibles: primary/secondary
- Overflow: acciones menos usadas o destructivas
- No saturar la tabla de botones azules

## Mobile
- Card: primary visible, overflow en menú
- No mostrar todos los botones en mobile

## Ejemplo

```kotlin
AdaptiveDataAction(
    id = "edit",
    label = "Edit",
    icon = { ... },
    priority = AdaptiveActionPriority.Secondary,
    destructive = false,
    onClick = { item -> edit(item) }
)

AdaptiveDataView(
    actions = listOf(...)
)
```

## Reglas
- Destructive va en overflow o separado
- No repetir botones en mobile
- Ejemplos: Employees/Products/Invoices
