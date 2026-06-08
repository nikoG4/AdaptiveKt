# Module Dependency Audit

This review ensures that the Gradle multi-module architecture remains clean, acyclic, and properly scoped after the layout hardening phase.

## Current Dependency Graph
1. **`adaptive-core`**
   - Dependencies: `compose.foundation`
   - Role: Pure data structures, enums, layout bounds measuring logic (`AdaptiveApp`, `AdaptiveConfig`). Contains no layout drawing algorithms.
   - Leakage: None. Does not depend on any internal modules.

2. **`adaptive-layout`**
   - Dependencies: `project(":adaptive-core")` (implementation), `compose.foundation`
   - Role: Provides the high-level drawing primitives (`AdaptiveGrid`, `AdaptiveTwoPane`, `AdaptiveListDetailScaffold`).
   - Leakage: None. Does not depend on navigation or demo modules.

3. **`adaptive-navigation`**
   - Dependencies: `project(":adaptive-core")` (`api`), `project(":adaptive-components")` (implementation)
   - Role: High-level scaffold and routing mechanism.
   - Leakage: None. It deliberately uses `api` to expose `adaptive-core` so consumers of `AdaptiveNavigationScaffold` implicitly receive access to `AdaptiveNavigationMode` without needing a dedicated core dependency in their app-level build files.

## The `AdaptiveNavigationMode` Move
- **File location:** `adaptive-core/src/commonMain/kotlin/io/github/adaptivekt/navigation/AdaptiveNavigationMode.kt`
- **Why:** The core configuration block (`AdaptiveNavigationPolicy`) must know about the enum states (BottomBar vs Sidebar) to resolve rules, but `adaptive-core` cannot depend on `adaptive-navigation`.
- **Integrity Check:** The package declaration remains `io.github.adaptivekt.navigation`. Because Kotlin does not enforce strict package-to-directory structure mirroring, this works perfectly. 
- **Conclusion:** Circular dependencies were successfully avoided.

## Final Conclusion
No architectural issues found. The dependency hierarchy `core -> layout -> navigation` remains strict.
