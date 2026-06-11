param(
    [string]$OutputDir = "artifacts\screenshots\ai-workspace-premium-refactor",
    [string]$BaseUrl = "",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

function Wait-LocalSite {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Url,
        [int]$TimeoutSeconds = 45
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    $lastError = $null

    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 2
            if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 500) {
                Write-Host "Local site is ready at $Url" -ForegroundColor Green
                return
            }
        } catch {
            $lastError = $_.Exception.Message
        }

        Start-Sleep -Milliseconds 500
    }

    throw "Timed out waiting for local site at $Url. Last error: $lastError"
}

if (-not $SkipBuild) {
    Write-Host "Preparing Pages site..." -ForegroundColor Cyan
    .\tools\prepare-pages-site.ps1
}

if (-not (Test-Path "site-dist\examples\ai-workspace\index.html")) {
    throw "site-dist AI Workspace route is missing. Run tools\prepare-pages-site.ps1 first or omit -SkipBuild."
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
    Wait-LocalSite -Url "$captureBaseUrl/"
}

try {
    if (Test-Path $OutputDir) {
        Remove-Item -LiteralPath $OutputDir -Recurse -Force
    }
    New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

    Write-Host "Running AI Workspace Playwright captures..." -ForegroundColor Cyan
    Push-Location tools\docs-site-capture
    node .\capture-ai-workspace-premium.js $captureBaseUrl (Join-Path $root $OutputDir)
    $captureExitCode = $LASTEXITCODE
    Pop-Location
    if ($captureExitCode -ne 0) {
        throw "AI Workspace Playwright capture failed with exit code $captureExitCode."
    }

    $sheetFiles = @(
        "tablet-chats-light.png",
        "tablet-chats-dark.png",
        "desktop-chats-light.png",
        "desktop-chats-dark.png",
        "large-chats-light.png",
        "large-chats-dark.png",
        "desktop-chat-detail-light.png",
        "desktop-chat-detail-dark.png",
        "tablet-prompts-light.png",
        "tablet-prompts-dark.png",
        "desktop-prompts-light.png",
        "desktop-prompts-dark.png",
        "large-prompts-light.png",
        "large-prompts-dark.png",
        "desktop-prompt-detail-light.png",
        "desktop-prompt-detail-dark.png"
    )

    $resolvedOutputDir = Resolve-Path $OutputDir
    $sheetPath = Join-Path $resolvedOutputDir "contact-sheet-pane-lists.png"
    $existingFiles = $sheetFiles |
        ForEach-Object { Join-Path $resolvedOutputDir $_ } |
        Where-Object { Test-Path $_ }

    if ($existingFiles.Count -gt 0) {
        Add-Type -AssemblyName System.Drawing

        $thumbWidth = 420
        $labelHeight = 34
        $gap = 16
        $columns = 4
        $items = @()

        foreach ($file in $existingFiles) {
            $image = [System.Drawing.Image]::FromFile($file)
            $scale = $thumbWidth / [double]$image.Width
            $thumbHeight = [int][Math]::Round($image.Height * $scale)
            $items += [pscustomobject]@{
                File = $file
                Name = Split-Path $file -Leaf
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

        Write-Host "Pane list contact sheet saved at $sheetPath" -ForegroundColor Green
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

Write-Host "AI Workspace captures saved in $OutputDir" -ForegroundColor Green
