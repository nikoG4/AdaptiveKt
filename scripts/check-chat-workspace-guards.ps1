param(
    [string]$SourceDir = "examples/communication-suite-demo/src"
)

$ErrorActionPreference = "Stop"
$workspaceRoot = Split-Path -Parent $PSScriptRoot
Push-Location $workspaceRoot
$failed = $false

Write-Host "Checking Chat Workspace Demo Guards..."

$patterns = @(
    "BoxWithConstraints",
    "breakpointForWidth",
    "import androidx.compose.foundation.text.selection.SelectionContainer",
    "import androidx.compose.ui.window.Dialog",
    "import androidx.compose.material.Button",
    "import androidx.compose.material3.Button",
    "import androidx.compose.material.Card",
    "import androidx.compose.material3.Card",
    "import androidx.compose.material.TextField",
    "import androidx.compose.material3.TextField",
    "TODO()",
    "error(`"not implemented`")",
    "NotImplementedError"
)

foreach ($pattern in $patterns) {
    $matches = Get-ChildItem -Path "$SourceDir\*" -Recurse -Include *.kt -ErrorAction SilentlyContinue | Select-String -Pattern $pattern -ErrorAction SilentlyContinue
    if ($matches -ne $null) {
        Write-Host "ERROR: Found forbidden pattern in Chat Workspace: $pattern" -ForegroundColor Red
        $matches | ForEach-Object { Write-Host "$($_.Path):$($_.LineNumber)" }
        $failed = $true
    }
}

if ($failed) {
    Write-Host "Chat Workspace guards failed." -ForegroundColor Red
    Pop-Location
    exit 1
} else {
    Write-Host "Chat Workspace guards passed." -ForegroundColor Green
    Pop-Location
    exit 0
}
