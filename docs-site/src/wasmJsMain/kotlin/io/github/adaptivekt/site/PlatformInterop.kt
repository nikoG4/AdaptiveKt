package io.github.adaptivekt.site

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@JsFun("(id) => { const el = document.getElementById(id); if (el) el.scrollIntoView({behavior: 'smooth', block: 'start'}); }")
internal external fun scrollIntoViewJs(id: String)

@JsFun("(text) => navigator.clipboard.writeText(text)")
internal external fun writeTextToClipboardJs(text: String): kotlin.js.Promise<JsAny?>

@JsFun("(event, data) => console.log('[Telemetry] ' + event + ' - ' + data)")
internal external fun logTelemetryJs(event: String, data: String)

@JsFun("(str) => encodeURIComponent(str)")
internal external fun encodeURIComponentJs(str: String): String

@JsFun("(str) => decodeURIComponent(str)")
internal external fun decodeURIComponentJs(str: String): String

@JsFun("(route, component, section) => { let el = document.getElementById('docs-validation-state'); if (!el) { el = document.createElement('div'); el.id = 'docs-validation-state'; el.style.display = 'none'; document.body.appendChild(el); } el.setAttribute('data-route', route); el.setAttribute('data-component', component); el.setAttribute('data-section', section); }")
internal external fun updateValidationStateJs(route: String, component: String, section: String)

internal actual object PlatformInterop {
    actual fun scrollToElement(id: String) {
        scrollIntoViewJs(id)
    }

    actual fun getWindowOrigin(): String = kotlinx.browser.window.location.origin

    actual fun getWindowBasePath(): String = kotlinx.browser.window.location.pathname

    actual fun encodeUrlComponent(value: String): String = encodeURIComponentJs(value)

    actual fun decodeUrlComponent(value: String): String = decodeURIComponentJs(value)

    actual fun logTelemetry(event: String, data: String) {
        logTelemetryJs(event, data)
    }

    actual fun updateValidationState(route: String, component: String, section: String) {
        updateValidationStateJs(route, component, section)
    }

    actual fun currentSiteBasePath(): String {
        var path = getWindowBasePath()
        if (path.endsWith("index.html")) {
            path = path.substringBeforeLast("index.html")
        }
        if (!path.endsWith("/")) {
            path += "/"
        }
        return path
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





