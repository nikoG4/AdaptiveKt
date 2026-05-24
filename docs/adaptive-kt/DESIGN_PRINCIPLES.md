# AdaptiveKt — Design Principles

## 1. Compose-First, Not CSS-First
AdaptiveKt is built around Compose idioms:

- composables as the primary API surface,
- `Modifier` for styling and layout extension,
- slots for content injection and customization,
- typed parameters instead of string-based rules.

## 2. Platform-Agnostic Adaptivity
The library supports Android, Desktop, Web, and WASM by focusing on:

- `AdaptiveBreakpoint` driven layout decisions,
- declarative breakpoints instead of platform-specific code,
- adaptive containers and scaffolds.

## 3. Complement Material 3
AdaptiveKt adds responsive structure without replacing Material:

- reuse Material components inside adaptive layouts,
- provide wrappers that work with Material 3 theming,
- keep styling optional and composable.

## 4. High-Level Patterns Over Low-Level Widgets
Provide building blocks for app architecture:

- adaptive containers,
- adaptive navigation,
- responsive forms,
- adaptive data views,
- feedback states.

Avoid rigid, opinionated widgets.

## 5. Slots and Customization
Expose flexible slots for:

- headers,
- sidebars,
- filter panels,
- actions,
- row/card rendering,
- form field content.

## 6. Design Tokens as First-Class Values
Supply a coherent token system for spacing, width, radius, and pane widths so adaptive UIs remain consistent.

## 7. Progressive Enhancement
Support a mobile-first baseline and enhance for larger screens:

- `Compact`: one-column forms, drawer navigation,
- `Medium`: two-column forms, hybrid navigation,
- `Expanded` / `Large`: sidebar navigation, multi-column layout.

## 8. Accessibility and Usability
Ensure adaptive patterns respect:

- keyboard and focus behavior,
- touch-friendly spacing,
- responsive action visibility,
- mobile-friendly navigation patterns.

## 9. Future-Friendly API Stability
Design public APIs with:

- minimal breaking surface,
- clear module boundaries,
- extension points for custom adaptivity,
- ability to evolve from alpha to stable.
