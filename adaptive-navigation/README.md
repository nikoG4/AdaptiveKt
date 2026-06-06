# AdaptiveNavigator

`adaptive-navigation` provides a clean, robust, multiplatform routing and navigation system designed for AdaptiveKt applications. It seamlessly bridges native in-memory navigation with Web History APIs (Hash and standard History mode) for WasmJs without coupling your app to browser specifics.

## Core Concepts

### `AdaptiveNavigator<R>`
The central interface for navigating your app. It holds the `currentRoute` and manages the `backStack`.
```kotlin
interface AdaptiveNavigator<R> {
    val currentRoute: R
    val canGoBack: Boolean
    fun navigate(route: R)
    fun replace(route: R)
    fun goBack()
}
```

### `AdaptiveRouteCodec<R>`
A bidirectional mapper that translates your strongly typed route objects (e.g., `Screen.Home`) to and from string paths (e.g., `"/"`). This keeps your UI logic strictly typed.
```kotlin
interface AdaptiveRouteCodec<R> {
    fun encode(route: R): String
    fun decode(path: String): R?
}
```

### `AdaptiveWebNavigationMode`
Controls how the navigator interacts with the browser's URL on the Web (`wasmJsMain`).
- `Hash` (Default): Uses `window.location.hash` (`/#/path`). Safe for GitHub pages and static hosts.
- `History`: Uses standard `window.history.pushState` (`/path`). Requires server configuration for fallback routing.

## Usage

Define your app routes as a sealed class or enum, then create a codec for it.

```kotlin
// 1. Define Routes
sealed class AppRoute {
    data object Home : AppRoute()
    data object Products : AppRoute()
    data class ProductDetail(val id: String) : AppRoute()
}

// 2. Create Codec
object AppRouteCodec : AdaptiveRouteCodec<AppRoute> {
    override fun encode(route: AppRoute): String = when (route) {
        is AppRoute.Home -> "/"
        is AppRoute.Products -> "/shop"
        is AppRoute.ProductDetail -> "/product/${route.id}"
    }

    override fun decode(path: String): AppRoute? {
        val normalized = if (path.startsWith("/")) path else "/$path"
        return when {
            normalized == "/" -> AppRoute.Home
            normalized == "/shop" -> AppRoute.Products
            normalized.startsWith("/product/") -> {
                val id = normalized.substringAfter("/product/")
                if (id.isNotEmpty()) AppRoute.ProductDetail(id) else null
            }
            else -> null // Will fallback to initialRoute
        }
    }
}
```

Initialize it at the root of your App using `rememberAdaptiveNavigator`:

```kotlin
@Composable
fun MyApp() {
    val navigator = rememberAdaptiveNavigator(
        initialRoute = AppRoute.Home,
        codec = AppRouteCodec,
        options = AdaptiveNavigationOptions(
            webMode = AdaptiveWebNavigationMode.Hash
        )
    )

    when (val screen = navigator.currentRoute) {
        is AppRoute.Home -> HomeScreen(onNavigate = { navigator.navigate(AppRoute.Products) })
        is AppRoute.Products -> ProductsScreen(onNavigate = { navigator.navigate(AppRoute.ProductDetail(it)) })
        is AppRoute.ProductDetail -> DetailScreen(id = screen.id, onBack = { navigator.goBack() })
    }
}
```

## Features & Guarantees
- **No Infinite Loops**: Safely manages `window.location.hash` alongside `onhashchange` callbacks without triggering duplicate navigations.
- **Robust Fallbacks**: Navigating to an unknown route or empty hash defaults gracefully to the `initialRoute`, restoring the UI and rewriting the invalid URL via `replace`.
- **True Browser Sync**: The browser's Back and Forward buttons stay flawlessly synchronized with the internal `currentRoute`.
- **100% Shared UI**: No conditional `expect/actual` or `window` references in your application code.
- **Cross-Platform Ready**: Uses an in-memory stack automatically when running on Android, iOS, JVM, or Desktop environments.
