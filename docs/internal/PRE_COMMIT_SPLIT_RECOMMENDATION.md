# Pre-commit Split Recommendation

The current git uncommitted changes represent a massive architectural refactoring, the introduction of a new component library, and major structural hardening. To keep the repository history legible and bisectable, it is highly recommended to split the changes into four isolated commits.

## Commit 1: Core Configuration
**Message:** `feat(core): add adaptive app configuration system`
**Purpose:** Introduces the `AdaptiveApp` boundaries, breakpoint calculations, and policy resolution layer.
**Files to stage:**
- `adaptive-core/src/commonMain/kotlin/io/github/adaptivekt/core/*`
- `adaptive-core/src/commonTest/kotlin/io/github/adaptivekt/core/*`
- *Rename tracked as moved:* `adaptive-navigation/.../AdaptiveNavigationMode.kt` -> `adaptive-core/.../AdaptiveNavigationMode.kt`

## Commit 2: Layout Primitives
**Message:** `feat(layout): add adaptive page and list-detail primitives`
**Purpose:** Implements the high-level `AdaptivePage`, `AdaptiveTwoPane`, and `AdaptiveListDetailScaffold` primitives that consume the newly merged Core configuration.
**Files to stage:**
- `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/*`
- `adaptive-layout/src/commonTest/kotlin/io/github/adaptivekt/layout/*`
- `adaptive-layout/src/jvmTest/kotlin/io/github/adaptivekt/layout/AdaptiveTwoPaneTest.kt`
- `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/AdaptiveNavigationScaffold.kt`

## Commit 3: Examples and Demos
**Message:** `feat(examples): add AI workspace demo and refactor ecommerce`
**Purpose:** Maps the new primitives into the real-world sample applications, completely eliminating `BoxWithConstraints` usages.
**Files to stage:**
- `examples/ai-workspace-demo/*` (entire new directory)
- `examples/ecommerce-demo/*` (all modifications)
- `docs-site/src/commonMain/kotlin/io/github/adaptivekt/site/SiteDemoPage.kt`

## Commit 4: Build Hardening & Documentation
**Message:** `chore(build): harden demo validation and Pages integration`
**Purpose:** Solidifies CI/CD limits, adds rigorous layout guards, and documents the entire architectural transition via internal audit files.
**Files to stage:**
- `.github/workflows/ci.yml`
- `.gitignore`
- `README.md`, `adaptive-core/README.md`, `adaptive-layout/README.md`
- `tools/prepare-pages-site.ps1`
- `scripts/*`
- `docs/internal/*`
- `adaptive-layout/src/jvmTest/kotlin/io/github/adaptivekt/layout/LayoutGuardTest.kt`

---

## Fallback (Single Commit)
If you prefer a single merge commit for simplicity during rapid iteration:
**Message:** `feat(layout): add adaptive primitives and AI workspace demo`
**Body:** Introduces the core `AdaptiveConfig` layout resolution boundary, the `AdaptiveListDetailScaffold` and `AdaptivePage` families, and completely replaces `BoxWithConstraints` in the demo apps. Hardens WebAssembly compilation and CI validation matrices.
