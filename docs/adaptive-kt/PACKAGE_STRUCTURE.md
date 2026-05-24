# AdaptiveKt — Package Structure

## Gradle modules for v0.1-alpha

- `:adaptive-core`
- `:adaptive-layout`
- `:adaptive-navigation`
- `:adaptive-forms`
- `:adaptive-data`
- `:adaptive-feedback`
- `:admin-demo`

## Package names

### adaptive-core

- `io.github.adaptivekt.core`
- `io.github.adaptivekt.core.internal` (implementation-only)

### adaptive-layout

- `io.github.adaptivekt.layout`
- `io.github.adaptivekt.layout.internal`

### adaptive-navigation

- `io.github.adaptivekt.navigation`
- `io.github.adaptivekt.navigation.internal`

### adaptive-forms

- `io.github.adaptivekt.forms`
- `io.github.adaptivekt.forms.internal`

### adaptive-data

- `io.github.adaptivekt.data`
- `io.github.adaptivekt.data.internal`

### adaptive-feedback

- `io.github.adaptivekt.feedback`
- `io.github.adaptivekt.feedback.internal`

### admin-demo

- `io.github.adaptivekt.admin.demo`
- `io.github.adaptivekt.admin.demo.model`
- `io.github.adaptivekt.admin.demo.ui`

## Naming conventions

- Public API lives in the root package of each module.
- Internal implementation details are kept behind `.internal`.
- Demo code uses a separate `admin.demo` package.

## Build structure

- `adaptive-core` is the foundation and dependency for all other modules.
- `adaptive-layout`, `adaptive-navigation`, `adaptive-forms`, `adaptive-data`, and `adaptive-feedback` depend on `adaptive-core`.
- `admin-demo` depends on `adaptive-core`, `adaptive-layout`, `adaptive-navigation`, `adaptive-forms`, `adaptive-data`, and `adaptive-feedback`.
