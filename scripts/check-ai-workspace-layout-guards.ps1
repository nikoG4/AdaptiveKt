param(
    [string]$SourceDir = "examples/ai-workspace-demo/src/commonMain/kotlin"
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
$sourcePath = Join-Path $root $SourceDir

if (-not (Test-Path $sourcePath)) {
    throw "AI Workspace source directory not found: $sourcePath"
}

$files = Get-ChildItem -Path $sourcePath -Recurse -Include *.kt

$hardPatterns = @(
    @{ Name = "BoxWithConstraints"; Pattern = "BoxWithConstraints" },
    @{ Name = "breakpointForWidth"; Pattern = "breakpointForWidth" },
    @{ Name = "BoxWithConstraints import"; Pattern = "import\s+androidx\.compose\.foundation\.layout\.BoxWithConstraints" },
    @{ Name = "Material Button import"; Pattern = "import\s+androidx\.compose\.material3\.Button" },
    @{ Name = "Material Card import"; Pattern = "import\s+androidx\.compose\.material3\.Card" },
    @{ Name = "Material TextField import"; Pattern = "import\s+androidx\.compose\.material3\.TextField" },
    @{ Name = "Material OutlinedTextField import"; Pattern = "import\s+androidx\.compose\.material3\.OutlinedTextField" },
    @{ Name = "Material wildcard import"; Pattern = "import\s+androidx\.compose\.material3\.\*" },
    @{ Name = "User-facing Button"; Pattern = "\bButton\s*\(" },
    @{ Name = "User-facing Card"; Pattern = "\bCard\s*\(" },
    @{ Name = "User-facing TextField"; Pattern = "\bTextField\s*\(" },
    @{ Name = "User-facing OutlinedTextField"; Pattern = "\bOutlinedTextField\s*\(" }
)

$warningPatterns = @(
    "LocalAdaptiveLayoutInfo.current",
    "AdaptiveGrid\s*\(\s*columns\s*=",
    "\bRow\s*\(",
    "\bColumn\s*\(",
    "\bBox\s*\(",
    "\bSpacer\s*\(",
    "\bText\s*\(",
    "\bLazyColumn\s*\(",
    "\bLazyRow\s*\("
)

$failed = $false
Write-Host "AI Workspace layout guard: $sourcePath" -ForegroundColor Cyan
Write-Host ""
Write-Host "Hard-banned patterns:" -ForegroundColor Cyan

foreach ($entry in $hardPatterns) {
    $matches = $files | Select-String -Pattern $entry.Pattern
    $count = @($matches).Count
    if ($count -gt 0) {
        $failed = $true
        Write-Host "[FAIL] $($entry.Name): $count" -ForegroundColor Red
        $matches | ForEach-Object {
            Write-Host "  $($_.Path):$($_.LineNumber): $($_.Line.Trim())" -ForegroundColor Red
        }
    } else {
        Write-Host "[OK] $($entry.Name): 0" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "Warning-only usage counts:" -ForegroundColor Cyan
foreach ($pattern in $warningPatterns) {
    $matches = $files | Select-String -Pattern $pattern
    Write-Host ("{0}: {1}" -f $pattern, @($matches).Count)
}

if ($failed) {
    throw "AI Workspace layout guard failed."
}

Write-Host ""
Write-Host "AI Workspace layout guard passed." -ForegroundColor Green

