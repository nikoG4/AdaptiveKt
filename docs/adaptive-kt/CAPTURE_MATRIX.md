# Matriz de Capturas de Admin Demo

**Fecha:** 2026-05-22

## Screens Capturados

| Screen | Compact (420x900) | Large (1440x900) |
|--------|---|---|
| Dashboard | `build/visual-captures/dashboard-compact.png` | `build/visual-captures/dashboard-large.png` |
| Employees | `build/visual-captures/employees-compact.png` | `build/visual-captures/employees-large.png` |
| Products | `build/visual-captures/products-compact.png` | `build/visual-captures/products-large.png` |
| Invoices | `build/visual-captures/invoices-compact.png` | `build/visual-captures/invoices-large.png` |
| Settings | `build/visual-captures/settings-compact.png` | `build/visual-captures/settings-large.png` |
| Account Menu | `build/visual-captures/large/account-menu-large-1440x900.png` | `build/visual-captures/large/account-menu-large-1440x900.png` |

## Capture Commands

### Single Capture

```bash
.\gradlew.bat :admin-demo:run --args="--capture --screen dashboard --width 1440 --height 900 --output build/visual-captures/dashboard-large.png --delayMs 1500"
```

### Account Menu Open

```bash
.\gradlew.bat :admin-demo:run --args="--capture --screen dashboard --width 1440 --height 900 --output build/visual-captures/large/account-menu-large-1440x900.png --accountMenuOpen"
```

### Batch Capture Script

```powershell
.\tools\capture-admin-demo.ps1
```

## States Tested

### Invoices Screen

| State | Trigger |
|-------|---|
| Content | Default |
| Empty | Toggle "Empty" |
| Loading | Toggle "Loading" |
| Error | Toggle "Error" |

### Toggles per Capture

- [ ] Dashboard (KPI cards, panels)
- [ ] Employees (table con avatar, acciones)
- [ ] Products (table con thumbnail, acciones)
- [ ] Invoices (table, estado content)
- [ ] Settings (formulario)

---

## Capturas Pendientes (Futuras)

| Capture | Motivación |
|---------|-------------|
| Medium (720x900) | Verificar 2-column layout |
| Expanded (1000x900) | Verificar 4-column layout |
| Mobile (390x844) | Validar responsive |
| Tablet (768x1024) | Validar responsive |

---

## Análisis de Capturas

### Dashboard

| Breakpoint | Cards por fila | Observación |
|---|---|---|
| Compact | 1 | Full-width cards |
| Medium | 2 | Layout 2 columnas |
| Large | 4 | Layout 4 columnas |

### Employees/Products/Invoices

| Breakpoint | Layout | Acción visible |
|---|---|---|
| Compact | Cards | Primary action visible |
| Medium/Expanded | Cards | Primary + overflow |
| Large | Table | Header + separadores + acciones |

### Settings

| Breakpoint | Layout | Observación |
|---|---|---|
| Compact | 1 column | Form vertical |
| Medium | 2 column | Labels top |
| Large | 2 column | Form compacto |
