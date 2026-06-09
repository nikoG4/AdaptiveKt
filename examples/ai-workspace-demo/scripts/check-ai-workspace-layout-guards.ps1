$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$srcDir = Join-Path (Split-Path -Parent $scriptDir) "src\commonMain\kotlin\io\github\adaptivekt\examples\aiworkspace"
$files = Get-ChildItem -Path $srcDir -Recurse -Filter *.kt

$failed = $false

foreach ($file in $files) {
    $content = Get-Content $file.FullName
    $matches = $content | Select-String -Pattern "BoxWithConstraints", "breakpointForWidth", "LocalAdaptiveLayoutInfo\.current", "AdaptiveBreakpoint\.Compact", "Icons\.Default", "material\.icons", "🏠", "➡️", "➕"

    if ($matches) {
        Write-Host "ERROR: Layout violation found in $($file.Name)" -ForegroundColor Red
        foreach ($match in $matches) {
            Write-Host "  Line $($match.LineNumber): $($match.Line.Trim())" -ForegroundColor Yellow
        }
        $failed = $true
    }
}

if ($failed) {
    Write-Host "Layout Guard Test FAILED." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Layout Guard Test PASSED. No manual constraints found." -ForegroundColor Green
    exit 0
}
