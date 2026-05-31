param(
    [string]$OutputDir = "build/visual-captures",
    [string]$ZipPath = "build/adaptivekt-admin-demo-visual-captures.zip",
    [switch]$AbortOnFailure = $true,
    [switch]$ComponentsOnly = $false
)

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

$gradle = Join-Path $root "gradlew.bat"
$visualRoot = Join-Path $root $OutputDir
$moduleOutputRoot = Join-Path (Join-Path $root "admin-demo") "build\visual-captures"
$manifestPath = Join-Path $visualRoot "manifest.json"
$reportPath = Join-Path $visualRoot "visual-capture-report.md"
$zipFullPath = Join-Path $root $ZipPath

function Run-Gradle {
    param(
        [string]$task,
        [string]$gradleArgs
    )

    $arg = '--args=' + $gradleArgs
    Write-Host "Running Gradle command: $gradle $task $arg"

    $stdoutFile = [System.IO.Path]::GetTempFileName()
    $stderrFile = [System.IO.Path]::GetTempFileName()

    & $gradle $task $arg > $stdoutFile 2> $stderrFile
    $exitCode = $LASTEXITCODE

    if (Test-Path $stdoutFile) {
        Get-Content $stdoutFile | ForEach-Object { Write-Host $_ }
    }
    if (Test-Path $stderrFile) {
        Get-Content $stderrFile | ForEach-Object { Write-Host $_ }
    }

    Remove-Item $stdoutFile, $stderrFile -ErrorAction SilentlyContinue
    return $exitCode
}

$standardScreens = @(
    "dashboard",
    "employees",
    "products",
    "invoices",
    "invoices-empty",
    "invoices-loading",
    "invoices-error",
    "settings",
    "components"
)

$componentScreens = @(
    "components-buttons",
    "components-badges",
    "components-avatars",
    "components-cards",
    "components-dropdowns",
    "components-fields",
    "components-selects",
    "components-selects-open",
    "components-multiselects",
    "components-multiselects-open",
    "components-carousels",
    "components-navigation-tree",
    "components-feedback"
)

$screens = if ($ComponentsOnly) {
    @("components") + $componentScreens
} else {
    $standardScreens + $componentScreens
}

$breakpoints = @(
    @{ name = "compact"; width = 420; height = 900 },
    @{ name = "medium"; width = 720; height = 900 },
    @{ name = "expanded"; width = 1000; height = 900 },
    @{ name = "large"; width = 1440; height = 900 }
)

if (Test-Path $visualRoot) {
    Write-Host "Cleaning existing output directory: $visualRoot"
    Remove-Item -Recurse -Force $visualRoot
}

New-Item -ItemType Directory -Force -Path $visualRoot | Out-Null

if (Test-Path $zipFullPath) {
    Remove-Item -Force $zipFullPath
}

$captures = @()
$total = $screens.Count * $breakpoints.Count
$index = 0

foreach ($bp in $breakpoints) {
    $breakpointDir = Join-Path $visualRoot $bp.name
    New-Item -ItemType Directory -Force -Path $breakpointDir | Out-Null

    foreach ($screen in $screens) {
        $index++
        $fileName = "$screen-$($bp.name)-$($bp.width)x$($bp.height).png"
        $moduleRelativeOutput = Join-Path "build\visual-captures" $bp.name
        $moduleRelativeOutput = Join-Path $moduleRelativeOutput $fileName
        $moduleOutputFile = Join-Path (Join-Path $root "admin-demo") $moduleRelativeOutput
        $outputFile = Join-Path $breakpointDir $fileName

        $command = "--capture --screen $screen --width $($bp.width) --height $($bp.height) --output $moduleRelativeOutput --delayMs 1500"

        Write-Host "[$index/$total] Capturing $screen $($bp.name) $($bp.width)x$($bp.height)..."
        Write-Host "Module output arg: $moduleRelativeOutput"
        Write-Host "Final copy to: $outputFile"

        $gradleArg = $command
        Write-Host "Gradle program args: $gradleArg"

        $exitCode = Run-Gradle ":admin-demo:run" $gradleArg
        if ($exitCode -ne 0) {
            Write-Error "Capture failed for $screen $($bp.name)"
            if ($AbortOnFailure) { exit $exitCode }
            continue
        }

        if (-not (Test-Path $moduleOutputFile)) {
            Write-Error "Expected module output file not found: $moduleOutputFile"
            if ($AbortOnFailure) { exit 1 }
            continue
        }

        if (-not (Test-Path $outputFile)) {
            Copy-Item -Path $moduleOutputFile -Destination $outputFile -Force
        }

        $fileInfo = Get-Item $outputFile
        $capturedAt = (Get-Date).ToString("o")

        $captures += [PSCustomObject]@{
            screen = $screen
            breakpoint = $bp.name
            width = $bp.width
            height = $bp.height
            file = "$($bp.name)/$fileName"
            fileSizeBytes = $fileInfo.Length
            capturedAt = $capturedAt
            command = $command
        }

        Write-Host "OK $OutputDir/$($bp.name)/$fileName ($($fileInfo.Length) bytes)"
    }
}

$manifest = [PSCustomObject]@{
    generatedAt = (Get-Date).ToString("o")
    total = $captures.Count
    captures = $captures
}

$manifestJson = $manifest | ConvertTo-Json -Depth 5
$manifestJson | Set-Content -Path $manifestPath -Encoding UTF8

$reportLines = @()
$reportLines += "# Admin Demo Visual Capture Report"
$reportLines += ""
$reportLines += "Generated at: $(Get-Date -Format u)"
$reportLines += "Command: .\tools\capture-admin-demo.ps1 -OutputDir $OutputDir -ZipPath $ZipPath"
$reportLines += ""
$reportLines += "## Capture matrix"
$reportLines += ""
$reportLines += "| Screen | Breakpoint | Width | Height | File | Size (bytes) |"
$reportLines += "|---|---|---|---|---|---|"

foreach ($capture in $captures) {
    $reportLines += "| $($capture.screen) | $($capture.breakpoint) | $($capture.width) | $($capture.height) | $($capture.file) | $($capture.fileSizeBytes) |"
}

$reportLines += ""
$reportLines += "## Output"
$reportLines += ""
$reportLines += "- Output directory: $OutputDir"
$reportLines += "- Manifest: $OutputDir/manifest.json"
$reportLines += "- Report: $OutputDir/visual-capture-report.md"
$reportLines += "- Zip: $ZipPath"
$reportLines += ""
$reportLines += "## Notes"
$reportLines += ""
$reportLines += "- El script usa AWT Robot para capturar la ventana dedicada de admin-demo."
$reportLines += "- Requiere una sesion grafica real y una pantalla activa."
$reportLines += "- Use -ComponentsOnly para capturar solo la pantalla UI Kit y sus secciones enfocadas."
$reportLines += "- No captura la pantalla completa ni otra app."
$reportLines += "- Si alguna captura falla, el script aborta por defecto."

$reportLines | Set-Content -Path $reportPath -Encoding UTF8

Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::CreateFromDirectory($visualRoot, $zipFullPath)

if (-not (Test-Path $zipFullPath)) {
    Write-Error "ZIP file was not created: $zipFullPath"
    exit 1
}

Write-Host ""
Write-Host "Total screenshots: $($captures.Count)"
Write-Host "Manifest: $manifestPath"
Write-Host "Report: $reportPath"
Write-Host "Zip: $zipFullPath"

exit 0
