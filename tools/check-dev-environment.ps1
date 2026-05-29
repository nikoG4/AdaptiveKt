param(
    [switch]$FailOnWarning = $false
)

$warnings = @()

function Add-WarningMessage {
    param([string]$Message)
    $script:warnings += $Message
    Write-Warning $Message
}

Write-Host "AdaptiveKt development environment check"
Write-Host ""

$javaHome = $env:JAVA_HOME
if ([string]::IsNullOrWhiteSpace($javaHome)) {
    Add-WarningMessage "JAVA_HOME is not set. AdaptiveKt expects JDK 17 for local Gradle builds."
} else {
    Write-Host "JAVA_HOME: $javaHome"
}

$javaCommand = Get-Command java -ErrorAction SilentlyContinue
if ($null -eq $javaCommand) {
    Add-WarningMessage "java was not found on PATH. Install JDK 17 and put JAVA_HOME\\bin first on PATH."
} else {
    Write-Host "java executable: $($javaCommand.Source)"

    $javaVersionOutput = & java -version 2>&1
    Write-Host "java -version:"
    $javaVersionOutput | ForEach-Object { Write-Host "  $_" }

    $versionLine = ($javaVersionOutput | Select-Object -First 1).ToString()
    $major = $null
    if ($versionLine -match '"(?<version>[0-9]+)(\.([0-9]+))?') {
        $first = [int]$Matches['version']
        if ($first -eq 1 -and $versionLine -match '"1\.(?<legacy>[0-9]+)') {
            $major = [int]$Matches['legacy']
        } else {
            $major = $first
        }
    }

    if ($null -eq $major) {
        Add-WarningMessage "Could not parse Java major version from: $versionLine"
    } elseif ($major -ne 17) {
        Add-WarningMessage "Detected Java $major. AdaptiveKt builds are validated with JDK 17; Kotlin/Gradle may fail with newer JDKs such as 25."
    } else {
        Write-Host "Java major version: 17 OK"
    }
}

Write-Host ""
if (Test-Path "local.properties") {
    Write-Host "local.properties: present"
    Write-Host "  Values are intentionally not printed."
} else {
    Write-Host "local.properties: not present"
}

$androidSdk = $env:ANDROID_HOME
if ([string]::IsNullOrWhiteSpace($androidSdk)) {
    $androidSdk = $env:ANDROID_SDK_ROOT
}

if ([string]::IsNullOrWhiteSpace($androidSdk)) {
    Add-WarningMessage "ANDROID_HOME/ANDROID_SDK_ROOT is not set. Android library tasks may need an installed Android SDK or local.properties with sdk.dir."
    Write-Host "Android SDK help:"
    Write-Host "  - Install Android Studio or Android command-line tools."
    Write-Host "  - Set ANDROID_HOME or ANDROID_SDK_ROOT for the current shell."
    Write-Host "  - Or create local.properties with sdk.dir=... locally; do not commit it."
} else {
    Write-Host "Android SDK env: configured"
}

Write-Host ""
if ($warnings.Count -eq 0) {
    Write-Host "Environment check completed: OK"
    exit 0
}

Write-Host "Environment check completed with $($warnings.Count) warning(s)."
Write-Host ""
Write-Host "Recommended Windows shell setup for this session:"
Write-Host '$env:JAVA_HOME="C:\Program Files\Java\jdk-17"'
Write-Host '$env:PATH="$env:JAVA_HOME\bin;$env:PATH"'
Write-Host '.\gradlew.bat build --console=plain --stacktrace'

if ($FailOnWarning) {
    exit 1
}

exit 0
