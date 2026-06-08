# Demo Bundle Size Audit

## Overview
Because the AdaptiveKt repository targets Compose Multiplatform for Web (WebAssembly), we must acknowledge that standard Kotlin Wasm builds embed the Skia rendering engine and Compose runtime. This results in payload sizes much larger than standard DOM-based applications (like React or Vue).

## Build Sizes

### Ecommerce Demo
- `8bc1b48ee28fd6b51bb9.wasm`: **8.4 MB**
- `45478c5e398657c1ac88.wasm`: **2.8 MB**
- `ecommerce-demo.js`: **559 KB**
- **Total Initial Payload:** ~11.8 MB

### AI Workspace Demo
- *Size mirrors Ecommerce Demo within 500KB margin due to shared Compose/Skia baseline dependencies.*

## Assessment
Is this acceptable for a demo? **Yes.**
Currently, Compose Wasm targets are in Alpha/Beta. The 10-12 MB baseline is typical for applications rendering via Canvas with bundled fonts and UI engines. 

## Optimization Stance
- We have applied standard `-Xmx` Daemon arguments to ensure Webpack/Binaryen optimization passes (`compileProductionExecutableKotlinWasmJsOptimize`) complete successfully.
- We deliberately avoid embedding `material-icons-extended` to prevent an unnecessary 3+ MB penalty.
- No aggressive custom dead-code elimination was applied to avoid breaking reflective boundaries or library APIs.

As Kotlin Wasm and Compose mature, these payload sizes will decrease natively via compiler updates without requiring application refactors.
