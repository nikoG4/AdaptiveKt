package io.github.adaptivekt.examples.communication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.adaptivekt.examples.communication.ui.CommunicationApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CommunicationApp()
        }
    }
}
