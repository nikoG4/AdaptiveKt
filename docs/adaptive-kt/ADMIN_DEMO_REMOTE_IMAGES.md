# Admin Demo Remote Images

PR D1 adds remote images to `:admin-demo` only.

Remote image loading is demo-only in PR D1. AdaptiveKt core/components remain independent from image loading libraries.

## Rescue Notes

Gemma/Qwen left the tree partially touched:

- `Employee.avatarUrl` and `Product.thumbnailUrl` were already present in `AdminDemoModel.kt`.
- Public demo data already contained the requested remote URLs.
- `AdminDemoRemoteImages.kt` did not exist anymore, matching the report that Qwen deleted the broken helper to recover the build.
- No `PLAN.md` or stale `ADMIN_DEMO_REMOTE_IMAGES.md` was found.
- `:admin-demo:build` passed before continuing, so the baseline was recovered.

## Dependency

The image loading dependency is scoped to `:admin-demo`:

- `media.kamel:kamel-image:0.7.3` in `commonMain`
- `io.ktor:ktor-client-cio:2.3.4` in `jvmMain`

No AdaptiveKt library module depends on Kamel or Ktor image loading.

## Helpers

Demo-only helpers live in:

`admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoRemoteImages.kt`

Implemented helpers:

```kotlin
@Composable
fun DemoRemoteAvatar(
    name: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
)

@Composable
fun DemoRemoteThumbnail(
    label: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    fallbackTone: Color = Color(0xFFE8F1FF),
)
```

`fallbackTone` is demo-only and lets product thumbnails keep the existing product color placeholder when the remote image is unavailable.

## Behavior

Employees:

- Uses `DemoRemoteAvatar`.
- Loads `avatarUrl` with Kamel.
- Uses `ContentScale.Crop`.
- Clips to a circle.
- Falls back to `AdaptiveAvatar` for null, loading, or failure.

Products:

- Uses `DemoRemoteThumbnail`.
- Loads `thumbnailUrl` with Kamel.
- Uses `ContentScale.Crop`.
- Clips to the existing admin thumbnail radius.
- Uses a subtle border.
- Falls back to the existing product-style generated thumbnail for null, loading, or failure.

The demo remains usable without internet or when public URLs fail.

## URLs

Employees:

- `https://randomuser.me/api/portraits/women/44.jpg`
- `https://randomuser.me/api/portraits/men/32.jpg`
- `https://randomuser.me/api/portraits/women/68.jpg`
- `https://randomuser.me/api/portraits/men/75.jpg`

Products:

- `https://picsum.photos/seed/adaptive-product-1/128/128`
- `https://picsum.photos/seed/adaptive-product-2/128/128`
- `https://picsum.photos/seed/adaptive-product-3/128/128`
- `https://picsum.photos/seed/adaptive-product-4/128/128`

## Verification

Commands executed:

```powershell
.\gradlew.bat :admin-demo:build --console=plain --stacktrace
.\gradlew.bat build --console=plain --stacktrace
.\tools\capture-admin-demo.ps1
```

Results:

- `:admin-demo:build` passed.
- Full `build` passed.
- Visual captures generated successfully.

Capture outputs:

- Directory: `build/visual-captures`
- Report: `build/visual-captures/visual-capture-report.md`
- ZIP: `build/adaptivekt-admin-demo-visual-captures.zip`

Visual review covered:

- `employees-compact-420x900.png`
- `employees-large-1440x900.png`
- `products-compact-420x900.png`
- `products-large-1440x900.png`

Observed:

- Employee avatars loaded and remained circular.
- Product thumbnails loaded and remained rounded/cropped.
- Compact cards kept media placement and hierarchy.
- Desktop tables stayed aligned and sober.

Note: Kamel emits SLF4J no-binding warnings to stderr during capture, but the capture tasks complete and images are generated.

## Constraints Preserved

- No public API change in `adaptive-components`.
- No public API change in `adaptive-data`.
- No `adaptive-navigation`, `adaptive-forms`, or `adaptive-feedback` changes.
- No Material 3.
- No icon packs.
- No Web target.
- No dark mode.
- No AdaptiveSelect.
- No broad refactor.

## Future Work

- Consider a tiny logging/no-op binding only if SLF4J capture noise becomes disruptive.
- Keep remote image loading out of AdaptiveKt core/components until a deliberate media primitive is designed.
- A future `AdaptiveThumbnail` could replace the demo-only product helper if the library needs a public media primitive.
