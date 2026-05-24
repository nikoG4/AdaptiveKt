# Arquitectura de adaptive-components

## Módulo propuesto: :adaptive-components

- Depende de: `:adaptive-core`
- NO debe depender de: navigation, data, forms, feedback, admin-demo

### Contenido mínimo propuesto
- AdaptiveButton
- AdaptiveIconButton
- AdaptiveBadge
- AdaptiveAvatar
- AdaptiveDropdownMenu
- AdaptiveMenuItem
- AdaptiveCard
- AdaptiveSurface
- AdaptiveTextField
- AdaptiveSearchField
- AdaptiveSectionHeader
- AdaptiveDivider

### Módulos consumidores
- adaptive-navigation
- adaptive-data
- adaptive-forms
- adaptive-feedback
- admin-demo

### Diagrama de dependencias permitidas

```mermaid
graph TD
  core[adaptive-core]
  components[adaptive-components]
  navigation[adaptive-navigation]
  data[adaptive-data]
  forms[adaptive-forms]
  feedback[adaptive-feedback]
  demo[admin-demo]

  core --> components
  components --> navigation
  components --> data
  components --> forms
  components --> feedback
  components --> demo
  navigation --> demo
  data --> demo
  forms --> demo
  feedback --> demo
```

- adaptive-components NO debe depender de ningún consumidor.
- No debe haber ciclos.
- Los helpers visuales deben ser internal salvo API pública documentada.
- Los tokens de color/spacing/radius deben venir de adaptive-core.
