$ErrorActionPreference = "Stop"
$failed = $false

Write-Host "Running navigation guards..."

# 1. BackHandler should not be used manually inside demo screens
$demoFiles = Get-ChildItem -Path "examples" -Recurse -Filter "*.kt"

foreach ($file in $demoFiles) {
    if ($file.Name -in "AiWorkspaceApp.kt", "EcommerceApp.kt", "AdminDemoApp.kt") {
        continue
    }

    $content = Get-Content $file.FullName
    if ($content -match "BackHandler\(") {
        Write-Host "ERROR: Manual BackHandler found in $($file.FullName). Use AdaptiveNavigationBackHandler at root instead." -ForegroundColor Red
        $failed = $true
    }
    if ($content -match "androidx\.activity\.compose\.BackHandler") {
        Write-Host "ERROR: Direct BackHandler import found in $($file.FullName). Use AdaptiveNavigationBackHandler at root instead." -ForegroundColor Red
        $failed = $true
    }
}

# 2. Android-only imports in commonMain
$commonMainFiles = Get-ChildItem -Path "." -Recurse -Filter "*.kt" | Where-Object { $_.FullName -match "src\\commonMain" }

foreach ($file in $commonMainFiles) {
    $content = Get-Content $file.FullName
    if ($content -match "androidx\.activity") {
        Write-Host "ERROR: Android-only import found in commonMain: $($file.FullName)." -ForegroundColor Red
        $failed = $true
    }
}

if ($failed) {
    Write-Host "Navigation guard checks FAILED." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Navigation guard checks PASSED." -ForegroundColor Green
}
