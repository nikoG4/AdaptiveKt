# Docs Site Hardening Baseline

## 1. Identificación
* **Base SHA**: 949826964d822c092303e58f71578640ac79a74f (squash merge del PR #15)
* **Rama de trabajo**: `feat/docs-site-production-hardening`
* **Fecha de auditoría**: Junio 26, 2026

## 2. Estado Actual del Sistema (Fase 0)

### Rutas Actuales
El sistema soporta las siguientes rutas base (vía `SiteRoute` enum/sealed classes):
* `/` (Home)
* `/docs/` (Documentación general)
* `/components/` (Catálogo de componentes)
* `/demo/app/` (Demo embebida)

### Formato actual de hash y query
* Los fragmentos (hashes) se utilizan para identificar el id del componente en `/components/` (ej. `#adaptive-button`) y los tópicos en `/docs/` (ej. `#getting-started`).
* No hay uso extensivo ni estandarizado de *query parameters* (como `?q=`, `?theme=`, `?section=`).
* La gestión actual delega al `initialSiteRoute()`, `initialSiteHash()`, `pushSiteRouteAndHash()` de Kotlin Wasm JS interop.

### Lista real de componentes (24 en total)
Extraída de `SiteComponentsPage.kt`:
1. `adaptive-theme`
2. `adaptive-button`
3. `adaptive-icon-button`
4. `adaptive-badge`
5. `adaptive-chip`
6. `adaptive-avatar`
7. `adaptive-thumbnail`
8. `adaptive-card-surface`
9. `adaptive-selection-area`
10. `adaptive-text-field`
11. `adaptive-search-field`
12. `adaptive-select`
13. `adaptive-multi-select`
14. `adaptive-form-layout`
15. `adaptive-data-view`
16. `adaptive-navigation-scaffold`
17. `adaptive-navigation-tree`
18. `adaptive-breadcrumbs`
19. `adaptive-tabs`
20. `adaptive-carousel`
21. `adaptive-accordion-dialog`
22. `feedback-states` (Agrupa: EmptyState, LoadingState, ErrorState)

*(El ID extra para "Centered modal dialog" de `adaptive-accordion-dialog` se maneja dinámicamente con prefijos)*

### Tópicos de documentación (7 en total)
Extraídos de `SiteDocsPage.kt`:
1. `getting-started`
2. `theme`
3. `responsive-navigation-behavior`
4. `layout-system`
5. `publishing`
6. `visual-verification`
7. `roadmap`

### Estado real del Clipboard
La función `requestCopyToClipboard` en `DocsUi.kt` delega el texto. Sin embargo, no expone un modelo de confirmación real de que la llamada a la API `navigator.clipboard.writeText()` o de Compose falló (Promise rejected) o si funcionó. Se asume éxito inmediatamente tras el click (`copied = true`) y luego se revierte con una demora en corutina. No detecta contextos no seguros (no-HTTPS).

### Comportamiento del Historial
Utiliza `observeHistory` para atrapar eventos `popstate`. Utiliza `pushSiteRouteAndHash` para empujar estados. No maneja casos borde como queries rotos, ni restaura apropiadamente el `theme=dark` frente a retrocesos de página.

### Comportamiento del TOC
La interacción actual en `SiteComponentsPage.kt` incluye: `onTocItemClick = { /* TOC clicks are currently for local focal feedback only; they do not touch the primary route */ }`.
Es decir, es decorativo o cambia un valor visual efímero, pero no hace scrolling al anchor real ni altera la URL.

### Breakpoints actuales
Las configuraciones en AdaptiveKt típicamente usan:
* Compact: < 600dp
* Medium: 600dp - 839dp
* Expanded: 840dp - 1149dp
* Large: 1150dp+
Esto requiere revisión minuciosa durante las correcciones responsive de Docs-site.

### Scripts y Herramientas existentes
* PowerShell: `check-docs-site-guards.ps1`, `prepare-pages-site.ps1`, `check-site-links.ps1`, `check-home-code-comparison-guards.ps1`
* Bash: `check-docs-site-guards.sh`, `check-home-code-comparison-guards.sh`
* JS: `tools/docs-site-capture/capture-home-code-comparison.js`, `generate-home-code-comparison-report.js`

### Jobs de CI Existentes (`ci.yml`)
* `changes` (filtro de paths)
* `docs-site` (WasmJS test & build + guards bash)
* `build` (Android/JVM library build)
* `ai-workspace-demo`
* `communication-suite-demo`
* `visual-artifacts` (Capturas masivas en Windows: Home Code Comparison, Component Gallery, Communication Suite, Validation logs).

---

## 3. Riesgos y Problemas Confirmados

* **Navegación / Historial**: Empujar hashes e interactuar con back/forward actualmente pierde estado. Falta soporte a queries `?q=` y `?section=`.
* **Selectores Wasm y Playwright**: La captura debe seguir interactuando vía URL interna controlada (`?capture=1`) porque Wasm no expone DOM.
* **Component Search**: Inexistente. El catálogo completo requiere scroll manual.
* **TOC y Deep-linking**: Decorativo. No hay un permalink confiable para secciones internas de un componente.
* **Responsive**: Las tablas de parámetros podrían sobrepasar (overflow) el viewport o distorsionar la UI principal en teléfonos (320px-390px). Los códigos largos (DocsCodeBlock) pueden ensanchar el contenedor superior.
* **Accesibilidad**: Focus rings, contraste en hover de cards, y descriptores visuales (`contentDescription` en IconButton) deben ser rigurosamente verificados y auditados.
* **GitHub Pages y Base Path**: Las URLs deben considerar que el deploy se hace en `/AdaptiveKt/`. Parsear URLs asumiendo la raíz `/` romperá la navegación directa en producción.

## 4. Archivos Probablemente Afectados
* `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/AdaptiveKtSiteApp.kt`
* `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/DocsUi.kt`
* `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteComponentsPage.kt`
* `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteNavigation.kt`
* `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteRouter.kt`
* Interop en `DocsSiteWasm.kt`.
* Nuevo modelo: `SiteLocation.kt`, `SiteSearch.kt`.
* Nuevo bash/ps1 guards para `navigation-guards` y visual checks extendidos en `ci.yml`.
