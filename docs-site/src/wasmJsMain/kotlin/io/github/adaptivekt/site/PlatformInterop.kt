package io.github.adaptivekt.site

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@JsFun("(id) => { const el = document.getElementById(id); if (el) el.scrollIntoView({behavior: 'smooth', block: 'start'}); }")
internal external fun scrollIntoViewJs(id: String)

@JsFun("(text) => navigator.clipboard.writeText(text)")
internal external fun writeTextToClipboardJs(text: String): kotlin.js.Promise<JsAny?>

@JsFun("(event, data) => console.log('[Telemetry] ' + event + ' - ' + data)")
internal external fun logTelemetryJs(event: String, data: String)

internal actual object PlatformInterop {
    actual fun scrollToElement(id: String) {
        scrollIntoViewJs(id)
    }

    actual fun getWindowOrigin(): String = kotlinx.browser.window.location.origin

    actual fun logTelemetry(event: String, data: String) {
        logTelemetryJs(event, data)
    }



    actual fun copyToClipboard(text: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            writeTextToClipboardJs(text)
                .then {
                    onSuccess()
                    null
                }
                .catch {
                    onError("Failed to copy")
                    null
                }
        } catch (e: Exception) {
            onError(e.message ?: "Unknown error")
        }
    }
}





