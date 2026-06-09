# Ecommerce Showcase Rules

The `ecommerce-demo` must serve as a pristine, idiomatic showcase of `AdaptiveKt`.

## Core Layout
- **Screen-level Layout**: Must use `AdaptiveScrollablePage` or `AdaptiveColumnPage`. Avoid naked `LazyColumn` for main page structures.
- **Page Sections**: Must use `AdaptiveSection` to naturally group content without manual padding logic.
- **Product Grids**: Must use `AdaptiveGrid` utilizing default bounds rather than manually forcing column counts based on compact/expanded states.
- **Top Actions & Filters**: Search, sort, and filter rows must use `AdaptiveActionBar`.
- **Complex Page Splits**: Cart, checkout, and product details with distinct primary/secondary areas must utilize `AdaptiveTwoPane`.

## Components
- **Cards**: User-facing cards should migrate to `AdaptiveCard` (if available).
- **Buttons**: User-facing buttons should use `AdaptiveButton` (if available).
- **Forms**: User inputs and checkouts should use `AdaptiveForm` primitives (if available).

## Permitted vs Prohibited Generic Usage
- **Permitted**: `Row`, `Column`, `Box`, `Text`, `Spacer` are fully allowed *inside* small presentational components.
- **Prohibited**: `Row`, `Column`, `Box` should *not* be the primary screen adaptive strategy.
- **Restricted**: `LocalAdaptiveLayoutInfo.current` should be severely minimized. It is allowed *only* for genuine product-specific structural variants (e.g. completely altering a sidebar layout) and must be explicitly documented. Manual layout sizing based on `layoutInfo.isCompact` for generic spacing is forbidden.
