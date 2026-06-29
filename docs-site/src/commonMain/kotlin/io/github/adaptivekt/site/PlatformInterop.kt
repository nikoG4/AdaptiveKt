package io.github.adaptivekt.site

internal expect object PlatformInterop {
    fun scrollToElement(id: String)
    fun copyToClipboard(text: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun getWindowOrigin(): String
    fun getWindowBasePath(): String
    fun encodeUrlComponent(value: String): String
    fun decodeUrlComponent(value: String): String
    fun logTelemetry(event: String, data: String = "none")
    fun updateValidationState(route: String, component: String, section: String)
    fun currentSiteBasePath(): String
}


