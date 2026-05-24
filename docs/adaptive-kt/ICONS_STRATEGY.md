# Estrategia de iconos en AdaptiveKt

## Principios
- No se inventa icon pack propio.
- Los componentes aceptan iconos como slot composable:
  `icon: (@Composable () -> Unit)? = null`
- Fallback glyph (inicial) si icon == null.
- No dependencia obligatoria de Font Awesome ni otro pack.
- admin-demo puede usar un icon pack Compose si compila bien.
- Futuro: módulo opcional :adaptive-icons-fontawesome o :adaptive-icons-lucide.

## Ejemplo de uso

```kotlin
AdaptiveButton(
    text = "Save",
    icon = { FontAwesomeIcon(name = "save") }
)

AdaptiveNavItem(
    id = "dashboard",
    label = "Dashboard",
    icon = { LucideIcon(name = "layout-dashboard") }
)

// Si icon == null, se muestra la inicial del label como fallback.
```

## Reglas
- adaptive-core y adaptive-components no dependen de icon packs.
- Los icon packs deben ser opcionales y desacoplados.
- Documentar en README y ejemplos.
