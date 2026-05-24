# AdaptiveKt Visual Capture Tooling

Este tooling agrega una forma de ejecutar `admin-demo` en modo de captura visual desde JVM/Desktop sin cambiar la API pública de AdaptiveKt.

## Objetivo

- Abrir `admin-demo` en un modo de captura dedicado.
- Crear y controlar explícitamente una `ComposeWindow` propia para admin-demo.
- Seleccionar una pantalla inicial (`dashboard`, `employees`, `products`, `invoices`, `settings`).
- Esperar a que la ventana esté visible y al frente.
- Capturar sólo los bounds reales de esa ventana con AWT `Robot`.
- Guardar un PNG en la ruta indicada.
- Cerrar la app automáticamente.

## Cómo funciona

En modo `--capture`, el demo NO utiliza la ventana normal de `application { Window { ... } }`.
En su lugar, se crea directamente una `androidx.compose.ui.awt.ComposeWindow()` con título único:

- `AdaptiveKt Admin Demo Capture - dashboard`
- `AdaptiveKt Admin Demo Capture - employees`

La captura se realiza con:

- `window.isAlwaysOnTop = true`
- `window.toFront()` y `window.requestFocus()` antes de capturar
- `Robot().createScreenCapture(Rectangle(location.x, location.y, size.width, size.height))`

Esto garantiza que el screenshot use la ventana controlada directamente, no la ventana activa por casualidad.

## Uso

```powershell
.
\gradlew :admin-demo:run --args="--capture --screen dashboard --width 1440 --height 900 --output build/visual/dashboard-large.png --delayMs 1500"
```

```powershell
.
\gradlew :admin-demo:run --args="--capture --screen employees --width 420 --height 900 --output build/visual/employees-compact.png --delayMs 1500"
```

## Argumentos soportados

- `--capture`
- `--screen <dashboard|employees|products|invoices|settings>`
- `--width <int>`
- `--height <int>`
- `--output <path>`
- `--delayMs <int opcional, default 1200>`

## Tarea Gradle

Desde la carpeta raíz:

```powershell
.
\gradlew :admin-demo:captureVisuals
```

El task `captureVisuals` ya usa `--delayMs 1500` para dar tiempo a que la ventana aparezca.

También existe un script de captura por lotes que genera múltiples tamaños, manifest y ZIP:

```powershell
.
\tools\capture-admin-demo.ps1
```

Por defecto, el script genera capturas en:

- `build/visual-captures/compact`
- `build/visual-captures/medium`
- `build/visual-captures/expanded`
- `build/visual-captures/large`

Y empaqueta todo en:

- `build/adaptivekt-admin-demo-visual-captures.zip`

## Limitaciones

- Requiere una sesión gráfica real y una pantalla activa.
- No funciona en modo headless sin un display virtual.
- La ventana no debe estar minimizada.
- Si la ventana está cubierta por otra aplicación, la captura puede fallar o capturar mal.
- La captura usa AWT `Robot`, por lo que depende de la plataforma JVM/desktop.

## Diagnóstico

Si la captura falla, el runner imprime la lista de ventanas visibles y sus bounds:

- title
- visible
- showing
- bounds

Esto es diagnóstico, no el mecanismo principal de captura.
