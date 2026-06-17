$ErrorActionPreference = "Stop"
cd examples/communication-suite-demo
.\gradlew clean build --no-daemon --console=plain --stacktrace
.\gradlew wasmJsBrowserDistribution --no-daemon --console=plain --stacktrace
cd ..\..
.\gradlew build --no-daemon --console=plain --stacktrace

.\tools\prepare-pages-site.ps1
.\scripts\check-communication-suite-guards.ps1
.\scripts\check-docs-site-guards.ps1
.\scripts\check-overlay-and-selection-guards.ps1
.\scripts\check-component-interaction-guards.ps1
.\tools\validate-component-routes.ps1 -SkipBuild
.\tools\validate-communication-suite-routes.ps1 -SkipBuild
.\tools\capture-communication-suite.ps1 -SkipBuild
.\tools\check-site-links.ps1
Write-Host "All verification steps completed successfully!"
