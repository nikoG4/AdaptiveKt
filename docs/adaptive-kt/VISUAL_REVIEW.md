# PR9 Visual Review

Generated: 2026-05-22

## Capture Set

- Output directory: `build/visual-captures`
- Manifest: `build/visual-captures/manifest.json`
- Report: `build/visual-captures/visual-capture-report.md`
- ZIP: `build/adaptivekt-admin-demo-visual-captures.zip`
- Total captures: 20 standard matrix captures plus an optional account-menu capture when generated manually.

## Reviewed Breakpoints

- Compact: 420x900
- Medium: 720x900
- Expanded: 1000x900
- Large: 1440x900

## Acceptance Review

- Button hover shape: pass. Data/demo buttons now paint hover and pressed state inside the clipped rounded shape; no external rectangular block is used.
- Compact top spacing: pass. Topbar height is tokenized and content no longer starts with a large dead area.
- Medium rail labels: pass. Labels are shortened and constrained to one line, avoiding broken wrapping like `Dashboa rd`.
- Large dashboard: pass. KPI cards are larger and supported by recent invoice and activity panels, so the page no longer feels empty.
- Monthly revenue: pass. KPI card width and one-line value prevent the previous awkward break.
- Large tables: pass. Data tables render as a unified table surface with subtle header, separators, row height, badges, avatars/thumbnails, and quieter actions.
- Emails and long text: pass for demo data. Table layout and demo text clipping prevent odd email line breaks.
- Badges: pass. Status badges are pill shaped with soft tones in the demo and rounded default data styling.
- Compact data cards: pass. Generated cards show media when available, title, subtitle, status, limited metadata, primary action, and overflow, not every field.
- Settings large: pass. Form content is constrained by `AdaptiveFormLayout.maxWidth`.
- Settings compact: pass. Actions are visible at the end and do not cover fields.
- Topbar account menu: pass. The demo includes a user avatar with a dropdown for profile, account settings, and sign out.
- Buttons: pass. Demo and data action buttons use pill radius, consistent height, clear variants, and shape-safe interaction states.
- Empty/loading/error defaults: pass by component implementation; invoice screen exposes state toggles for manual review.

## Notes

- The capture tooling uses AWT Robot and still requires an active graphical desktop.
- The visual language remains Foundation-only. No Material 3 or external design dependency was added.
- The rail uses shortened labels rather than icons from an icon library because AdaptiveKt currently has no icon dependency.
- Demo media uses generated avatars/thumbnails so the behavior can be reviewed without adding assets or network loading.
