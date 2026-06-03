# Inventario de Primitives Visuales de AdaptiveKt

**Fecha:** 2026-05-22

## Objetivo

Documentar todos los componentes, helpers y composites visuales existentes en AdaptiveKt para planificar la futura creación de :adaptive-components.

---

## Tabla Inventario

| Helper/componente | Archivo | Módulo | Público/Internal/Demo | Qué hace | Posible destino futuro |
|---|---|----|----|---|----|
| **AdaptiveButton** (futuro) | - | - | - | - | :adaptive-components |
| DemoButton | admin-demo/ui/AdminDemoUi.kt | admin-demo | Public/Demo | Botón pill con variants Primary/Secondary/Danger, estados hover/pressed/indication | :adaptive-components -> AdaptiveButton |
| DefaultActionButton | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Acción de tabla (primary/secondary), shape-safe interaction states | :adaptive-components -> AdaptiveButton |
| DemoToggleChip | admin-demo/ui/AdminDemoUi.kt | admin-demo | Demo | Toggle chip con estado seleccionado/border | :adaptive-components -> AdaptiveToggleChip |
| AccountMenuItem | admin-demo/ui/AdminDemoUi.kt | admin-demo | Demo | Item de menú de cuenta | :adaptive-components -> AdaptiveMenuItem |
| **AdaptiveIconButton** (futuro) | - | - | - | - | :adaptive-components |
| **AdaptiveBadge** | - | - | - | - | :adaptive-components |
| DemoBadge | admin-demo/ui/AdminDemoUi.kt | admin-demo | Public/Demo | Badge con tone mapping (Success/Warning/Danger/Info/Neutral) | :adaptive-components -> AdaptiveBadge |
| DemoStatusText | admin-demo/ui/AdminDemoUi.kt | admin-demo | Demo | Texto con estado como badge inline (usado en KPI cards) | :adaptive-components -> AdaptiveStatusBadge |
| DefaultStatusBadge | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Badge por defecto inferido por column status | :adaptive-components -> AdaptiveStatusBadge |
| **AdaptiveAvatar** | - | - | - | - | :adaptive-components |
| DemoAvatar | admin-demo/ui/AdminDemoUi.kt | admin-demo | Public/Demo | Avatar con initials de nombre, color, size | :adaptive-components -> AdaptiveAvatar |
| **AdaptiveCard** | - | - | - | - | :adaptive-components |
| DemoCard | admin-demo/ui/AdminDemoUi.kt | admin-demo | Public/Demo | Card KPI con title/value/subtitle/indicator | :adaptive-components -> AdaptiveKpiCard |
| DemoPanel | admin-demo/ui/AdminDemoUi.kt | admin-demo | Demo | Panel con title/subtitle/content | :adaptive-components -> AdaptiveCard |
| DefaultMobileCardContent | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Card generado en móvil por AdaptiveDataView | :adaptive-components -> AdaptiveCard |
| AdaptiveDataView cardContent slot | adaptive-data/AdaptiveDataView.kt | adaptive-data | API Pública | Composable opcional para contenido de card | :adaptive-components (mover implementación interna) |
| **AdaptiveSurface** | - | - | - | - | :adaptive-components |
| DataSurface, DataBorder | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Tokens de color para data surface | :adaptive-core -> Tokens (expandir) |
| **AdaptiveTextField** | - | - | - | - | :adaptive-components |
| DemoTextField | admin-demo/ui/AdminDemoUi.kt | admin-demo | Public/Demo | TextField con placeholder, border, clip, height | :adaptive-components -> AdaptiveTextField |
| BasicText wrapper | admin-demo/ui/AdminDemoUi.kt | admin-demo | Internal | Wrapper BasicText con emphasis styles | :adaptive-components (si es útil reutilizar) |
| **AdaptiveSearchField** (futuro) | - | - | - | - | :adaptive-components |
| DemoTextField usado como search | admin-demo/screens/*.kt | admin-demo | Demo | Uso de DemoTextField como filtro de texto | :adaptive-components -> AdaptiveSearchField |
| **AdaptiveSelect** (futuro) | - | - | - | - | :adaptive-components (futuro PR Select) |
| **AdaptiveMultiSelect** (futuro) | - | - | - | - | :adaptive-components (futuro PR Select) |
| **AdaptiveDropdownMenu** | - | - | - | - | :adaptive-components |
| AccountMenu | admin-demo/ui/AdminDemoUi.kt | admin-demo | Demo | Dropdown menu con avatar/title/actions | :adaptive-components -> AdaptiveDropdownMenu |
| DefaultOverflowMenu | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Overflow menu popover en tabla | :adaptive-components -> AdaptiveOverflowMenu |
| **AdaptiveMenuItem** | - | - | - | - | :adaptive-components |
| **AdaptiveSectionHeader** | - | - | - | - | :adaptive-components |
| DemoSectionTitle | admin-demo/ui/AdminDemoUi.kt | admin-demo | Public/Demo | Sección title grande bold | :adaptive-components -> AdaptiveSectionHeader |
| InfoRow | admin-demo/ui/AdminDemoUi.kt | admin-demo | Demo | Row label/value (ajuste en settings) | :adaptive-components -> AdaptiveInfoRow (opcional) |
| **AdaptiveDivider** (futuro) | - | - | - | - | :adaptive-components |
| **AdaptiveContainer** | adaptive-layout/AdaptiveContainer.kt | adaptive-layout | API Pública | Container centrado que adapta breakpoint | adaptive-layout (quedar) |
| **AdaptiveGrid** | adaptive-layout/AdaptiveGrid.kt | adaptive-layout | API Pública | Grid adaptativo con fillMaxWidth(fraction) | adaptive-layout (quedar) |
| AdaptiveDataView | adaptive-data/AdaptiveDataView.kt | adaptive-data | API Pública | Table/Card responsive con states/acciones/colunas | adaptive-data (quedar) |
| AdaptiveEmptyState | adaptive-feedback/AdaptiveEmptyState.kt | adaptive-feedback | API Pública | Empty state con title/description/icon/action | adaptive-feedback (quedar) |
| AdaptiveLoadingState | adaptive-feedback/AdaptiveLoadingState.kt | adaptive-feedback | API Pública | Loading state con border circle indicator | adaptive-feedback (quedar) |
| AdaptiveErrorState | adaptive-feedback/AdaptiveErrorState.kt | adaptive-feedback | API Pública | Error state con title/description/icon/retry | adaptive-feedback (quedar) |
| FeedbackStateLayout | adaptive-feedback/FeedbackStateLayout.kt | adaptive-feedback | Internal | Layout compartido para feedback states | adaptive-feedback (quedar) |
| SimpleLoadingIndicator | adaptive-feedback/AdaptiveLoadingState.kt | adaptive-feedback | Internal | Indicador de carga simple con border | adaptive-feedback (quedar) |
| SimpleText | adaptive-feedback/AdaptiveEmptyState.kt | adaptive-feedback | Internal | Wrapper BasicText para reutilización | adaptive-feedback (quedar) |
| AdaptiveDataColumn.cell | adaptive-data/AdaptiveDataView.kt | adaptive-data | API Pública | Slot por columna en AdaptiveDataView | adaptive-data (quedar) |
| AdaptiveFilterSlot | adaptive-data/AdaptiveDataTypes.kt | adaptive-data | API Pública (typealias) | Slot opcional para filtro en DataView | adaptive-data (quedar) |
| AdaptiveActionsSlot | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Slot de acciones en toolbar | adaptive-data (quedar) |
| **AdaptiveDataAction** | adaptive-data/AdaptiveDataTypes.kt | adaptive-data | API Pública | Acción de fila con priority/label/destructive | adaptive-data (quedar) |
| WeightedDataRow | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Layout composable con distribución proporcional | adaptive-data (quedar/internal) |
| DataCellBox | adaptive-data/AdaptiveDataView.kt | adaptive-data | Internal | Helper Box con padding end | adaptive-data (quedar/internal) |
| **AdaptiveDataMobileRole** | adaptive-data/AdaptiveDataTypes.kt | adaptive-data | API Pública (enum) | Enum de roles para columnas en mobile card | adaptive-data (quedar) |
| **AdaptiveActionPriority** | adaptive-data/AdaptiveDataTypes.kt | adaptive-data | API Pública (enum) | Enum de prioridades para acciones de fila | adaptive-data (quedar) |
| **AdaptiveNavItem** | adaptive-navigation/AdaptiveNavItem.kt | adaptive-navigation | API Pública | Item de navegación con id/label/icon slot | adaptive-navigation (quedar) |
| AdaptiveNavigationMode | adaptive-navigation/AdaptiveNavigationMode.kt | adaptive-navigation | API Pública (enum) | Enum de modos de navegación por breakpoint | adaptive-navigation (quedar) |
| AdaptiveNavigationScaffold | adaptive-navigation/AdaptiveNavigationScaffold.kt | adaptive-navigation | API Pública | Scaffold que elige nav chrome por breakpoint | adaptive-navigation (quedar) |
| Drawer | adaptive-navigation/NavigationSurfaces.kt | adaptive-navigation | API Pública | Drawer compacto con overlay dim | adaptive-navigation (quedar) |
| BottomNavigation | adaptive-navigation/NavigationSurfaces.kt | adaptive-navigation | API Pública | Bottom nav horizontal en compact | adaptive-navigation (quedar) |
| NavigationRail | adaptive-navigation/NavigationSurfaces.kt | adaptive-navigation | API Pública | Rail vertical con glyph fallback | adaptive-navigation (quedar) |
| Sidebar | adaptive-navigation/NavigationSurfaces.kt | adaptive-navigation | API Pública | Sidebar con header/app bar | adaptive-navigation (quedar) |
| **AdaptiveFormLayout** | adaptive-forms/AdaptiveFormLayout.kt | adaptive-forms | API Pública | Layout de formulario responsivo | adaptive-forms (quedar) |
| AdaptiveFormScope | adaptive-forms/AdaptiveFormLayout.kt | adaptive-forms | API Pública (interface) | Scope para definir formularios | adaptive-forms (quedar) |
| AdaptiveFormSectionScope | adaptive-forms/AdaptiveFormLayout.kt | adaptive-forms | API Pública (interface) | Scope para secciones | adaptive-forms (quedar) |
| AdaptiveFormActionsScope | adaptive-forms/AdaptiveFormLayout.kt | adaptive-forms | API Pública (interface) | Scope para acciones | adaptive-forms (quedar) |
| FieldSpan | adaptive-forms/AdaptiveFormTypes.kt | adaptive-forms | API Pública (enum) | Span para campos (Full/Half/Third/TwoThirds/Columns) | adaptive-forms (quedar) |
| LabelPosition | adaptive-forms/AdaptiveFormTypes.kt | adaptive-forms | API Pública (enum) | Posición de labels (Top/Start) | adaptive-forms (quedar) |
| ValidationMessage | adaptive-forms/AdaptiveFormTypes.kt | adaptive-forms | API Pública | Mensaje de validación | adaptive-forms (quedar) |
| ValidationMessageType | adaptive-forms/AdaptiveFormTypes.kt | adaptive-forms | API Pública (enum) | Tipo de mensaje (Error/Warning/Info) | adaptive-forms (quedar) |
| AdaptiveFormColumns | adaptive-forms/AdaptiveFormTypes.kt | adaptive-forms | API Pública | Helper de columns por breakpoint | adaptive-forms (quedar) |
| columnsForBreakpoint | adaptive-forms/AdaptiveFormLayout.kt | adaptive-forms | API Pública | Helper para obtener columns | adaptive-forms (quedar) |
| resolveFieldSpan | adaptive-forms/AdaptiveFormLayout.kt | adaptive-forms | API Pública | Helper para resolver field span | adaptive-forms (quedar) |
| AdaptiveBreakpoint | adaptive-core/AdaptiveBreakpoint.kt | adaptive-core | API Pública | Enum de breakpoints (Compact/Medium/Expanded/Large) | adaptive-core (quedar) |
| AdaptiveWindowSize | adaptive-core/AdaptiveWindowSize.kt | adaptive-core | API Pública | WindowSize composable | adaptive-core (quedar) |
| AdaptiveInfo | adaptive-core/AdaptiveInfo.kt | adaptive-core | API Pública | Info adaptativa (breakpoint) | adaptive-core (quedar) |
| rememberAdaptiveInfo | adaptive-core/AdaptiveInfo.kt | adaptive-core | API Pública | remember de AdaptiveInfo | adaptive-core (quedar) |
| AdaptiveContent | adaptive-core/AdaptiveContent.kt | adaptive-core | API Pública | Contenido centrado adaptable | adaptive-core (quedar) |
| AdaptiveScope | adaptive-core/AdaptiveScope.kt | adaptive-core | API Pública (interface) | Scope API principal | adaptive-core (quedar) |
| AdaptiveVisibility | adaptive-core/AdaptiveVisibility.kt | adaptive-core | API Pública | Visibilidad responsiva | adaptive-core (quedar) |
| AdaptiveValue | adaptive-core/AdaptiveValue.kt | adaptive-core | API Pública | Valor responsivo | adaptive-core (quedar) |
| AdaptiveTokens | adaptive-core/AdaptiveTokens.kt | adaptive-core | API Pública | Tokens de spacing/widths/radius/pane widths | adaptive-core (quedar) |

---

## Criterios para migrar a :adaptive-components

### Prioridad Alta (migrar primero)

1. **Botones y acciones**: DemoButton, DefaultActionButton, DefaultStatusBadge
2. **Avatars y medios**: DemoAvatar, DemoThumbnail, DefaultStatusBadge
3. **Cards y surfaces**: DemoCard, DemoPanel, DefaultMobileCardContent
4. **Menús y dropdowns**: AccountMenu, DefaultOverflowMenu
5. **Textos y campos**: DemoTextField

**Razonamiento**: Estos son componentes atómicos reutilizables por todos los módulos.

### Prioridad Media

1. **Sections y headers**: DemoSectionTitle, InfoRow
2. **Chips y toggles**: DemoToggleChip
3. **Layouts internos**: WeightedDataRow, DataCellBox, DataDivider

**Razonamiento**: Útiles para componentes más complejos, pero menos atómicos.

### Prioridad Baja

1. **Feedback states**: Ya en :adaptive-feedback, solo consolidar implementation
2. **Form helpers**: Ya en :adaptive-forms
3. **Navigation surfaces**: Ya en :adaptive-navigation
4. **Data helpers**: Ya en :adaptive-data

**Razonamiento**: Específicos de módulos, mejor mantener en su contexto actual.

---

## Helpers visuales duplicados

### Identificación de duplicación

| Helper 1 | Archivo 1 | Módulo 1 | Helper 2 | Archivo 2 | Módulo 2 | Duplicación |
|----------|-----------|----------|----------|-----------|----------|-------------|
| DemoButton | admin-demo/ui/AdminDemoUi.kt | admin-demo | DefaultActionButton | adaptive-data/AdaptiveDataView.kt | adaptive-data | Sí (API pública vs internal) |
| DemoCard | admin-demo/ui/AdminDemoUi.kt | admin-demo | DefaultMobileCardContent | adaptive-data/AdaptiveDataView.kt | adaptive-data | Parcial (KPI vs data card) |
| DemoPanel | admin-demo/ui/AdminDemoUi.kt | admin-demo | DemoCard | admin-demo/ui/AdminDemoUi.kt | admin-demo | Parcial (panel vs KPI card) |
| DemoAvatar | admin-demo/ui/AdminDemoUi.kt | admin-demo | - | - | - | Único (generar inicial de nombre) |
| DemoThumbnail | admin-demo/ui/AdminDemoUi.kt | admin-demo | - | - | - | Único (thumbnail de producto) |
| DemoBadge | admin-demo/ui/AdminDemoUi.kt | admin-demo | DefaultStatusBadge | adaptive-data/AdaptiveDataView.kt | adaptive-data | Sí (badge general vs status) |
| DemoSectionTitle | admin-demo/ui/AdminDemoUi.kt | admin-demo | InfoRow | admin-demo/ui/AdminDemoUi.kt | admin-demo | Parcial (title vs row) |

---

## Conclusiones

### Componentes candidatos para :adaptive-components (PR B2-B4)

1. **Botones**: DemoButton -> AdaptiveButton con variants
2. **Badges**: DemoBadge + DemoStatusText -> AdaptiveBadge + AdaptiveStatusBadge
3. **Avatars**: DemoAvatar -> AdaptiveAvatar
4. **Cards**: DemoCard + DemoPanel -> AdaptiveCard (KPI), AdaptivePanel
5. **Menús**: AccountMenu + DefaultOverflowMenu -> AdaptiveDropdownMenu + AdaptiveOverflowMenu
6. **Textfields**: DemoTextField -> AdaptiveTextField
7. **Sections**: DemoSectionTitle -> AdaptiveSectionHeader

### Componentes que NO migrar (dejar en módulos actuales)

- AdaptiveFormLayout, AdaptiveFormColumns, LabelPosition, FieldSpan (específico forms)
- AdaptiveDataView, AdaptiveDataColumn, AdaptiveDataState (específico data)
- AdaptiveNavigationScaffold, Drawer, BottomNavigation, NavigationRail, Sidebar (específico nav)
- AdaptiveEmptyState, AdaptiveLoadingState, AdaptiveErrorState (ya en adaptive-feedback)
- AdaptiveContainer, AdaptiveGrid (ya en adaptive-layout)

### Plan de migración

| PR | Componentes | Módulos afectados | Capturas requeridas |
|----|-------------|---|----|
| B1 | Stub del módulo | - | Build check |
| B2 | Button, IconButton, Badge, Avatar | data, admin-demo | Tablas, cards, actions, account |
| B3 | Card, Surface, Dropdown, Menu | data, admin-demo | Dash, employees, products, invoices |
| B4 | TextField, SearchField | forms, admin-demo | Settings, filters |
| B5 | SectionHeader, Divider, ToggleChip | data, forms, admin-demo | Sections, validation |
