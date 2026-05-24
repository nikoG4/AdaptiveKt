# Plan de migración a adaptive-components

## PR B: Crear :adaptive-components (stubs)
- Objetivo: Crear módulo vacío con build.gradle y stubs de componentes.
- Archivos: build.gradle.kts, package io.github.adaptivekt.components, AdaptiveButton.kt, etc.
- Módulos: core, components
- Criterio: build OK, sin dependencias cíclicas.

## PR C1: Migrar botones/badges
- Objetivo: Mover DemoButton, DefaultActionButton, badges a components.
- Archivos: AdaptiveButton.kt, AdaptiveBadge.kt
- Módulos: components, data, admin-demo
- Criterio: admin-demo y data usan AdaptiveButton/Badge, build y capturas OK.

## PR C2: Migrar avatar/card/surface
- Objetivo: Mover DemoAvatar, DemoCard, DemoPanel, surface helpers.
- Archivos: AdaptiveAvatar.kt, AdaptiveCard.kt, AdaptiveSurface.kt
- Módulos: components, admin-demo, data
- Criterio: admin-demo/data usan primitives, build y capturas OK.

## PR C3: Migrar dropdown/menu
- Objetivo: Mover DemoDropdown, menu item, overflow menu.
- Archivos: AdaptiveDropdownMenu.kt, AdaptiveMenuItem.kt
- Módulos: components, data, navigation
- Criterio: navigation/data usan primitives, build y capturas OK.

## PR C4: Migrar textfield/searchfield
- Objetivo: Mover DemoTextField, search field.
- Archivos: AdaptiveTextField.kt, AdaptiveSearchField.kt
- Módulos: components, forms, admin-demo
- Criterio: forms/demo usan primitives, build y capturas OK.

## PR C5: Limpiar duplicados
- Objetivo: Eliminar helpers duplicados en admin-demo/data/navigation/feedback.
- Archivos: varios.
- Criterio: no hay duplicados, build y capturas OK.

## PR C6: Regenerar capturas y comparar
- Objetivo: Validar visualmente que no se rompió nada.
- Archivos: capturas, manifest, report.
- Criterio: capturas OK, sin regresiones visuales.

### Para cada PR:
- Objetivo claro
- Archivos/módulos afectados
- Criterios de aceptación
- Comando gradle sugerido
- Capturas requeridas
- Riesgos: ciclos, regresiones visuales, dependencias rotas
