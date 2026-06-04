param(
    [string]$SmokeDir = ""
)

$ErrorActionPreference = "Stop"

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$localMaven = Join-Path $repoRoot "build\local-maven"

if (-not (Test-Path $localMaven)) {
    throw "Local Maven repository not found: $localMaven. Run .\gradlew.bat publishAllPublicationsToLocalTestRepository first."
}

$propertiesPath = Join-Path $repoRoot "gradle.properties"
$properties = @{}
Get-Content $propertiesPath | ForEach-Object {
    if ($_ -match "^\s*([^#][^=]+?)\s*=\s*(.+?)\s*$") {
        $properties[$matches[1].Trim()] = $matches[2].Trim()
    }
}

$groupId = $properties["GROUP"]
$versionName = $properties["VERSION_NAME"]

if ([string]::IsNullOrWhiteSpace($groupId) -or [string]::IsNullOrWhiteSpace($versionName)) {
    throw "GROUP and VERSION_NAME must be defined in gradle.properties."
}

if ([string]::IsNullOrWhiteSpace($SmokeDir)) {
    $SmokeDir = Join-Path $repoRoot "build\local-consumer-smoke"
}

$smokeFullPath = [System.IO.Path]::GetFullPath($SmokeDir)
$buildRoot = [System.IO.Path]::GetFullPath((Join-Path $repoRoot "build"))

if (-not $smokeFullPath.StartsWith($buildRoot, [System.StringComparison]::OrdinalIgnoreCase)) {
    throw "SmokeDir must be inside the repository build directory. Refusing to recreate: $smokeFullPath"
}

if (Test-Path $smokeFullPath) {
    Remove-Item -LiteralPath $smokeFullPath -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $smokeFullPath | Out-Null
New-Item -ItemType Directory -Force -Path (Join-Path $smokeFullPath "src\commonMain\kotlin\smoke") | Out-Null

$localMavenUri = ($localMaven -replace "\\", "/")

Set-Content -Path (Join-Path $smokeFullPath "settings.gradle.kts") -Encoding UTF8 -Value @"
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("$localMavenUri") }
        google()
        mavenCentral()
    }
}

rootProject.name = "adaptivekt-local-consumer-smoke"
"@

Set-Content -Path (Join-Path $smokeFullPath "build.gradle.kts") -Encoding UTF8 -Value @"
@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    kotlin("multiplatform") version "2.1.21"
    id("org.jetbrains.compose") version "1.8.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21"
}

kotlin {
    jvm()
    wasmJs {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation("${groupId}:adaptive-core:${versionName}")
                implementation("${groupId}:adaptive-components:${versionName}")
                implementation("${groupId}:adaptive-layout:${versionName}")
                implementation("${groupId}:adaptive-feedback:${versionName}")
                implementation("${groupId}:adaptive-navigation:${versionName}")
                implementation("${groupId}:adaptive-forms:${versionName}")
                implementation("${groupId}:adaptive-data:${versionName}")
            }
        }
    }
}
"@

Set-Content -Path (Join-Path $smokeFullPath "src\commonMain\kotlin\smoke\ConsumerSmoke.kt") -Encoding UTF8 -Value @"
package smoke

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveCard
import io.github.adaptivekt.core.AdaptiveBreakpoint
import io.github.adaptivekt.core.AdaptiveColorSchemes
import io.github.adaptivekt.core.AdaptiveTheme
import io.github.adaptivekt.data.AdaptiveDataColumn
import io.github.adaptivekt.data.AdaptiveDataContent
import io.github.adaptivekt.data.AdaptiveDataView
import io.github.adaptivekt.feedback.AdaptiveEmptyState
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.layout.AdaptiveGrid
import io.github.adaptivekt.navigation.AdaptiveNavItem
import io.github.adaptivekt.navigation.AdaptiveNavigationScaffold

private data class SmokeRow(val name: String)

fun smokeBreakpointReference(): AdaptiveBreakpoint = AdaptiveBreakpoint.Compact

@Composable
fun ConsumerSmokeApp() {
    AdaptiveTheme(colorScheme = AdaptiveColorSchemes.defaultLight()) {
        Column {
            AdaptiveGrid {
                item(span = 6) {
                    AdaptiveCard {
                        BasicText("Published card")
                    }
                }
            }

            AdaptiveButton(text = "Published button", onClick = {})

            AdaptiveEmptyState(title = "Published empty state")

            AdaptiveFormLayout {
                section(title = "Published form") {
                    field(label = "Name") {
                        BasicText("Alicia")
                    }
                }
                actions {
                    primary {
                        AdaptiveButton(text = "Save", onClick = {})
                    }
                }
            }

            AdaptiveDataView(
                state = AdaptiveDataContent(listOf(SmokeRow("Alicia"))),
                columns = listOf(
                    AdaptiveDataColumn<SmokeRow>(
                        id = "name",
                        header = "Name",
                    ) { row ->
                        BasicText(row.name)
                    },
                ),
            )

            AdaptiveNavigationScaffold(
                navItems = listOf(AdaptiveNavItem(id = "dashboard", label = "Dashboard")),
                selectedItemId = "dashboard",
                onItemSelected = {},
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    BasicText("Published navigation")
                }
            }
        }
    }
}
"@

Write-Host "Local Maven repository: $localMaven"
Write-Host "Smoke consumer directory: $smokeFullPath"
Write-Host "Consuming coordinates:"
@(
    "adaptive-core",
    "adaptive-components",
    "adaptive-layout",
    "adaptive-feedback",
    "adaptive-navigation",
    "adaptive-forms",
    "adaptive-data"
) | ForEach-Object {
    Write-Host ("  {0}:{1}:{2}" -f $groupId, $_, $versionName)
}

$isWindowsHost = [System.IO.Path]::DirectorySeparatorChar -eq "\"
$gradlew = if ($isWindowsHost) {
    Join-Path $repoRoot "gradlew.bat"
} else {
    Join-Path $repoRoot "gradlew"
}

if (-not $isWindowsHost) {
    & chmod +x $gradlew
}

& $gradlew -p $smokeFullPath compileKotlinJvm compileKotlinWasmJs --console=plain --stacktrace
if ($LASTEXITCODE -ne 0) {
    throw "Local consumer smoke build failed with exit code $LASTEXITCODE."
}

Write-Host "Local publishing consumer smoke passed."
