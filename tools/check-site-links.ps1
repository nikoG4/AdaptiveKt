param(
    [string]$DistDir = "site-dist"
)

$ErrorActionPreference = "Stop"

$root = Resolve-Path "$PSScriptRoot\.."
Set-Location $root

$distPath = Resolve-Path $DistDir
$htmlFiles = Get-ChildItem -Path $distPath -Filter "*.html" -Recurse
$hrefPattern = 'href\s*=\s*["'']([^"'']*)["'']'
$ok = New-Object System.Collections.Generic.List[string]
$warnings = New-Object System.Collections.Generic.List[string]
$broken = New-Object System.Collections.Generic.List[string]

function Test-InternalTarget {
    param(
        [string]$SourceFile,
        [string]$Href,
        [string]$BaseHref
    )

    $target = $Href.Split('#')[0].Split('?')[0]
    if ([string]::IsNullOrWhiteSpace($target)) {
        $warnings.Add("$SourceFile -> $Href is an empty or anchor-only link")
        return
    }

    if ($target -match '^(https?:|mailto:|tel:|javascript:)') {
        $warnings.Add("$SourceFile -> $Href is external or protocol-based; skipped")
        return
    }

    if ($target -eq "#") {
        $warnings.Add("$SourceFile -> # is a placeholder link")
        return
    }

    $effectiveTarget = $target
    $normalizedBaseHref = if ([string]::IsNullOrWhiteSpace($BaseHref)) { "/" } else { $BaseHref.Trim() }
    if (-not $normalizedBaseHref.EndsWith("/")) {
        $normalizedBaseHref = "$normalizedBaseHref/"
    }

    if (-not $target.StartsWith("/") -and $normalizedBaseHref.StartsWith("/")) {
        $effectiveTarget = "$normalizedBaseHref$target"
    }

    $candidate = if ($effectiveTarget.StartsWith("/")) {
        $localTarget = $effectiveTarget
        if ($normalizedBaseHref -ne "/" -and $localTarget.StartsWith($normalizedBaseHref, [System.StringComparison]::OrdinalIgnoreCase)) {
            $localTarget = $localTarget.Substring($normalizedBaseHref.Length)
        }
        Join-Path $distPath.Path $localTarget.TrimStart("/")
    } else {
        Join-Path (Split-Path $SourceFile -Parent) $effectiveTarget
    }

    if ($effectiveTarget.EndsWith("/")) {
        $candidate = Join-Path $candidate "index.html"
    } elseif (-not [System.IO.Path]::HasExtension($candidate)) {
        $asDirectoryIndex = Join-Path $candidate "index.html"
        if (Test-Path $asDirectoryIndex) {
            $candidate = $asDirectoryIndex
        }
    }

    if (Test-Path $candidate) {
        $ok.Add("$SourceFile -> $Href")
    } else {
        $broken.Add("$SourceFile -> $Href expected $candidate")
    }
}

foreach ($file in $htmlFiles) {
    $content = Get-Content -Raw -Path $file.FullName
    $baseMatch = [regex]::Match($content, '<base\s+href\s*=\s*["'']([^"'']+)["'']', [System.Text.RegularExpressions.RegexOptions]::IgnoreCase)
    $baseHref = if ($baseMatch.Success) { $baseMatch.Groups[1].Value } else { "/" }
    foreach ($match in [regex]::Matches($content, $hrefPattern, [System.Text.RegularExpressions.RegexOptions]::IgnoreCase)) {
        Test-InternalTarget -SourceFile $file.FullName -Href $match.Groups[1].Value -BaseHref $baseHref
    }
}

if (-not (Test-Path (Join-Path $distPath "demo\app\index.html"))) {
    $broken.Add("Required admin demo target is missing: demo/app/index.html")
} else {
    $ok.Add("Required admin demo target exists: demo/app/index.html")
}

Write-Host "Site link check" -ForegroundColor Cyan
Write-Host "OK: $($ok.Count)" -ForegroundColor Green
Write-Host "Warnings: $($warnings.Count)" -ForegroundColor Yellow
Write-Host "Broken: $($broken.Count)" -ForegroundColor $(if ($broken.Count -gt 0) { "Red" } else { "Green" })

if ($warnings.Count -gt 0) {
    Write-Host "`nWarnings:" -ForegroundColor Yellow
    $warnings | ForEach-Object { Write-Host "  $_" -ForegroundColor Yellow }
}

if ($broken.Count -gt 0) {
    Write-Host "`nBroken links:" -ForegroundColor Red
    $broken | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
    exit 1
}
