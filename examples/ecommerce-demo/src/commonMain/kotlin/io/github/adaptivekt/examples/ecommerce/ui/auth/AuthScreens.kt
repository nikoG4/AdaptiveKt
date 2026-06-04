package io.github.adaptivekt.examples.ecommerce.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen

@Composable
fun LoginScreen(state: StoreState, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Login Screen")
            Button(onClick = { state.loginDemo() }) {
                Text("Login Demo")
            }
            Button(onClick = { state.navigateTo(Screen.Home) }) {
                Text("Continue as Guest")
            }
        }
    }
}

@Composable
fun RegisterScreen(state: StoreState, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Register Screen")
    }
}
