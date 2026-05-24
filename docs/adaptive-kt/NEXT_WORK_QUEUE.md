# Next Work Queue (Cola de Tareas)

**Fecha:** 2026-05-22

## Propósito

Lista ordenada de próximas tareas separadas por herramienta. Solo documentación y análisis. No UI compleja ni cambios visuales en esta fase.

---

## Para Copilot

### Tareas de documentación

- [ ] Actualizar `COMPONENT_CATALOG.md` con :adaptive-components
- [ ] Actualizar `ADMIN_DEMO.md` con referencias a nuevos componentes
- [ ] Actualizar `DESIGN_DEFAULTS.md` con tokens de color sugeridos
- [ ] Documentar API de :adaptive-components en README

### Inventario

- [ ] Documentar todos los componentes del inventario en COMPONENT_CATALOG.md
- [ ] Crear tabla de dependencias actualizadas
- [ ] Listar helpers duplicados identificados

### Tests simples

- [ ] Verificar :adaptive-components:jvmTest pasa con stubs
- [ ] Ejecutar :adaptive-data:jvmTest
- [ ] Ejecutar :admin-demo:build

### Scripts

- [ ] Verificar que `tools/capture-admin-demo.ps1` sigue funcionando
- [ ] Documentar comando de captura en README
- [ ] No escribir scripts complejos (fuera de alcance)

---

## Para Claude Code (local/qwen)

### Auditorías

- [ ] Revisar todos los colores hardcodeados en AdaptiveDataView
- [ ] Identificar duplicados de componentes visuales
- [ ] Auditoría de consistencia de spacing entre componentes
- [ ] Verificar que no hay magic numbers en layout

### Documentación

- [ ] Escribir README para :adaptive-components
- [ ] Documentar API pública en COMPONENT_CATALOG.md
- [ ] Crear guías de migración para cada PR
- [ ] Documentar decisiones de diseño en ADRs

### Análisis de dependencias

- [ ] Verificar que no hay ciclos dependientes
- [ ] Listar todos los imports entre módulos
- [ ] Documentar grafo de dependencias actual
- [ ] Identificar dependencias ocultas

### Colores hardcodeados

- [ ] Listar todos los colores en AdminDemoUi.kt
- [ ] Listar todos los colores en AdaptiveDataView.kt
- [ ] Crear tabla de colores por módulo
- [ ] Priorizar tokens a migrar primero

### Tareas lentas pero seguras

- [ ] Leer todos los ADRs existentes
- [ ] Revisar todos los tests existentes
- [ ] Auditar uso de tokens en código
- [ ] Verificar consistencia de breakpoints

---

## Para Codex (cuando esté disponible)

### :adaptive-components real

- [x] Crear `adaptive-components/build.gradle.kts`
- [x] Crear package `io.github.adaptivekt.components`
- [x] Agregar componentes B1+B2 funcionales
- [x] Verificar build exitoso
- [x] Crear documentación básica en `ADAPTIVE_COMPONENTS_API.md`
- [x] Agregar pantalla `UI Kit` en admin-demo para PR B3
- [x] Agregar captura `--screen components`
- [x] Agregar capturas enfocadas `components-*` para PR B3.1
- [x] Agregar `-ComponentsOnly` al capture script
- [x] Migrar primitives duplicados de admin-demo hacia :adaptive-components en PR C1
- [x] Documentar helpers migrados y helpers mantenidos en `ADMIN_DEMO_COMPONENT_MIGRATION.md`
- [x] Migrar primitives internos de adaptive-data hacia :adaptive-components en PR C2
- [x] Documentar migración de adaptive-data en `ADAPTIVE_DATA_COMPONENT_MIGRATION.md`

### PR C3 recomendado

- [ ] Migrar botones de acciones de `adaptive-forms` a `AdaptiveButton`
- [ ] Evaluar uso de `AdaptiveTextField` en defaults/documentación de forms sin romper slots existentes
- [ ] Mantener `AdaptiveFormLayout` API estable
- [ ] No migrar navigation/feedback en el mismo PR
- [ ] No implementar Select todavía

### Data follow-ups futuros

- [ ] Diseñar un badge compartido con content slot o estrategia segura para celdas composables
- [ ] Diseñar `AdaptiveDropdownMenu` anclado antes de reemplazar popups de DataView
- [ ] Revisar hover/pressed visual de row actions con captura manual o tooling futuro

### AdaptiveSelect/MultiSelect (futuro)

- [ ] Investigar API de select compositional
- [ ] Documentar requerimientos
- [ ] Listar casos de uso
- [ ] **NO implementar aún**

### DataView v2

- [ ] Investigar AdaptiveDataAction<T>
- [ ] Investigar primary/secondary/overflow
- [ ] Investigar desktop vs mobile actions
- [ ] Documentar mejoras a cardContent
- [ ] **NO implementar aún**

### Theme/dark mode foundation

- [ ] Investigar AdaptiveTheme
- [ ] Investigar AdaptiveColorScheme
- [ ] Documentar tokens light/dark
- [ ] Identificar colores hardcodeados por migrar
- [ ] **NO implementar aún**

### UI polish basado en capturas

- [ ] Revisar capturas existentes
- [ ] Identificar inconsistencias visuales
- [ ] Listar mejoras estéticas posibles
- [ ] **NO implementar aún**

---

## Resumen por Herramienta

| Herramienta | Qué hacer | Qué NO hacer |
|---|---|----|
| **Copilot** | Docs, inventarios, tests, scripts | UI compleja, cambios visuales |
| **Claude Code** | Auditorías, docs, análisis, colors | Cambios de UI, commits grandes |
| **Codex** | :adaptive-components, Select, theme, UI polish | Sin explicit approval |

---

## Próximos Pasos Inmediatos

### 1. Validar build actual
```bash
.\gradlew.bat :adaptive-core:build
.\gradlew.bat :adaptive-layout:build
.\gradlew.bat :adaptive-navigation:build
.\gradlew.bat :adaptive-forms:build
.\gradlew.bat :adaptive-data:build
.\gradlew.bat :adaptive-feedback:build
.\gradlew.bat :admin-demo:build
```

### 2. Verificar tests
```bash
.\gradlew.bat test
```

### 3. Capturar matriz visual actual
```bash
.\gradlew.bat :admin-demo:captureVisuals
```

### 4. Revisar capturas
- Abrir `build/visual-captures/`
- Leer `build/visual-captures/visual-capture-report.md`
- Verificar manifest en `build/visual-captures/manifest.json`

### 5. Documentar hallazgos
- Actualizar `COMPONENT_CATALOG.md`
- Actualizar `ADMIN_DEMO.md`
- Verificar que todos los docs nuevos existen

---

## Riesgos de Próximas Tareas

| Tarea | Riesgo | Mitigación |
|------|--------|-----------|
| :adaptive-components | Ciclos dependientes | Solo dependa de :adaptive-core |
| Colores hardcodeados | Visual inconsistency | Documentar, no corregir aún |
| Tests existentes | Tests incompletos | Ejecutar primero, documentar |
| Build actual | Tarda mucho | No insistir si falla |

---

## Notas

- **No implementes código funcional todavía**
- **No hagas commits**
- **No cambies UI**
- **Solo documentación y auditoría**
- **Mantener simple**
- **Priorizar tareas seguras**
