# Next Work Queue (Cola de Tareas)

## Post SITE-3

- [x] Add local JDK 17 environment diagnostics and setup docs.
- [x] Add AdaptiveTheme light/default foundation.
- [x] T2: migrate data/forms/navigation/feedback internals to theme tokens where safe.
- [x] T3: add dark-mode color scheme after light migration is stable.
- [ ] T4: evaluate optional platform/brand presets.
- [ ] Add async/server-backed search guidance for select-style components.
- [ ] Evaluate virtualized option lists for very large select datasets.
- [ ] Add optional docs-site visual diff baselines after the first Pages deployment.
- [ ] Add a non-blocking scheduled link check for deployed Pages URLs.
- [x] Add manual macOS validation workflow for local publishing.
- [x] Run manual macOS validation workflow and inspect iOS artifacts.
- [ ] Add conditional signing and manual publish workflow documentation for PUBLISH-1B.
- [x] Add `AdaptiveCarousel` and `AdaptiveNavigationTree` primitives.
- [x] Add UI Kit showcase coverage for carousel and navigation tree.
- [x] Add carousel transition animations and animated feedback loading indicators.
- [x] Fix mobile admin-demo scroll and switch sidebar defaults to compact pill navigation.
- [ ] Add deeper visual regression baselines for new dark hover states.
- [ ] Consider autoplay for carousel only if a future product use case needs it.
- [ ] Consider `AdaptiveBreadcrumbs` or `AdaptiveAccordion` in a later UI navigation polish PR.
- [ ] Reduce docs-site Wasm bundle size only if it becomes a practical hosting/performance issue.
- [ ] Consider a small responsive navigation refinement for very narrow docs-site widths.
- [ ] Add Maven publishing setup in a dedicated publishing PR.

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
- [x] Agregar `AdaptiveIcons` funcionales embebidos en PR C2.2
- [x] Reemplazar glyphs obvios en components showcase, SearchField, data overflow y demo
- [x] Rescatar PR D1 y agregar remote images demo-only para employees/products
- [x] Documentar D1 en `ADMIN_DEMO_REMOTE_IMAGES.md`

### PR C3 recomendado

- [x] Agregar dependencia controlada `adaptive-forms -> adaptive-components`
- [x] Migrar divider interno de secciones a `AdaptiveDivider`
- [x] Mantener `AdaptiveFormLayout` API estable
- [x] Documentar por que acciones y fields se mantienen como slots del usuario
- [x] No migrar navigation/feedback en el mismo PR
- [x] No implementar Select todavia

### PR C4 recomendado

- [x] Migrar primitives visuales seguros de `adaptive-feedback` a `adaptive-components`
- [x] Usar `AdaptiveSurface` para Empty/Loading/Error sin cambiar API
- [x] Usar `AdaptiveIcons` para glyphs default de Empty/Error
- [x] Mantener acciones como slots del usuario
- [x] Mantener API publica de feedback
- [x] Agregar capturas `invoices-empty`, `invoices-loading`, `invoices-error`
- [x] No tocar navigation/forms/data en el mismo PR

### PR C5 recomendado

- [ ] Migrar primitives visuales seguros de `adaptive-navigation` a `adaptive-components`
- [ ] Evaluar `AdaptiveSurface`/`AdaptiveDivider` para paneles de navigation sin cambiar API
- [x] Evaluar `AdaptiveIcons` para affordances internas de navigation
- [ ] Mantener API publica de navigation
- [ ] No tocar data/forms/feedback en el mismo PR

### Data follow-ups futuros

- [ ] Diseñar un badge compartido con content slot o estrategia segura para celdas composables
- [ ] Diseñar `AdaptiveDropdownMenu` anclado antes de reemplazar popups de DataView
- [ ] Revisar hover/pressed visual de row actions con captura manual o tooling futuro

### Select-style components

- [x] Implementar `AdaptiveSelect<T>`
- [x] Implementar `AdaptiveMultiSelect<T>`
- [ ] Investigar busqueda async/server-backed
- [ ] Evaluar virtualizacion de menus grandes
- [ ] **NO implementar aún**

### DataView v2

- [ ] Investigar AdaptiveDataAction<T>
- [ ] Investigar primary/secondary/overflow
- [ ] Investigar desktop vs mobile actions
- [ ] Documentar mejoras a cardContent
- [ ] **NO implementar aún**

### Theme/dark mode foundation

- [x] Implementar AdaptiveTheme base
- [x] Implementar AdaptiveColorScheme light/default
- [x] Documentar tokens light y alcance dark futuro
- [x] Identificar colores hardcodeados por migrar
- [x] Migrar data/forms/navigation/feedback en T2
- [ ] Implementar dark mode en T3

### UI polish basado en capturas

- [ ] Revisar capturas existentes
- [ ] Identificar inconsistencias visuales
- [ ] Listar mejoras estéticas posibles
- [ ] **NO implementar aún**

---

## AUDIT-1 Completado (2026-05-30)
===================================================

- [x] Ejecutar auditoría de estado del proyecto
- [x] Generar reporte en PROJECT_READINESS_AUDIT.md
- [x] Actualizar PROGRESS_LOG.md con hallazgos
- [x] Actualizar NEXT_WORK_QUEUE.md con próximos pasos

### PUBLISH-0A Corrección (2026-05-30)
- [x] Corregir `PROJECT_READINESS_AUDIT.md`.
- [x] Confirmar que `.github/workflows/ci.yml` existe.
- [x] Confirmar que `.github/workflows/pages.yml` existe.
- [x] Documentar que `admin-demo` y `docs-site` permanecen en `settings.gradle.kts`.
- [x] Documentar que `admin-demo` y `docs-site` quedan fuera solo de Maven publishing.
- [x] Crear `LICENSE` Apache-2.0.
- [x] Crear `CHANGELOG.md` inicial.
- [x] Crear `docs/publishing/MAVEN_CENTRAL_READINESS.md`.

### P0 — Bloqueantes reales a resolver antes de PUBLISH-0:
- [x] Crear LICENSE file (Apache 2.0)
- [x] Crear CHANGELOG.md inicial
- [x] Definir una estrategia única de versionado (`GROUP`/`VERSION_NAME` en `gradle.properties`)
- [x] Definir groupId/artifactIds finales para dry-run local
- [x] Definir estrategia de publishing para módulos publicables (`maven-publish` nativo)

### P1 — Antes de Maven Central:
- [x] Confirmar GitHub Actions CI y Pages existentes
- [x] Agregar dry-run local de Maven publishing
- [x] Configurar POM metadata básica
- [x] Agregar workflow manual seguro para validar publishing local en macOS
- [x] Documentar validación macOS/iOS y plan de signing sin secretos
- [ ] Confirmar artifacts de sources/docs para Maven Central real
- [ ] Documentar estrategia de tags de release
- [ ] Configurar signing y secrets
- [ ] Agregar workflow manual de publicación remota

### P2 — Mejoras recomendadas:
- [ ] Agregar coverage thresholds a CI
- [ ] Documentar iOS targets requieren macOS
- [ ] Agregar audit de dependencia de licencias

### P3 — Nice to have:
- [ ] Agregar GitHub Actions badge en README
- [ ] Agregar coverage badge

### Próximo paso inmediato:
PUBLISH-1B/PUBLISH-2: ejecutar validación macOS manual, definir estrategia de sources/docs jars, signing condicional y workflow manual de publicación remota sin ejecución automática.

### PUBLISH-0B Completado (2026-05-30)
- [x] Definir `GROUP=io.github.nikog4.adaptivekt`.
- [x] Definir `VERSION_NAME=0.1.0-alpha01`.
- [x] Configurar `maven-publish` solo en módulos de librería.
- [x] Mantener `admin-demo` y `docs-site` dentro del build pero fuera de publishing.
- [x] Agregar repositorio local de dry-run en `build/local-maven`.
- [x] Agregar tarea agregada `publishAllPublicationsToLocalTestRepository`.
- [x] Documentar `docs/publishing/LOCAL_PUBLISHING.md`.

### PUBLISH-0C Completado (2026-05-30)
- [x] Crear `tools/verify-local-publishing-consumer.ps1`.
- [x] Crear consumer temporal en `build/local-consumer-smoke`.
- [x] Consumir `build/local-maven` como repositorio Maven.
- [x] Usar coordenadas Maven para los 7 módulos publicables.
- [x] Verificar `compileKotlinJvm`.
- [x] Verificar `compileKotlinWasmJs`.
- [x] Confirmar que el consumer no usa `project(":...")` ni `includeBuild`.

### PUBLISH-1A Completado (2026-05-30)
- [x] Auditar tareas de publishing disponibles en Windows.
- [x] Documentar que Windows valida metadata, JVM, Android release y Wasm JS.
- [x] Documentar que iOS requiere macOS.
- [x] Crear `docs/publishing/IOS_MACOS_VALIDATION.md`.
- [x] Crear workflow manual `.github/workflows/publishing-validation.yml`.
- [x] Mantener el workflow sin secrets, sin remoto, sin release y sin tag.
- [x] Documentar plan futuro de signing condicional.

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
## Docs Site Follow-Ups

- Add Playwright smoke screenshots for `:docs-site`.
- Add link checking for generated Pages artifacts.
- Add visual diff coverage for the docs landing and component catalog.
- Consider a lightweight Markdown rendering path for selected `docs/components/*.md` files.
