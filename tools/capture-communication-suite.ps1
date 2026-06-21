param(
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"
$workspaceRoot = Split-Path -Parent $PSScriptRoot

Push-Location $workspaceRoot

if (-not $SkipBuild) {
    Write-Host "Building Pages site..."
    .\tools\prepare-pages-site.ps1
}

Write-Host "Starting local server..."
Start-Job -Name "HttpServer" -ScriptBlock {
    python -m http.server 8080 --directory "$using:workspaceRoot\site-dist"
}

Start-Sleep -Seconds 3

try {
    Write-Host "Installing Playwright dependencies..."
    Push-Location tools\docs-site-capture
    npm install
    npx playwright install chromium
    
    $outDir = Join-Path $workspaceRoot "artifacts/screenshots/communication-suite"

    if (Test-Path $outDir) {
        Remove-Item -Recurse -Force $outDir
    }

    New-Item -ItemType Directory -Force -Path $outDir | Out-Null
    
    Write-Host "Running visual capture..."
    node capture-communication-suite.js `
        "http://localhost:8080/examples/communication-suite" `
        "$outDir"

    if ($LASTEXITCODE -ne 0) {
        throw "Capture script failed with exit code $LASTEXITCODE"
    }

    $requiredContactSheets = @(
        "contact-sheet-adaptive-chat-light.png",
        "contact-sheet-adaptive-chat-dark.png",
        "contact-sheet-adaptive-chat-chat-contacts.png",
        "contact-sheet-adaptive-chat-calls-settings.png",
        "contact-sheet-adaptive-chat-mobile-desktop.png"
    )

    foreach ($file in $requiredContactSheets) {
        $filePath = Join-Path $outDir $file

        if (-not (Test-Path $filePath)) {
            throw "Missing required contact sheet: $file"
        }

        $fileInfo = Get-Item $filePath

        if ($fileInfo.Length -le 0) {
            throw "Required contact sheet is empty: $file"
        }
    }

    $allPngs = Get-ChildItem -Path $outDir -Filter "*.png" -File

    $stateScreenshots = $allPngs | Where-Object {
        $_.Name -notlike "contact-sheet-*"
    }

    $contactSheets = $allPngs | Where-Object {
        $_.Name -like "contact-sheet-*"
    }

    if ($stateScreenshots.Count -ne 200) {
        throw "Expected 200 state screenshots, found $($stateScreenshots.Count)"
    }

    if ($contactSheets.Count -ne 5) {
        throw "Expected 5 contact sheets, found $($contactSheets.Count)"
    }

    if ($allPngs.Count -ne 205) {
        throw "Expected 205 total PNG files, found $($allPngs.Count)"
    }

    $emptyFiles = $allPngs | Where-Object { $_.Length -le 0 }

    if ($emptyFiles.Count -gt 0) {
        $names = ($emptyFiles | Select-Object -ExpandProperty Name) -join ", "
        throw "Empty PNG files detected: $names"
    }
} finally {
    Pop-Location
    Write-Host "Stopping local server..."
    Stop-Job -Name "HttpServer"
    Remove-Job -Name "HttpServer"
    Pop-Location
}
