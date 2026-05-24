# Audit Rápido de Colores Hardcodeados

**Fecha:** 2026-05-22

## Principio

`adaptive-core` debería proveer tokens de color (light/dark). Sin embargo, no se implementa dark mode aún, por lo que los colores están hardcodeados en los módulos.

## Colores Hardcodeados por Módulo

### :adaptive-data

Archivo: `adaptive-data/src/commonMain/kotlin/io/github/adaptivekt/data/AdaptiveDataView.kt`

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| DataSurface | `Color(0xFFFFFFFF)` | Background de tabla/card | Bajo (es white standard) |
| DataBorder | `Color(0xFFE2E8F0)` | Border de tabla/card | Medio |
| DataHeaderBackground | `Color(0xFFF1F5F9)` | Header background | Medio |
| DataText | `Color(0xFF0F172A)` | Text principal | Medio |
| DataMutedText | `Color(0xFF64748B)` | Text secundario | Medio |
| DataPrimary | `Color(0xFF2563EB)` | Color primario (buttons, accents) | Alto |
| DataPrimaryDark | `Color(0xFF1E40AF)` | Text primario oscuro (icon, overflow menu) | Alto |
| DataDanger | `Color(0xFFB91C1C)` | Danger text/border | Alto |

### admin-demo/ui/AdminDemoUi.kt

#### DemoButton

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Primary pressed | `Color(0xFF1D4ED8)` | Primary button pressed | Medio |
| Primary hovered | `Color(0xFF315FDC)` | Primary button hovered | Medio |
| Primary default | `Color(0xFF2563EB)` | Primary button default | Alto |
| Secondary pressed | `Color(0xFFE2E8F0)` | Secondary button pressed | Medio |
| Secondary hovered | `Color(0xFFF1F5F9)` | Secondary button hovered | Medio |
| Secondary default | `Color(0xFFF8FAFC)` | Secondary button default | Alto |
| Danger pressed | `Color(0xFFFEE2E2)` | Danger button pressed | Alto |
| Danger hovered | `Color(0xFFFFF1F2)` | Danger button hovered | Alto |
| Danger default | `Color(0xFFFEF2F2)` | Danger button default | Alto |
| Primary border | `Color(0xFF2563EB)` | Primary border hover/pressed | Alto |
| Secondary border | `Color(0xFFCBD5E1)` | Secondary border hovered | Medio |
| Danger border | `Color(0xFFFCA5A5)` | Danger border hovered | Alto |
| Text Primary | `Color(0xFFFFFFFF)` | Primary text | Bajo (white) |
| Text Secondary | `Color(0xFF0F172A)` | Secondary text | Medio |
| Text Danger | `Color(0xFFB91C1C)` | Danger text | Alto |

#### DemoBadge / DemoStatusText

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Neutral background | `Color(0xFFF8FAFC)` | Neutral badge | Medio |
| Success background | `Color(0xFFECFDF5)` | Success badge | Alto |
| Warning background | `Color(0xFFFFF7ED)` | Warning badge | Alto |
| Danger background | `Color(0xFFFEF2F2)` | Danger badge | Alto |
| Info background | `Color(0xFFEFF6FF)` | Info badge | Alto |
| Neutral border | `Color(0xFFE2E8F0)` | Neutral border | Medio |
| Success border | `Color(0xFFA7F3D0)` | Success border | Alto |
| Warning border | `Color(0xFFFED7AA)` | Warning border | Alto |
| Danger border | `Color(0xFFFECACA)` | Danger border | Alto |
| Info border | `Color(0xFFBFDBFE)` | Info border | Alto |
| Neutral text | `Color(0xFF334155)` | Neutral text | Medio |
| Success text | `Color(0xFF047857)` | Success text | Alto |
| Warning text | `Color(0xFFB45309)` | Warning text | Alto |
| Danger text | `Color(0xFFB91C1C)` | Danger text | Alto |
| Info text | `Color(0xFF1D4ED8)` | Info text | Alto |

#### DemostatusText (inline text con tone)

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Success text | `Color(0xFF047857)` | Active/Paid badges | Alto |
| Warning text | `Color(0xFFB45309)` | Pending/Leave/Low/Draft | Alto |
| Danger text | `Color(0xFFB91C1C)` | Overdue/Out/Disabled/Error | Alto |
| Info text | `Color(0xFF1D4ED8)` | New/Info | Alto |
| Neutral text | `Color(0xFF334155)` | Default | Medio |

#### DemoAvatar

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Border | `Color(0xFFBFDBFE)` | Avatar border | Medio |
| Text initials | `Color(0xFF1D4ED8)` | Avatar text | Alto |

#### DemoThumbnail

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Border | `Color(0xFFE2E8F0)` | Thumbnail border | Medio |
| Text | `Color(0xFF0F172A)` | Thumbnail text | Alto |

#### AccountMenu

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Hovered background | `Color(0xFFF1F5F9)` | Dropdown hover | Medio |
| Default background | `Color(0xFFFFFFFF)` | Dropdown default | Bajo |
| Border | `Color(0xFFE2E8F0)` | Dropdown border | Medio |
| Hovered item background | `Color(0xFFF8FAFC)` | Menu item hover | Medio |
| Danger text | `Color(0xFFB91C1C)` | Sign out text | Alto |
| Default text | `Color(0xFF0F172A)` | Menu item text | Medio |

### adaptive-feedback

Archivo: `adaptive-feedback/src/commonMain/kotlin/io/github/adaptivekt/feedback/EmptyState.kt` (y otros)

| Variable | Color | Uso | Riesgo |
|----------|-------|--------|
| Background | `Color.White` | Background estado | Bajo |
| Text default | `Color(0xFF0F172A)` | Text principal | Medio |
| Text muted | `Color(0xFF64748B)` | Description | Medio |
| Error/Error | `Color(0xFFB91C1C)` | Error text | Alto |
| Warning/Info | N/A | N/A | - |

### adaptive-layout

Archivo: `adaptive-layout/src/commonMain/kotlin/io/github/adaptivekt/layout/AdaptiveGrid.kt`

No tiene colores hardcodeados. Solo usa tokens.

### adaptive-navigation

Archivo: `adaptive-navigation/src/commonMain/kotlin/io/github/adaptivekt/navigation/NavigationSurfaces.kt`

No tiene colores hardcodeados directos. Usa componentes que los tienen.

### adaptive-forms

Archivo: `adaptive-forms/src/commonMain/kotlin/io/github/adaptivekt/forms/AdaptiveFormLayout.kt`

No tiene colores hardcodeados. Solo lógica de layout.

---

## Resumen de Riesgos

### Alto Riesgo (requiere atención primero)

1. **`DataPrimary`** (`0xFF2563EB`) - Usado en:
   - AdaptiveDataView buttons
   - DataPrimaryDark
   - DataText en overflow menu
   
2. **`DataMutedText`** (`0xFF64748B`) - Usado en:
   - Descriptions
   - Text secundario
   
3. **`DataDanger`** (`0xFFB91C1C`) - Usado en:
   - Danger buttons
   - Danger text
   
4. **`DemoBadge` tones** - Usados en:
   - Status badges
   - Success/Warning/Danger/Info/Neutral
   
5. **Avatar text** (`0xFF1D4ED8`) - Usado en:
   - DemoAvatar initials
   
6. **Primary button colors** - Usados en:
   - AdminDemoUi.kt DemoButton

### Medio Riesgo

1. **`DataBorder`** (`0xFFE2E8F0`) - Border común en data views
2. **Header backgrounds** - Light backgrounds en tabs
3. **Secondary button default** (`0xFFF8FAFC`)

### Bajo Riesgo

1. **White backgrounds** - Estándar admin UI
2. **Text primary** (`0xFF0F172A`) - Negro standard
3. **Text muted** - Gris estándar
4. **Neutral badge** - Gris oscuro standard

---

## Tokenización Recomendada (Futuro)

### Colores primarios

```kotlin
// En adaptive-core
object AdaptiveColors {
    val Primary = Color(0xFF2563EB)
    val PrimaryDark = Color(0xFF1E40AF)
    val PrimaryLight = Color(0xFF315FDC)
}
```

### Colores de estado

```kotlin
object AdaptiveColors {
    val Success = Color(0xFF047857)
    val Warning = Color(0xFFB45309)
    val Danger = Color(0xFFB91C1C)
    val Info = Color(0xFF1D4ED8)
}
```

### Textos

```kotlin
object AdaptiveColors {
    val Text = Color(0xFF0F172A)
    val TextMuted = Color(0xFF64748B)
}
```

### Borders y backgrounds

```kotlin
object AdaptiveColors {
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF8FAFC)
    val Border = Color(0xFFE2E8F0)
    val BorderHover = Color(0xFFCBD5E1)
}
```

### Badges

```kotlin
object AdaptiveColors {
    val BadgeSuccess = Color(0xFFECFDF5)
    val BadgeSuccessBorder = Color(0xFFA7F3D0)
    val BadgeWarning = Color(0xFFFFF7ED)
    val BadgeWarningBorder = Color(0xFFFED7AA)
    val BadgeDanger = Color(0xFFFEF2F2)
    val BadgeDangerBorder = Color(0xFFFECACA)
    val BadgeInfo = Color(0xFFEFF6FF)
    val BadgeInfoBorder = Color(0xFFBFDBFE)
    val BadgeNeutral = Color(0xFFF8FAFC)
    val BadgeNeutralBorder = Color(0xFFE2E8F0)
}
```

---

## Archivo más Problemático

**`admin-demo/src/commonMain/kotlin/io/github/adaptivekt/admin/demo/ui/AdminDemoUi.kt`**

Contiene la mayoría de los colores hardcodeados porque es el "kit de diseño" del demo.

Recomendación: Migrar colores a `AdaptiveColors` en `adaptive-core` cuando se implemente dark mode.

---

## Conclusión

- **20+ colores hardcodeados** en 2 archivos principales
- **4+ archivos** con colores hardcodeados
- **No hay riesgo inmediato** porque no hay dark mode
- **Refactorizar gradualmente** cuando se implemente dark mode
- **Tokenizar primero** los colores más usados
