# Admin Demo Visual Capture Report

Generated at: 2026-05-23 22:34:30Z
Command: .\tools\capture-admin-demo.ps1 -OutputDir build\visual-captures-components -ZipPath build\adaptivekt-components-showcase-captures.zip

## Capture matrix

| Screen | Breakpoint | Width | Height | File | Size (bytes) |
|---|---|---|---|---|---|
| components | compact | 420 | 900 | compact/components-compact-420x900.png | 34234 |
| components-buttons | compact | 420 | 900 | compact/components-buttons-compact-420x900.png | 28171 |
| components-badges | compact | 420 | 900 | compact/components-badges-compact-420x900.png | 19409 |
| components-avatars | compact | 420 | 900 | compact/components-avatars-compact-420x900.png | 19471 |
| components-cards | compact | 420 | 900 | compact/components-cards-compact-420x900.png | 30061 |
| components-dropdowns | compact | 420 | 900 | compact/components-dropdowns-compact-420x900.png | 22375 |
| components-fields | compact | 420 | 900 | compact/components-fields-compact-420x900.png | 25343 |
| components | medium | 720 | 900 | medium/components-medium-720x900.png | 61103 |
| components-buttons | medium | 720 | 900 | medium/components-buttons-medium-720x900.png | 39226 |
| components-badges | medium | 720 | 900 | medium/components-badges-medium-720x900.png | 29408 |
| components-avatars | medium | 720 | 900 | medium/components-avatars-medium-720x900.png | 29369 |
| components-cards | medium | 720 | 900 | medium/components-cards-medium-720x900.png | 41902 |
| components-dropdowns | medium | 720 | 900 | medium/components-dropdowns-medium-720x900.png | 32420 |
| components-fields | medium | 720 | 900 | medium/components-fields-medium-720x900.png | 36147 |
| components | expanded | 1000 | 900 | expanded/components-expanded-1000x900.png | 69710 |
| components-buttons | expanded | 1000 | 900 | expanded/components-buttons-expanded-1000x900.png | 45549 |
| components-badges | expanded | 1000 | 900 | expanded/components-badges-expanded-1000x900.png | 34557 |
| components-avatars | expanded | 1000 | 900 | expanded/components-avatars-expanded-1000x900.png | 34325 |
| components-cards | expanded | 1000 | 900 | expanded/components-cards-expanded-1000x900.png | 47941 |
| components-dropdowns | expanded | 1000 | 900 | expanded/components-dropdowns-expanded-1000x900.png | 38073 |
| components-fields | expanded | 1000 | 900 | expanded/components-fields-expanded-1000x900.png | 41944 |
| components | large | 1440 | 900 | large/components-large-1440x900.png | 78744 |
| components-buttons | large | 1440 | 900 | large/components-buttons-large-1440x900.png | 50044 |
| components-badges | large | 1440 | 900 | large/components-badges-large-1440x900.png | 39200 |
| components-avatars | large | 1440 | 900 | large/components-avatars-large-1440x900.png | 38846 |
| components-cards | large | 1440 | 900 | large/components-cards-large-1440x900.png | 52598 |
| components-dropdowns | large | 1440 | 900 | large/components-dropdowns-large-1440x900.png | 42765 |
| components-fields | large | 1440 | 900 | large/components-fields-large-1440x900.png | 46420 |

## Output

- Output directory: build\visual-captures-components
- Manifest: build\visual-captures-components/manifest.json
- Report: build\visual-captures-components/visual-capture-report.md
- Zip: build\adaptivekt-components-showcase-captures.zip

## Notes

- El script usa AWT Robot para capturar la ventana dedicada de admin-demo.
- Requiere una sesion grafica real y una pantalla activa.
- Use -ComponentsOnly para capturar solo la pantalla UI Kit y sus secciones enfocadas.
- No captura la pantalla completa ni otra app.
- Si alguna captura falla, el script aborta por defecto.
