param(
    [string[]]$Roots = @(
        "adaptive-core",
        "adaptive-components",
        "adaptive-layout",
        "adaptive-navigation",
        "adaptive-data",
        "adaptive-forms",
        "adaptive-feedback"
    )
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path "$PSScriptRoot\.."
Set-Location $repoRoot

$files = foreach ($root in $Roots) {
    if (Test-Path $root) {
        Get-ChildItem -Path $root -Recurse -Include *.kt -File | Where-Object {
            $_.FullName -like "*\src\commonMain\kotlin\*"
        }
    }
}

$failures = New-Object System.Collections.Generic.List[string]
$warnings = New-Object System.Collections.Generic.List[string]

foreach ($file in $files) {
    $relativePath = Resolve-Path -Relative $file.FullName
    $content = Get-Content -LiteralPath $file.FullName

    for ($i = 0; $i -lt $content.Count; $i++) {
        $line = $content[$i]
        $lineNumber = $i + 1

        if ($line -match "pointerHoverIcon" -and $relativePath -notlike "*AdaptiveInteractionModifiers.kt") {
            $failures.Add("${relativePath}:${lineNumber} direct pointerHoverIcon usage; use Modifier.adaptiveInteractiveCursor")
        }

        if ($line -match "Color\.(Black|White|Gray|Red|Green)" -and $relativePath -notlike "*AdaptiveColorScheme.kt") {
            $failures.Add("${relativePath}:${lineNumber} hardcoded named color; use AdaptiveTheme tokens")
        }

        if ($line -match "MaterialTheme\.colorScheme") {
            $failures.Add("${relativePath}:${lineNumber} Material colorScheme inside library component")
        }

        if ($line -match "\.clickable\s*\(" -or $line -match "Modifier\.clickable\s*\(") {
            if ($line.TrimStart().StartsWith("import ")) {
                continue
            }

            $start = [Math]::Max(0, $i - 8)
            $nearby = ($content[$start..$i] -join "`n")
            if ($nearby -notmatch "adaptiveInteractiveCursor") {
                $failures.Add("${relativePath}:${lineNumber} clickable without nearby adaptiveInteractiveCursor")
            }
        }

        if ($line -match "\b(Row|Column|Box|Spacer|Text)\s*\(") {
            $warnings.Add("${relativePath}:${lineNumber} structural Compose primitive")
        }
    }
}

Write-Host "AdaptiveKt component interaction guard" -ForegroundColor Cyan
Write-Host "Files scanned: $($files.Count)"
Write-Host "Warnings: $($warnings.Count)"

if ($warnings.Count -gt 0) {
    $warnings | Select-Object -First 25 | ForEach-Object { Write-Host "[warn] $_" -ForegroundColor DarkYellow }
    if ($warnings.Count -gt 25) {
        Write-Host "[warn] ... $($warnings.Count - 25) additional warning entries omitted" -ForegroundColor DarkYellow
    }
}

if ($failures.Count -gt 0) {
    Write-Host "Hard failures:" -ForegroundColor Red
    $failures | ForEach-Object { Write-Host "[fail] $_" -ForegroundColor Red }
    exit 1
}

Write-Host "Component interaction guard passed." -ForegroundColor Green
