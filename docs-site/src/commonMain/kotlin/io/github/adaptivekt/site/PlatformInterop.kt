package io.github.adaptivekt.site

internal expect object PlatformInterop {
    fun scrollToElement(id: String)
    fun copyToClipboard(text: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun getWindowOrigin(): String
    fun logTelemetry(event: String, data: String = "none")
}


