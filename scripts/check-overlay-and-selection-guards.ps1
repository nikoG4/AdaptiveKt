param(
    [string[]]$Roots = @(
        "adaptive-components",
        "adaptive-data",
        "docs-site",
        "examples\ai-workspace-demo",
        "examples\ecommerce-demo"
    )
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path "$PSScriptRoot\.."
Set-Location $repoRoot

$failures = New-Object System.Collections.Generic.List[string]
$warnings = New-Object System.Collections.Generic.List[string]

$dialogFile = "adaptive-components\src\commonMain\kotlin\io\github\adaptivekt\components\AdaptiveDialog.kt"
if (-not (Test-Path $dialogFile)) {
    $failures.Add("AdaptiveDialog.kt is missing")
} else {
    $dialogContent = Get-Content -LiteralPath $dialogFile -Raw
    if ($dialogContent -notmatch "androidx\.compose\.ui\.window\.Dialog" -or $dialogContent -notmatch "\bDialog\s*\(") {
        $failures.Add("AdaptiveDialog must render through Compose Dialog, not inline layout-flow content")
    }
    if ($dialogContent -match "Color\.(Black|White|Gray)" -or $dialogContent -match "Color\(0x") {
        $failures.Add("AdaptiveDialog uses hardcoded scrim/surface colors; use AdaptiveTheme/AdaptiveOverlayDefaults")
    }
}

$files = foreach ($root in $Roots) {
    if (Test-Path $root) {
        Get-ChildItem -Path $root -Recurse -Include *.kt -File | Where-Object {
            $_.FullName -like "*\src\commonMain\kotlin\*"
        }
    }
}

foreach ($file in $files) {
    $relativePath = Resolve-Path -Relative $file.FullName
    $content = Get-Content -LiteralPath $file.FullName

    for ($i = 0; $i -lt $content.Count; $i++) {
        $line = $content[$i]
        $lineNumber = $i + 1

        if ($line -match "SelectionContainer" -and $relativePath -notlike "*AdaptiveSelectionArea.kt") {
            $failures.Add("${relativePath}:${lineNumber} direct SelectionContainer usage; use AdaptiveSelectionArea")
        }

        if ($line -match "androidx\.activity" -and $relativePath -like "*\src\commonMain\kotlin\*") {
            $failures.Add("${relativePath}:${lineNumber} androidx.activity import in commonMain")
        }

        if ($relativePath -like "*examples\*" -and ($line -match "BoxWithConstraints" -or $line -match "breakpointForWidth")) {
            $failures.Add("${relativePath}:${lineNumber} banned demo-level layout breakpoint/BoxWithConstraints usage")
        }

        if ($relativePath -like "*examples\*" -or $relativePath -like "*docs-site\*") {
            if ($line -match "androidx\.compose\.material3\.(Dialog|AlertDialog)") {
                $failures.Add("${relativePath}:${lineNumber} generic Material dialog import; use AdaptiveDialog")
            }
        }

        if ($relativePath -like "*adaptive-components*" -and $relativePath -match "Dialog|Dropdown|Select|Tooltip") {
            if ($line -match "Color\.(Black|White|Gray)" -and $relativePath -notlike "*AdaptiveOverlayDefaults*") {
                $failures.Add("${relativePath}:${lineNumber} hardcoded named color in overlay component")
            }
        }

        if ($line -match "\b(Text|Row|Column|Box|Spacer)\s*\(") {
            $warnings.Add("${relativePath}:${lineNumber} structural Compose primitive")
        }
    }
}

Write-Host "AdaptiveKt overlay and selection guard" -ForegroundColor Cyan
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

Write-Host "Overlay and selection guard passed." -ForegroundColor Green

