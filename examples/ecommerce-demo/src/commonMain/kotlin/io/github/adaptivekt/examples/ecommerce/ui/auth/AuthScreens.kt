package io.github.adaptivekt.examples.ecommerce.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.adaptivekt.components.AdaptiveButton
import io.github.adaptivekt.components.AdaptiveButtonVariant
import io.github.adaptivekt.components.AdaptiveTextField
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.AdaptiveValidationMessage
import io.github.adaptivekt.layout.AdaptiveContainer
import io.github.adaptivekt.core.AdaptiveTheme

@Composable
fun LoginScreen(state: StoreState, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AdaptiveFormLayout(modifier = Modifier.widthIn(max = 400.dp)) {
                section(title = "Welcome Back", description = "Enter your details to access your account.") {
                    field(
                        label = "Email Address",
                        required = true,
                        validationMessage = if (showErrors && email.isBlank()) AdaptiveValidationMessage("Email is required") else null
                    ) {
                        AdaptiveTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "name@example.com",
                            validationMessage = if (showErrors && email.isBlank()) "Email is required" else null
                        )
                    }
                    field(
                        label = "Password",
                        required = true,
                        validationMessage = if (showErrors && password.isBlank()) AdaptiveValidationMessage("Password is required") else null
                    ) {
                        AdaptiveTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = "••••••••",
                            validationMessage = if (showErrors && password.isBlank()) "Password is required" else null
                        )
                    }
                }
                actions {
                    primary {
                        AdaptiveButton(
                            text = "Sign In",
                            onClick = {
                                if (email.isNotBlank() && password.isNotBlank()) {
                                    state.loginDemo()
                                } else {
                                    showErrors = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    secondary {
                        AdaptiveButton(
                            text = "Continue as Guest",
                            variant = AdaptiveButtonVariant.Secondary,
                            onClick = { state.navigateTo(Screen.Home) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                section {
                    field(label = "") {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            AdaptiveButton(text = "Forgot Password?", variant = AdaptiveButtonVariant.Ghost, onClick = { state.navigateTo(Screen.AuthForgotPassword) })
                            AdaptiveButton(text = "Create Account", variant = AdaptiveButtonVariant.Ghost, onClick = { state.navigateTo(Screen.AuthRegister) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(state: StoreState, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AdaptiveFormLayout(modifier = Modifier.widthIn(max = 400.dp)) {
                section(title = "Create an Account", description = "Join Adaptive Store today.") {
                    field(label = "Full Name", required = true) {
                        AdaptiveTextField(value = name, onValueChange = { name = it }, validationMessage = if (showErrors && name.isBlank()) "Name is required" else null)
                    }
                    field(label = "Email Address", required = true) {
                        AdaptiveTextField(value = email, onValueChange = { email = it }, validationMessage = if (showErrors && email.isBlank()) "Email is required" else null)
                    }
                    field(label = "Password", required = true) {
                        AdaptiveTextField(value = password, onValueChange = { password = it }, validationMessage = if (showErrors && password.length < 6) "Password must be at least 6 characters" else null)
                    }
                    field(label = "Confirm Password", required = true) {
                        AdaptiveTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, validationMessage = if (showErrors && password != confirmPassword) "Passwords do not match" else null)
                    }
                }
                actions {
                    primary {
                        AdaptiveButton(text = "Create Account", onClick = {
                            if (name.isNotBlank() && email.isNotBlank() && password.length >= 6 && password == confirmPassword) {
                                state.registerDemo()
                            } else {
                                showErrors = true
                            }
                        }, modifier = Modifier.fillMaxWidth())
                    }
                }
                section {
                    field(label = "") {
                        AdaptiveButton(text = "Already have an account? Sign In", variant = AdaptiveButtonVariant.Ghost, onClick = { state.navigateTo(Screen.AuthLogin) }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(state: StoreState, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AdaptiveFormLayout(modifier = Modifier.widthIn(max = 400.dp)) {
                section(title = "Reset Password", description = if (sent) "A reset link has been sent to your email." else "Enter your email to receive a reset link.") {
                    if (!sent) {
                        field(label = "Email Address", required = true) {
                            AdaptiveTextField(value = email, onValueChange = { email = it }, placeholder = "name@example.com")
                        }
                    }
                }
                actions {
                    if (!sent) {
                        primary {
                            AdaptiveButton(text = "Send Reset Link", onClick = { if (email.isNotBlank()) sent = true }, modifier = Modifier.fillMaxWidth())
                        }
                    } else {
                        primary {
                            AdaptiveButton(text = "Return to Login", onClick = { state.navigateTo(Screen.AuthLogin) }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                    secondary {
                        if (!sent) {
                            AdaptiveButton(text = "Cancel", variant = AdaptiveButtonVariant.Ghost, onClick = { state.navigateTo(Screen.AuthLogin) }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}
