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

Write-Host "Running DocsRegistry validation..."
python "$workspaceRoot/scripts/check-docs-site-registry.py"
if ($LASTEXITCODE -ne 0) { $failed = $true }

if ($failed) {
    Write-Host "Docs site guards failed." -ForegroundColor Red
    Pop-Location
    exit 1
} else {
    Write-Host "Docs site guards passed." -ForegroundColor Green
    Pop-Location
    exit 0
}

