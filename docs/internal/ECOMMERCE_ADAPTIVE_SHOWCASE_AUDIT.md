# Ecommerce Adaptive Showcase Audit

## Current Architecture
The `ecommerce-demo` was previously refactored to remove app-level `BoxWithConstraints` and `breakpointForWidth`. However, it still relies heavily on manual responsive logic (e.g. `isCompact` checks) and generic Compose containers (`LazyColumn`, `Row`, `Column`, `Box`) rather than fully leveraging the new high-level `AdaptiveKt` primitives.

## Usage Counts
- **BoxWithConstraints**: 0
- **breakpointForWidth**: 0
- **LocalAdaptiveLayoutInfo.current**: 7
- **isCompact / if (compact)**: 7
- **AdaptiveGrid**: 11
- **Button**: 42
- **Card**: 25
- **TextField**: 17
- **LazyColumn**: 11
- **Row**: 69
- **Column**: 43
- **Box**: 54
- **Spacer**: 109

### Adaptive Primitives Usage
- **AdaptiveScrollablePage**: 0
- **AdaptiveColumnPage**: 0
- **AdaptiveSection**: 0
- **AdaptiveActionBar**: 0
- **AdaptiveTwoPane**: 0
- **AdaptiveListDetailScaffold**: 0

## Classification of Generic Compose Usages
1. **Acceptable Internal Implementation Details**: `Row`, `Column`, `Box`, and `Spacer` when used strictly inside small atomic UI components (e.g., placing an icon next to text in a badge).
2. **Should be Replaced by AdaptiveKt Primitives**: 
   - Top-level `LazyColumn` and outer layout containers should be swapped for `AdaptiveScrollablePage` or `AdaptiveColumnPage`.
   - Structural grouping should use `AdaptiveSection`.
   - Layout mode branching (`if (compact)`) should be removed in favor of `AdaptiveGrid` auto-flowing defaults and `AdaptiveTwoPane` stacking.
   - Filter/search rows should become `AdaptiveActionBar`.
   - Generic user-facing `Button`, `Card`, and `TextField` instances should be migrated to their adaptive library equivalents where possible.
3. **Intentionally Kept**: Specific custom Hero visual elements or unique components where no AdaptiveKt equivalent exists.
