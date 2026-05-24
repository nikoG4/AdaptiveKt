# AdaptiveKt — Implementation Plan

This implementation plan is sliced into small, review-friendly PRs.

## PR 1: adaptive-core + tokens + tests

### Objective
Implement the adaptive foundation and token contracts.

### Files expected
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveBreakpoint.kt`
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveWindowSize.kt`
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveInfo.kt`
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveScope.kt`
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveContent.kt`
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveVisibility.kt`
- `adaptive-core/src/commonMain/kotlin/.../AdaptiveTokens.kt`
- `adaptive-core/src/commonTest/kotlin/.../AdaptiveCoreTest.kt`

### Criteria of acceptance
- `rememberAdaptiveInfo()` returns `AdaptiveInfo`.
- `adaptiveValue(...)` resolves values correctly across breakpoints.
- `AdaptiveVisibility` renders content only when expected.

### Gradle verification
- `./gradlew :adaptive-core:test`

### Risks
- breakpoint mapping may require adjustment across platforms.
- token naming may need refinement before wider usage.

## PR 2: adaptive-layout: AdaptiveContainer + AdaptiveGrid

### Objective
Implement responsive layout primitives for v0.1-alpha.

### Files expected
- `adaptive-layout/src/commonMain/kotlin/.../AdaptiveContainer.kt`
- `adaptive-layout/src/commonMain/kotlin/.../AdaptiveGrid.kt`
- `adaptive-layout/src/commonTest/kotlin/.../AdaptiveLayoutTest.kt`

### Criteria of acceptance
- `AdaptiveContainer` applies max-width and padding correctly.
- `AdaptiveGrid` supports 12-column content and item spans.

### Gradle verification
- `./gradlew :adaptive-layout:test`

### Risks
- layout span handling may vary by platform measurement.
- default gap semantics may need tuning.

## PR 3: adaptive-feedback: Empty/Loading/Error

### Objective
Implement the feedback primitives used by the demo.

### Files expected
- `adaptive-feedback/src/commonMain/kotlin/.../EmptyState.kt`
- `adaptive-feedback/src/commonMain/kotlin/.../LoadingState.kt`
- `adaptive-feedback/src/commonMain/kotlin/.../ErrorState.kt`
- `adaptive-feedback/src/commonTest/kotlin/.../AdaptiveFeedbackTest.kt`

### Criteria of acceptance
- feedback primitives expose clear APIs.
- states render default title/message and optional action slot.

### Gradle verification
- `./gradlew :adaptive-feedback:test`

### Risks
- visual defaults may not match target apps; keep them minimal.

## PR 4: adaptive-navigation básico

### Objective
Implement the adaptive navigation scaffold and core navigation components.

### Files expected
- `adaptive-navigation/src/commonMain/kotlin/.../AdaptiveNavItem.kt`
- `adaptive-navigation/src/commonMain/kotlin/.../AdaptiveNavigationScaffold.kt`
- `adaptive-navigation/src/commonMain/kotlin/.../Sidebar.kt`
- `adaptive-navigation/src/commonMain/kotlin/.../Drawer.kt`
- `adaptive-navigation/src/commonMain/kotlin/.../BottomNavigation.kt`
- `adaptive-navigation/src/commonMain/kotlin/.../NavigationRail.kt`
- `adaptive-navigation/src/commonTest/kotlin/.../AdaptiveNavigationTest.kt`

### Criteria of acceptance
- `AdaptiveNavigationScaffold` selects navigation mode by breakpoint.
- `onItemSelected` callback works without routing dependencies.

### Gradle verification
- `./gradlew :adaptive-navigation:test`

### Risks
- navigation behavior must remain platform-agnostic.
- default bottom nav or drawer semantics may need later refinement.

## PR 5: adaptive-forms básico

### Objective
Implement the basic responsive form layout contract.

### Files expected
- `adaptive-forms/src/commonMain/kotlin/.../AdaptiveFormLayout.kt`
- `adaptive-forms/src/commonMain/kotlin/.../FormSection.kt`
- `adaptive-forms/src/commonMain/kotlin/.../FormField.kt`
- `adaptive-forms/src/commonMain/kotlin/.../FormActions.kt`
- `adaptive-forms/src/commonMain/kotlin/.../FormTypes.kt`
- `adaptive-forms/src/commonTest/kotlin/.../AdaptiveFormsTest.kt`

### Criteria of acceptance
- form fields can be marked `Full`, `Half`, `TwoThirds`.
- labels adapt to compact and expanded layouts.
- sticky mobile actions are supported.

### Gradle verification
- `./gradlew :adaptive-forms:test`

### Risks
- field span logic can be complex across breakpoints.
- label positioning may need extra design refinement.

## PR 6: adaptive-data básico

### Objective
Implement the basic adaptive data view wrapper and state model.

### Files expected
- `adaptive-data/src/commonMain/kotlin/.../AdaptiveDataState.kt`
- `adaptive-data/src/commonMain/kotlin/.../AdaptiveDataColumn.kt`
- `adaptive-data/src/commonMain/kotlin/.../AdaptiveDataView.kt`
- `adaptive-data/src/commonTest/kotlin/.../AdaptiveDataTest.kt`

### Criteria of acceptance
- data view switches from table to card mode based on breakpoint.
- loading, empty, error states are supported.
- `filterSlot`, `actions`, and `rowActions` slots are available.

### Gradle verification
- `./gradlew :adaptive-data:test`

### Risks
- state-driven rendering must remain simple and not overfit advanced features.
- card/table transition semantics may require UI review.

## PR 7: admin-demo integrando todo

### Objective
Build the minimal demo app showcasing v0.1-alpha.

### Files expected
- `admin-demo/src/commonMain/kotlin/.../AdminDemoApp.kt`
- `admin-demo/src/commonMain/kotlin/.../DashboardScreen.kt`
- `admin-demo/src/commonMain/kotlin/.../EmployeesScreen.kt`
- `admin-demo/src/commonMain/kotlin/.../ProductsScreen.kt`
- `admin-demo/src/commonMain/kotlin/.../InvoicesScreen.kt`
- `admin-demo/src/commonMain/kotlin/.../SettingsScreen.kt`
- `admin-demo/src/commonMain/kotlin/.../model/Employee.kt`
- `admin-demo/src/commonMain/kotlin/.../model/Product.kt`
- `admin-demo/src/commonMain/kotlin/.../model/Invoice.kt`

### Criteria of acceptance
- desktop view shows sidebar, table list, and 2-column forms.
- mobile view shows drawer/hamburger, card list, and 1-column forms.
- `AdaptiveDataView` and `AdaptiveFormLayout` are used in screens.
- `EmptyState`, `LoadingState`, and `ErrorState` are demonstrated.

### Gradle verification
- `./gradlew :admin-demo:run`

### Risks
- demo complexity can grow if too many screens are added.
- platform-specific app startup may require small adjustments.
