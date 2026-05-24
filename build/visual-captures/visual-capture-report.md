# Admin Demo Visual Capture Report

Generated at: 2026-05-23 22:52:00Z
Command: .\tools\capture-admin-demo.ps1 -OutputDir build/visual-captures -ZipPath build/adaptivekt-admin-demo-visual-captures.zip

## Capture matrix

| Screen | Breakpoint | Width | Height | File | Size (bytes) |
|---|---|---|---|---|---|
| dashboard | compact | 420 | 900 | compact/dashboard-compact-420x900.png | 24581 |
| employees | compact | 420 | 900 | compact/employees-compact-420x900.png | 29739 |
| products | compact | 420 | 900 | compact/products-compact-420x900.png | 29353 |
| invoices | compact | 420 | 900 | compact/invoices-compact-420x900.png | 29413 |
| settings | compact | 420 | 900 | compact/settings-compact-420x900.png | 23224 |
| components | compact | 420 | 900 | compact/components-compact-420x900.png | 36595 |
| components-buttons | compact | 420 | 900 | compact/components-buttons-compact-420x900.png | 30443 |
| components-badges | compact | 420 | 900 | compact/components-badges-compact-420x900.png | 21793 |
| components-avatars | compact | 420 | 900 | compact/components-avatars-compact-420x900.png | 21829 |
| components-cards | compact | 420 | 900 | compact/components-cards-compact-420x900.png | 32561 |
| components-dropdowns | compact | 420 | 900 | compact/components-dropdowns-compact-420x900.png | 24772 |
| components-fields | compact | 420 | 900 | compact/components-fields-compact-420x900.png | 27680 |
| dashboard | medium | 720 | 900 | medium/dashboard-medium-720x900.png | 53197 |
| employees | medium | 720 | 900 | medium/employees-medium-720x900.png | 46986 |
| products | medium | 720 | 900 | medium/products-medium-720x900.png | 46233 |
| invoices | medium | 720 | 900 | medium/invoices-medium-720x900.png | 46033 |
| settings | medium | 720 | 900 | medium/settings-medium-720x900.png | 38159 |
| components | medium | 720 | 900 | medium/components-medium-720x900.png | 65198 |
| components-buttons | medium | 720 | 900 | medium/components-buttons-medium-720x900.png | 43432 |
| components-badges | medium | 720 | 900 | medium/components-badges-medium-720x900.png | 33546 |
| components-avatars | medium | 720 | 900 | medium/components-avatars-medium-720x900.png | 33523 |
| components-cards | medium | 720 | 900 | medium/components-cards-medium-720x900.png | 46081 |
| components-dropdowns | medium | 720 | 900 | medium/components-dropdowns-medium-720x900.png | 36667 |
| components-fields | medium | 720 | 900 | medium/components-fields-medium-720x900.png | 40293 |
| dashboard | expanded | 1000 | 900 | expanded/dashboard-expanded-1000x900.png | 59010 |
| employees | expanded | 1000 | 900 | expanded/employees-expanded-1000x900.png | 53135 |
| products | expanded | 1000 | 900 | expanded/products-expanded-1000x900.png | 52332 |
| invoices | expanded | 1000 | 900 | expanded/invoices-expanded-1000x900.png | 52170 |
| settings | expanded | 1000 | 900 | expanded/settings-expanded-1000x900.png | 42263 |
| components | expanded | 1000 | 900 | expanded/components-expanded-1000x900.png | 74141 |
| components-buttons | expanded | 1000 | 900 | expanded/components-buttons-expanded-1000x900.png | 49878 |
| components-badges | expanded | 1000 | 900 | expanded/components-badges-expanded-1000x900.png | 38920 |
| components-avatars | expanded | 1000 | 900 | expanded/components-avatars-expanded-1000x900.png | 38658 |
| components-cards | expanded | 1000 | 900 | expanded/components-cards-expanded-1000x900.png | 52106 |
| components-dropdowns | expanded | 1000 | 900 | expanded/components-dropdowns-expanded-1000x900.png | 42489 |
| components-fields | expanded | 1000 | 900 | expanded/components-fields-expanded-1000x900.png | 46312 |
| dashboard | large | 1440 | 900 | large/dashboard-large-1440x900.png | 61589 |
| employees | large | 1440 | 900 | large/employees-large-1440x900.png | 63826 |
| products | large | 1440 | 900 | large/products-large-1440x900.png | 62042 |
| invoices | large | 1440 | 900 | large/invoices-large-1440x900.png | 60825 |
| settings | large | 1440 | 900 | large/settings-large-1440x900.png | 46092 |
| components | large | 1440 | 900 | large/components-large-1440x900.png | 82539 |
| components-buttons | large | 1440 | 900 | large/components-buttons-large-1440x900.png | 53624 |
| components-badges | large | 1440 | 900 | large/components-badges-large-1440x900.png | 42813 |
| components-avatars | large | 1440 | 900 | large/components-avatars-large-1440x900.png | 42461 |
| components-cards | large | 1440 | 900 | large/components-cards-large-1440x900.png | 56185 |
| components-dropdowns | large | 1440 | 900 | large/components-dropdowns-large-1440x900.png | 46366 |
| components-fields | large | 1440 | 900 | large/components-fields-large-1440x900.png | 49786 |

## Output

- Output directory: build/visual-captures
- Manifest: build/visual-captures/manifest.json
- Report: build/visual-captures/visual-capture-report.md
- Zip: build/adaptivekt-admin-demo-visual-captures.zip

## Notes

- El script usa AWT Robot para capturar la ventana dedicada de admin-demo.
- Requiere una sesion grafica real y una pantalla activa.
- Use -ComponentsOnly para capturar solo la pantalla UI Kit y sus secciones enfocadas.
- No captura la pantalla completa ni otra app.
- Si alguna captura falla, el script aborta por defecto.
