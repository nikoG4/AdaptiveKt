# AdaptiveKt release gate for external KMP chat consumers

Status: **NOT PUBLISHED**. This checklist is a release gate, not proof of Maven Central availability.

## Compatibility baseline

- Kotlin: 2.1.21
- Compose Multiplatform: 1.8.2
- Required: Android, Desktop/JVM, WasmJS; preserve iOS where currently supported.
- Consume exclusively from Maven Central; do not use mavenLocal, project substitutions, or copied source.

## P0 artifact map

| Artifact | Public API requirements |
| --- | --- |
| adaptive-components | AdaptiveAvatar, AdaptiveSearchField, AdaptiveDialog, AdaptiveIconButton, AdaptiveDropdownMenu, AdaptiveMenuItem, AdaptiveTextField, AdaptiveBadge, AdaptiveButton, AdaptiveCard |
| adaptive-layout | AdaptiveListDetailScaffold, AdaptiveActionBar |
| adaptive-feedback | AdaptiveEmptyState, AdaptiveLoadingState, AdaptiveErrorState |
| adaptive-navigation | AdaptiveNavigationScaffold and dependencies |
| adaptive-forms | AdaptiveFormLayout |
| adaptive-core | Shared theme, tokens and dependencies |

Group: `io.github.nikog4.adaptivekt`. Confirm actual version and platform variants against Maven Central before telling consumers to upgrade.

## P1 implementation gate

1. AdaptivePasswordField: masked input, reveal toggle, errors, semantics, appropriate IME and disabled state.
2. AdaptiveMultilineTextField (or compatible AdaptiveTextField extension): IME support, bounded height, max length, multiline.
3. AdaptiveSnackbar/AdaptiveToast: nonblocking transient notices, retry action, dismissal, accessibility.
4. AdaptiveSkeleton: responsive placeholder primitives.

## P2 (nonblocking)

Presence indicator, generic selectable contact/conversation row, chronological separators.

## Release verification (mandatory)

- [ ] Review and preserve existing public signatures; add tests for new components.
- [ ] Build all target variants and inspect Gradle module metadata (.module), POMs, Android AARs, JVM artifacts, and WasmJS publications.
- [ ] Publish a *new* immutable version through the guarded Central Portal workflow, with valid signatures.
- [ ] Verify that the deployment is actually published (Central Portal upload alone is insufficient).
- [ ] Independently retrieve **all six required module coordinates** from `https://repo.maven.apache.org/maven2/` (no credentials, no repository fallback).
- [ ] Compile a clean standalone KMP consumer against Maven Central only, importing P0 + P1 APIs, for Android, JVM, WasmJS.
- [ ] Publish an example that composes AdaptivePasswordField, AdaptiveListDetailScaffold, AdaptiveAvatar, and AdaptiveSnackbar.
- [ ] Record the exact tested version, public coordinates, CI run, and external consumer build logs.

## Separation of concerns

This is a library compatibility checklist. Do not copy consumer business logic, chat message rules, backend code or application tests into AdaptiveKt.

## Current release workflow caveat

`.github/workflows/publish-release.yml` uses `publishingType=USER_MANAGED`: its successful upload step **does not** make the version available on Maven Central. Publishing in Central Portal and external resolution are separate mandatory gates.

## Minimal consumer-facing Compose example (after the new version is published)

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.adaptivekt.components.AdaptiveAvatar
import io.github.adaptivekt.components.AdaptivePasswordField
import io.github.adaptivekt.feedback.AdaptiveSnackbar
import io.github.adaptivekt.layout.AdaptiveListDetailScaffold

@Composable
fun AdaptiveKtConsumerExample() {
    var password by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<String?>(null) }
    var showNotice by remember { mutableStateOf(false) }

    Column {
        AdaptivePasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
        )
        AdaptiveListDetailScaffold(
            selectedItem = selected,
            onBackToList = { selected = null },
            listPane = {
                Row {
                    AdaptiveAvatar(name = "Sample Contact")
                    // Supply selectable rows in the consuming application.
                }
            },
            detailPane = { selectedName ->
                AdaptiveAvatar(name = selectedName)
            },
        )
        if (showNotice) {
            AdaptiveSnackbar(
                message = "Action completed",
                actionLabel = "Undo",
                onAction = { showNotice = false },
                onDismiss = { showNotice = false },
            )
        }
    }
}
```

This is a source example only, **not** an independently compiled external consumer. Do not treat it as a release gate pass.
