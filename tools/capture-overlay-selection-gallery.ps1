param(
    [string]$OutputDir = "artifacts\screenshots\overlay-selection-gallery",
    [string]$BaseUrl = "",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

if (-not $SkipBuild) {
    Write-Host "Preparing Pages site..." -ForegroundColor Cyan
    .\tools\prepare-pages-site.ps1
}

if (-not (Test-Path "site-dist\components\index.html")) {
    throw "site-dist components route is missing. Run tools\prepare-pages-site.ps1 first or omit -SkipBuild."
}

Write-Host "Installing Playwright dependencies..." -ForegroundColor Cyan
Push-Location tools\docs-site-capture
npm install
npx playwright install chromium
Pop-Location

$serverProcess = $null
$captureBaseUrl = $BaseUrl

if ([string]::IsNullOrWhiteSpace($captureBaseUrl)) {
    $captureBaseUrl = "http://localhost:8080"
    Write-Host "Starting local site-dist server at $captureBaseUrl..." -ForegroundColor Cyan
    $serverProcess = Start-Process -FilePath python -ArgumentList @("-m", "http.server", "8080", "-d", "site-dist") -WindowStyle Hidden -PassThru
    Start-Sleep -Seconds 3
}

try {
    if (Test-Path $OutputDir) {
        Remove-Item -LiteralPath $OutputDir -Recurse -Force
    }
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

    Write-Host "Running overlay and selection Playwright captures..." -ForegroundColor Cyan
    Push-Location tools\docs-site-capture
    node .\capture-overlay-selection-gallery.js $captureBaseUrl (Join-Path $root $OutputDir)
    Pop-Location

    $resolvedOutputDir = Resolve-Path $OutputDir
    $sheetPath = Join-Path $resolvedOutputDir "contact-sheet-overlay-selection-gallery.png"
    $existingFiles = Get-ChildItem -LiteralPath $resolvedOutputDir -Filter "*.png" | Where-Object {
        $_.Name -ne "contact-sheet-overlay-selection-gallery.png"
    } | Sort-Object Name

    if ($existingFiles.Count -gt 0) {
        Add-Type -AssemblyName System.Drawing

        $thumbWidth = 420
        $labelHeight = 34
        $gap = 16
        $columns = 4
        $items = @()

        foreach ($file in $existingFiles) {
            $image = [System.Drawing.Image]::FromFile($file.FullName)
            $scale = $thumbWidth / [double]$image.Width
            $thumbHeight = [int][Math]::Round($image.Height * $scale)
            $items += [pscustomobject]@{
                File = $file.FullName
                Name = $file.Name
                Image = $image
                Width = $thumbWidth
                Height = $thumbHeight
            }
        }

        $rows = [int][Math]::Ceiling($items.Count / [double]$columns)
        $rowHeights = for ($row = 0; $row -lt $rows; $row++) {
            $start = $row * $columns
            $end = [Math]::Min($start + $columns - 1, $items.Count - 1)
            ($items[$start..$end] | Measure-Object -Property Height -Maximum).Maximum + $labelHeight
        }

        $sheetWidth = ($columns * $thumbWidth) + (($columns + 1) * $gap)
        $sheetHeight = ($rowHeights | Measure-Object -Sum).Sum + (($rows + 1) * $gap)
        $bitmap = New-Object System.Drawing.Bitmap $sheetWidth, $sheetHeight
        $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
        $graphics.Clear([System.Drawing.Color]::FromArgb(248, 250, 252))
        $font = New-Object System.Drawing.Font "Segoe UI", 12
        $brush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(15, 23, 42))

        $index = 0
        $y = $gap
        for ($row = 0; $row -lt $rows; $row++) {
            $x = $gap
            for ($column = 0; $column -lt $columns; $column++) {
                if ($index -ge $items.Count) { break }
                $item = $items[$index]
                $graphics.DrawString($item.Name, $font, $brush, $x, $y)
                $graphics.DrawImage($item.Image, $x, $y + $labelHeight, $item.Width, $item.Height)
                $x += $thumbWidth + $gap
                $index++
            }
            $y += $rowHeights[$row] + $gap
        }

        $bitmap.Save($sheetPath, [System.Drawing.Imaging.ImageFormat]::Png)

        $graphics.Dispose()
        $bitmap.Dispose()
        $brush.Dispose()
        $font.Dispose()
        foreach ($item in $items) {
            $item.Image.Dispose()
        }

        Write-Host "Overlay selection contact sheet saved at $sheetPath" -ForegroundColor Green
    }
}
finally {
    if ((Get-Location).Path -like "*tools\docs-site-capture") {
        Pop-Location
    }
    if ($serverProcess -and -not $serverProcess.HasExited) {
        Write-Host "Stopping local site-dist server..." -ForegroundColor Cyan
        Stop-Process -Id $serverProcess.Id -Force
    }
}

Write-Host "Overlay selection captures saved in $OutputDir" -ForegroundColor Green

