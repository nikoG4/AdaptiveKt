# Visual Verification

AdaptiveKt uses smoke screenshots for manual visual review.

## Docs Site Web

```powershell
.\tools\capture-docs-site-web.ps1
```

Outputs:

```text
build/docs-site-web-captures/
build/adaptivekt-docs-site-web-captures.zip
```

Captured routes:

- `/`
- `/components/`
- `/docs/`
- `/demo/`
- `/?theme=dark` on compact and large
- `/components/?theme=dark` on compact and large
- `/demo/app/` smoke target
- `/demo/app/?theme=dark` smoke target

Viewports:

- compact: `420x900`
- medium: `720x900`
- large: `1440x900`

## Admin Demo Web

```powershell
.\tools\capture-admin-demo-web.ps1
```

Outputs:

```text
build/web-visual-captures/
build/adaptivekt-admin-demo-web-visual-captures.zip
```

The Web capture matrix includes light screenshots for the main admin-demo screens and dark screenshots on compact and large for:

- `dashboard`
- `invoices-error`
- `components-selects`
- `components-multiselects`
- `components-carousels`
- `components-navigation-tree`
- `components-feedback`

## Admin Demo Desktop

```powershell
.\tools\capture-admin-demo.ps1
```

Desktop capture uses AWT `Robot` and requires an active graphical session.

The desktop capture entry point accepts `--theme dark` for individual dark screenshots. The batch PowerShell script accepts `-Theme both` to capture the current light/dark matrix, including focused component sections such as carousel and feedback loading.

## Link Check

```powershell
.\tools\check-site-links.ps1
```

The link checker validates generated `site-dist` HTML for local internal links and confirms `demo/app/index.html` exists. External links are skipped.

## Before Push

Run:

```powershell
.\gradlew.bat build --console=plain --stacktrace
.\tools\prepare-pages-site.ps1
.\tools\capture-docs-site-web.ps1 -SkipBuild
.\tools\capture-admin-demo-web.ps1 -SkipBuild
.\tools\check-site-links.ps1
```

Robot capture and Playwright screenshots are not required in CI yet.
