# Resumen de Arquitectura de AdaptiveKt

**Fecha:** 2026-05-22

## Estructura Actual

### Módulos

```
adaptive-kt/
├── adaptive-core/          # Base: tokens, breakpoints, scopes, visibility
├── adaptive-layout/        # Grid, container adaptativos
├── adaptive-navigation/     # Navigation chrome (sidebar, drawer, rail, bottom nav)
├── adaptive-forms/         # Layout de formularios responsivos
├── adaptive-feedback/      # Empty, loading, error states
├── adaptive-data/          # DataView, columns, actions, states
└── admin-demo/             # Demo integrado con capture tooling
```

### Dependencias

```
:adaptive-core
    ↑ (dependido por todos)
:adaptive-layout
    ↑
:adaptive-navigation
:adaptive-feedback
    ↑
:adaptive-forms
    ↑
:adaptive-data
    ↑
:admin-demo (consume todos)
```

## API Pública por Módulo

### :adaptive-core

| API | Descripción |
|-----|-------------|
| `AdaptiveBreakpoint` | Compact, Medium, Expanded, Large |
| `AdaptiveWindowSize` | Composable para obtener breakpoint actual |
| `AdaptiveInfo` | Info responsiva (breakpoint actual) |
| `rememberAdaptiveInfo()` | remember para AdaptiveInfo |
| `AdaptiveContent` | Contenido centrado adaptable |
| `AdaptiveScope` | API compositional principal |
| `AdaptiveVisibility` | Visibilidad responsiva |
| `AdaptiveValue` | Valor responsivo |
| `AdaptiveTokens` | Tokens de spacing/widths/radius/pane widths |

### :adaptive-layout

| API | Descripción |
|-----|-------------|
| `AdaptiveContainer` | Container centrado que adapta breakpoint |
| `AdaptiveGrid` | Grid adaptativo con fillMaxWidth(fraction) |
| `AdaptiveGridScope` | Scope para items del grid |

### :adaptive-navigation

| API | Descripción |
|-----|-------------|
| `AdaptiveNavigationScaffold` | Scaffold que elige nav chrome por breakpoint |
| `AdaptiveNavItem` | Item de navegación con id/label/icon |
| `AdaptiveNavigationMode` | Enum: Drawer, BottomNavigation, NavigationRail, Sidebar |
| `navigationModeForBreakpoint()` | Helper para elegir modo por breakpoint |
| `Drawer` | Drawer compacto con overlay |
| `BottomNavigation` | Bottom nav horizontal en compact |
| `NavigationRail` | Rail vertical con glyph fallback |
| `Sidebar` | Sidebar con header/app bar |

### :adaptive-forms

| API | Descripción |
|-----|-------------|
| `AdaptiveFormLayout` | Layout de formulario responsivo |
| `AdaptiveFormScope` | Scope para definir formularios |
| `AdaptiveFormSectionScope` | Scope para secciones |
| `AdaptiveFormActionsScope` | Scope para acciones |
| `FieldSpan` | Full, Half, Third, TwoThirds, Columns |
| `LabelPosition` | Top, Start |
| `AdaptiveValidationMessage` | Mensaje de validación |
| `AdaptiveValidationMessageType` | Error, Warning, Info |
| `AdaptiveFormColumns` | Helper de columns por breakpoint |

### :adaptive-data

| API | Descripción |
|-----|-------------|
| `AdaptiveDataView` | Table/Card responsive |
| `AdaptiveDataColumn<T>` | Columna con metadata y cell |
| `AdaptiveDataState<out T>` | Sealed interface de states |
| `AdaptiveDataLoading` | Estado de carga |
| `AdaptiveDataError` | Estado de error |
| `AdaptiveDataEmpty` | Estado vacío |
| `AdaptiveDataContent` | Estado con contenido |
| `AdaptiveFilterSlot` | Slot opcional para filtro |
| `AdaptiveDataMobileRole` | Enum de roles para columnas |
| `AdaptiveActionPriority` | Enum de prioridades de acción |
| `AdaptiveDataAction<T>` | Acción de fila |
| `shouldUseTableLayout()` | Determinar si usar tabla o cards |
| `visibleColumnsForBreakpoint()` | Filtrar columnas por breakpoint |

### :adaptive-feedback

| API | Descripción |
|-----|-------------|
| `AdaptiveEmptyState` | Empty state composable |
| `AdaptiveLoadingState` | Loading state composable |
| `AdaptiveErrorState` | Error state composable |

### admin-demo

| API | Descripción |
|-----|-------------|
| `AdminDemoApp` | Entry point de demo |
| `AdminDemoScreen` | Screens (dashboard, employees, etc.) |
| `AdminDemoData` | Mock data |

## Principios de Diseño

### Foundation-only

- Sin Material 3 forzado
- Usar solo compose.foundation
- Componentes reutilizables sin dependencias pesadas

### Responsive Patterns

- Layouts se adaptan por breakpoint
- Navegación responsive (sidebar/rail/drawer/bottom nav)
- Tablas vs cards responsive
- Formularios responsive

### Zero-Opinion API

- Componentes sin contexto específico
- Slots opcionales (icon, filter, actions)
- Defaults profesionales sin hardcode

### Admin Defaults

- Default simple code produce buena UI
- No requerir 20 modifiers
- Visual quality similar a AdminLTE/CoreUI

## Limitaciones Actuales

### Compose Foundation (1.5.1)

- `weight()` es internal API, no disponible públicamente
- Drawer sin gestos swipe (overlay manual)
- No Material 3 disponible en MPP

### Captures

- Requiere sesión gráfica activa
- No válido en headless sin display virtual

### Iconos

- Sin icon pack obligatorio
- Fallback glyph (initial) si icon == null
- Futuro: módulos opcionales

## Plan Futuro

### :adaptive-components

- Componentes atómicos (botones, badges, avatars)
- Sin dependencias cíclicas
- Solo depende de :adaptive-core

### Select (futuro)

- AdaptiveSelect, AdaptiveMultiSelect
- Búsqueda local, item custom
- Fuera de alcance v0.1

### Dark Mode (futuro)

- AdaptiveTheme
- AdaptiveColorScheme
- Tokens light/dark
- Migración gradual de colores

### Icon Packs (futuro)

- :adaptive-icons-fontawesome
- :adaptive-icons-lucide
- Opcionales, desacoplados
