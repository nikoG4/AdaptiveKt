# Diseño de AdaptiveSelect y AdaptiveMultiSelect

## API simple

```kotlin
AdaptiveSelect(
    label = "Language",
    options = languages,
    selectedOption = selectedLanguage,
    optionLabel = { it.name },
    onSelectedOptionChange = { selectedLanguage = it }
)
```

## API avanzada con slots

```kotlin
AdaptiveSelect(
    label = "Employee",
    options = employees,
    selectedOption = selectedEmployee,
    optionLabel = { it.name },
    onSelectedOptionChange = { selectedEmployee = it },
    optionContent = { employee ->
        // avatar + name + role + badge, etc.
    },
    selectedContent = { employee ->
        // custom selected representation
    }
)
```

## Requisitos de diseño
- itemContent custom
- selectedContent custom
- imagen/avatar/badge en opción
- búsqueda local
- empty state
- clear button
- disabled
- validation
- multi-select con chips
- futuro async search (fuera de scope)
