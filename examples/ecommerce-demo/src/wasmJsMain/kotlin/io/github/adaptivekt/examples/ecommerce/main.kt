package io.github.adaptivekt.examples.ecommerce

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    println("APP_STARTED")
    ComposeViewport(document.body!!) {
        EcommerceApp()
    }
}
