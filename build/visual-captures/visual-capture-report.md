# Admin Demo Visual Capture Report

Generated at: 2026-05-26 09:25:26Z
Command: .\tools\capture-admin-demo.ps1 -OutputDir build/visual-captures -ZipPath build/adaptivekt-admin-demo-visual-captures.zip

## Capture matrix

| Screen | Breakpoint | Width | Height | File | Size (bytes) |
|---|---|---|---|---|---|
| dashboard | compact | 420 | 900 | compact/dashboard-compact-420x900.png | 27961 |
| employees | compact | 420 | 900 | compact/employees-compact-420x900.png | 42073 |
| products | compact | 420 | 900 | compact/products-compact-420x900.png | 47692 |
| invoices | compact | 420 | 900 | compact/invoices-compact-420x900.png | 32488 |
| settings | compact | 420 | 900 | compact/settings-compact-420x900.png | 26105 |
| components | compact | 420 | 900 | compact/components-compact-420x900.png | 36586 |
| components-buttons | compact | 420 | 900 | compact/components-buttons-compact-420x900.png | 30588 |
| components-badges | compact | 420 | 900 | compact/components-badges-compact-420x900.png | 22105 |
| components-avatars | compact | 420 | 900 | compact/components-avatars-compact-420x900.png | 22194 |
| components-cards | compact | 420 | 900 | compact/components-cards-compact-420x900.png | 32973 |
| components-dropdowns | compact | 420 | 900 | compact/components-dropdowns-compact-420x900.png | 24947 |
| components-fields | compact | 420 | 900 | compact/components-fields-compact-420x900.png | 28013 |
| dashboard | medium | 720 | 900 | medium/dashboard-medium-720x900.png | 65934 |
| employees | medium | 720 | 900 | medium/employees-medium-720x900.png | 69126 |
| products | medium | 720 | 900 | medium/products-medium-720x900.png | 75066 |
| invoices | medium | 720 | 900 | medium/invoices-medium-720x900.png | 59397 |
| settings | medium | 720 | 900 | medium/settings-medium-720x900.png | 53733 |
| components | medium | 720 | 900 | medium/components-medium-720x900.png | 80956 |
| components-buttons | medium | 720 | 900 | medium/components-buttons-medium-720x900.png | 57211 |
| components-badges | medium | 720 | 900 | medium/components-badges-medium-720x900.png | 47839 |
| components-avatars | medium | 720 | 900 | medium/components-avatars-medium-720x900.png | 43746 |
| components-cards | medium | 720 | 900 | medium/components-cards-medium-720x900.png | 58770 |
| components-dropdowns | medium | 720 | 900 | medium/components-dropdowns-medium-720x900.png | 47635 |
| components-fields | medium | 720 | 900 | medium/components-fields-medium-720x900.png | 52271 |
| dashboard | expanded | 1000 | 900 | expanded/dashboard-expanded-1000x900.png | 79027 |
| employees | expanded | 1000 | 900 | expanded/employees-expanded-1000x900.png | 71997 |
| products | expanded | 1000 | 900 | expanded/products-expanded-1000x900.png | 72836 |
| invoices | expanded | 1000 | 900 | expanded/invoices-expanded-1000x900.png | 59132 |
| settings | expanded | 1000 | 900 | expanded/settings-expanded-1000x900.png | 48212 |
| components | expanded | 1000 | 900 | expanded/components-expanded-1000x900.png | 84899 |
| components-buttons | expanded | 1000 | 900 | expanded/components-buttons-expanded-1000x900.png | 61567 |
| components-badges | expanded | 1000 | 900 | expanded/components-badges-expanded-1000x900.png | 54075 |
| components-avatars | expanded | 1000 | 900 | expanded/components-avatars-expanded-1000x900.png | 51455 |
| components-cards | expanded | 1000 | 900 | expanded/components-cards-expanded-1000x900.png | 70920 |
| components-dropdowns | expanded | 1000 | 900 | expanded/components-dropdowns-expanded-1000x900.png | 59169 |
| components-fields | expanded | 1000 | 900 | expanded/components-fields-expanded-1000x900.png | 63949 |
| dashboard | large | 1440 | 900 | large/dashboard-large-1440x900.png | 67150 |
| employees | large | 1440 | 900 | large/employees-large-1440x900.png | 80292 |
| products | large | 1440 | 900 | large/products-large-1440x900.png | 87944 |
| invoices | large | 1440 | 900 | large/invoices-large-1440x900.png | 59937 |
| settings | large | 1440 | 900 | large/settings-large-1440x900.png | 51997 |
| components | large | 1440 | 900 | large/components-large-1440x900.png | 80625 |
| components-buttons | large | 1440 | 900 | large/components-buttons-large-1440x900.png | 51791 |
| components-badges | large | 1440 | 900 | large/components-badges-large-1440x900.png | 41526 |
| components-avatars | large | 1440 | 900 | large/components-avatars-large-1440x900.png | 41200 |
| components-cards | large | 1440 | 900 | large/components-cards-large-1440x900.png | 54905 |
| components-dropdowns | large | 1440 | 900 | large/components-dropdowns-large-1440x900.png | 50155 |
| components-fields | large | 1440 | 900 | large/components-fields-large-1440x900.png | 58392 |

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
