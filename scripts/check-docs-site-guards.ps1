param(
    [string]$SourceDir = "docs-site/src"
)

$ErrorActionPreference = "Stop"
$workspaceRoot = Split-Path -Parent $PSScriptRoot

Push-Location $workspaceRoot
$failed = $false

Write-Host "Checking for unsafe SelectionContainer imports in docs-site..."
$selectionContainerMatches = Get-ChildItem -Path "$SourceDir\*" -Recurse -Include *.kt -ErrorAction SilentlyContinue | Select-String -Pattern "import androidx.compose.foundation.text.selection.SelectionContainer" -ErrorAction SilentlyContinue
if ($selectionContainerMatches -ne $null) {
    Write-Host "ERROR: SelectionContainer should not be used directly in docs-site to prevent layout overlaps. Use AdaptiveSelectionArea." -ForegroundColor Red
    $selectionContainerMatches | ForEach-Object { Write-Host "$($_.Path):$($_.LineNumber)" }
    $failed = $true
}

Write-Host "Checking for duplicate hash IDs in SiteComponentsPage.kt..."
$ids = Select-String -Path "$SourceDir\commonMain\kotlin\io\github\adaptivekt\site\SiteComponentsPage.kt" -Pattern 'id = "([^"]+)"' -AllMatches
$idList = $ids.Matches | Foreach-Object { $_.Groups[1].Value }
$duplicates = $idList | Group-Object | Where-Object Count -gt 1
if ($duplicates) {
    Write-Host "ERROR: Duplicate component IDs found:" -ForegroundColor Red
    $duplicates | ForEach-Object { Write-Host $_.Name }
    $failed = $true
}

if ($failed) {
    Write-Host "Docs site guards failed." -ForegroundColor Red
    Pop-Location
    exit 1
} else {
    Write-Host "Docs site guards passed." -ForegroundColor Green
    Pop-Location
    exit 0
}
