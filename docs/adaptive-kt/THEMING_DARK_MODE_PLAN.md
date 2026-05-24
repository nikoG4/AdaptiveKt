# Plan de theming y dark mode

## No implementar dark mode aún

## Plan
- AdaptiveTheme y AdaptiveColorScheme como entrypoint
- Tokens light/dark en adaptive-core
- Eliminar colores hardcodeados gradualmente
- Migración por módulos: components → navigation/data/forms/feedback → demo
- Toggle futuro en admin-demo
- Riesgos: colores hardcodeados, visuales inconsistentes
- No mezclar dark mode con PRs de Select/DataView
- Documentar tokens y estrategia en README
