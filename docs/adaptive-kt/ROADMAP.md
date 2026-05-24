# AdaptiveKt — Roadmap

## Vision
Build an adaptive Compose Multiplatform toolkit focused on responsive structure, navigation, forms, data views, and feedback patterns.

## Release Plan

### v0.1-alpha — Foundation

- `adaptive-core`
  - `AdaptiveContent`
  - `AdaptiveBreakpoint`, `AdaptiveWindowSize`, `AdaptiveInfo`, `AdaptiveScope`
  - `rememberAdaptiveInfo()`, `adaptiveValue(...)`, `AdaptiveVisibility`
  - design tokens for spacing, widths, radius, and pane widths
- `adaptive-layout`
  - `AdaptiveContainer`
  - `AdaptiveGrid`
  - `AdaptiveGridScope.item`
- `adaptive-navigation`
  - basic `AdaptiveNavigationScaffold`
  - `AdaptiveNavItem`, `Sidebar`, `Drawer`, `BottomNavigation`, `NavigationRail`
- `adaptive-forms`
  - basic `AdaptiveFormLayout`
  - `FormSection`, `FormField`, `FormActions`
- `adaptive-data`
  - basic `AdaptiveDataView`
  - `AdaptiveDataColumn<T>`, `AdaptiveDataState<out T>`
- `adaptive-feedback`
  - `EmptyState`, `LoadingState`, `ErrorState`
- `admin-demo`
  - desktop and mobile demo of dashboard, employees, products, invoices, settings

### v0.2 — Analytic data and action surfaces

- advanced data behavior: sorting, pagination, filter panels
- action surfaces: adaptive toolbar and overflow actions
- mobile FAB support and action slot refinement

### v0.3 — Advanced forms

- validation summary and advanced error handling
- stepper/wizard form flows
- dirty state guard patterns

### v0.4 — Expanded layout patterns

- master-detail layout
- dashboard layout primitives
- enhanced settings form surfaces

### v1.0 — Stable release

- API stabilization
- complete documentation and samples
- tests across Android/Desktop/Web/WASM
- Maven publication

## Milestones

1. Define core adaptive contracts and tokens
2. Implement responsive layout primitives
3. Build adaptive navigation scaffold
4. Deliver basic adaptive forms and data view
5. Validate through admin-demo
6. Stabilize APIs and document
7. Publish v1.0

## Future extensions

- data virtualization helpers,
- keyboard/focus navigation improvements,
- theme-aware adaptive tokens,
- platform-specific interaction refinements.
