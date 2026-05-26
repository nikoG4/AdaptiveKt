# AdaptiveKt - Admin Demo

## Objective

`admin-demo` is a standalone sample module that demonstrates the integrated use of:

- `adaptive-core`
- `adaptive-layout`
- `adaptive-navigation`
- `adaptive-forms`
- `adaptive-data`
- `adaptive-feedback`

The demo is a responsive admin dashboard for Compose Multiplatform without backend, persistence, or Material 3.
PR D1 intentionally adds remote image loading to `:admin-demo` only for visual demo data. There is still no backend or persistence.

## Screens

- Dashboard
- Employees
- Products
- Invoices
- Settings

## Adaptive Patterns

- `AdaptiveNavigationScaffold`
  - `Compact` -> Drawer / BottomNavigation
  - `Medium` -> NavigationRail
  - `Expanded` / `Large` -> Sidebar
- `AdaptiveContainer` centers page content.
- `AdaptiveGrid` organizes dashboard KPI cards.
- `AdaptiveDataView` renders desktop tables and generated mobile cards.
- `AdaptiveFormLayout` organizes form sections, fields, validation, and actions.
- `EmptyState`, `LoadingState`, and `ErrorState` are available from the invoice state toggles.

## PR8 - Professional Admin Defaults

The demo now shows AdaptiveKt as a professional admin dashboard kit:

- Dashboard with header, subtitle, and responsive KPI cards.
- Compact dashboard uses full-width cards.
- Medium and Expanded dashboard use two columns.
- Large dashboard uses four stable columns.
- NavigationRail uses fallback glyphs and short labels without broken wrapping.
- Sidebar has clear selection, quiet background, and consistent spacing.
- Compact topbar uses a glyph menu button and title.
- Employees, Products, and Invoices use `AdaptiveDataView` without custom `cardContent`, demonstrating generated mobile cards.
- Desktop tables use subtle headers, legible rows, pill badges, and inline actions.
- Mobile cards show title, subtitle, status, limited metadata, visible primary action, and overflow.
- Settings uses `AdaptiveFormLayout` with a reasonable max width and visible compact actions.
- Feedback states use improved defaults and can be reviewed with the Invoices toggles.

## Mock Data

The demo data lives in `AdminDemoData`:

- `Employee`
- `Product`
- `Invoice`
- `DashboardMetric`

## How To Run

From the project root:

```powershell
.\gradlew :admin-demo:compileKotlinJvm
```

Desktop run task:

```powershell
.\gradlew :admin-demo:run
```

## Visual Capture Mode

Single capture example:

```powershell
.\gradlew :admin-demo:run --args="--capture --screen dashboard --width 1440 --height 900 --output build/visual/dashboard-large.png"
```

Batch capture script:

```powershell
.\tools\capture-admin-demo.ps1
```

## PR8 Captures

Latest generated matrix:

- Directory: `build/visual-captures`
- Manifest: `build/visual-captures/manifest.json`
- Report: `build/visual-captures/visual-capture-report.md`
- ZIP: `build/adaptivekt-admin-demo-visual-captures.zip`

## PR9 - Polish, Media, and Account Menu

The demo now exercises richer professional defaults:

- Dashboard large has stronger KPI cards plus recent invoice and activity panels.
- Employees include generated initials avatars in desktop tables and compact cards.
- Products include generated thumbnails in desktop tables and compact cards.
- Desktop table actions use quieter secondary buttons by default.
- Compact cards retain media, status, limited metadata, one primary action, and overflow.
- The topbar includes a user avatar and account dropdown with profile, account settings, and sign out actions.
- Capture mode supports `--accountMenuOpen` for reviewing the dropdown open state.

## PR D1 - Demo-Only Remote Images

Remote image loading is demo-only in PR D1. AdaptiveKt core/components remain independent from image loading libraries.

Employees and Products now demonstrate real remote media:

- Employees load avatar URLs through demo-only `DemoRemoteAvatar`.
- Products load thumbnail URLs through demo-only `DemoRemoteThumbnail`.
- Avatars are circular and use `ContentScale.Crop`.
- Product thumbnails are rounded, bordered, and cropped.
- Null, loading, and failure states fall back to local generated placeholders.

The dependency is scoped to `:admin-demo`:

- `media.kamel:kamel-image:0.7.3`
- `io.ktor:ktor-client-cio:2.3.4`

Details are documented in `docs/adaptive-kt/ADMIN_DEMO_REMOTE_IMAGES.md`.

Account menu capture example:

```powershell
.\gradlew :admin-demo:run --args="--capture --screen dashboard --width 1440 --height 900 --output build/visual-captures/large/account-menu-large-1440x900.png --accountMenuOpen"
```

## Limitations

- No backend or persistence.
- No Material 3.
- Navigation is local state.
- No sorting or pagination yet.
- Overflow menus are simple Foundation-only popovers.
- Demo avatars and product thumbnails load public remote images when available and fall back to generated placeholders.
- The capture matrix records the initial Invoices content state; empty/loading/error are available through manual toggles.

## Main Files

- `admin-demo/build.gradle.kts`
- `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoApp.kt`
- `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoScreens.kt`
- `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoModel.kt`
- `admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt`
- `admin-demo/src/jvmMain/kotlin/io/github/adaptivekt/admin/demo/AdminDemoKt.kt`
