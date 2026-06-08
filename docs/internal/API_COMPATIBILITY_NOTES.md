# API Compatibility Notes

## Signatures Checked

### `AdaptiveGrid`
- **Change**: `columns: Int? = null` replaces hardcoded defaults (if any).
- **Risk**: Low. Existing callers passing specific column counts will still resolve correctly.
- **Decision**: Keeping `Int?` is perfectly source-compatible with standard usage and enables inheriting context from `LocalAdaptiveLayoutInfo`.

### `AdaptiveContainer`
- **Change**: `maxWidth: Dp? = null`, `contentPadding: PaddingValues? = null`.
- **Risk**: Low. Any code providing a fixed `maxWidth` string will compile identically. Nullability enables default inference from LayoutInfo.
- **Decision**: No changes required.

### `AdaptiveNavigationScaffold`
- **Change**: `navigationMode` defaults to `LocalAdaptiveLayoutInfo.current.navigationMode`.
- **Risk**: None. Callers omitting it will automatically receive responsive behavior, while callers supplying a specific mode retain manual control.
- **Decision**: Maintained source compatibility.

### `AdaptivePage`, `AdaptiveColumnPage`, `AdaptiveScrollablePage`
- **Change**: Added `AdaptiveColumnPage` and `AdaptiveScrollablePage` with clean trailing lambda `content: @Composable (PaddingValues) -> Unit` or `content: @Composable () -> Unit`.
- **Risk**: Low. Since `AdaptivePage` requires `PaddingValues` usage, keeping `AdaptiveScrollablePage` content clean (`@Composable ColumnScope.() -> Unit` or `@Composable () -> Unit`) avoids ambiguity.
- **Decision**: Ensures backward compatibility with initial iterations.

## Summary
The adaptive layout refactoring introduces highly flexible defaults using `null` checks backing into `LocalAdaptiveLayoutInfo`. No breaking API signatures were introduced that would fail existing standard callers in the target repository.
