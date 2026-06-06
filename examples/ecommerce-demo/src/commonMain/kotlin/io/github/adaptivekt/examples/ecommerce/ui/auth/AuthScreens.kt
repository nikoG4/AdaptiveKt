package io.github.adaptivekt.examples.ecommerce.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import io.github.adaptivekt.components.*
import io.github.adaptivekt.examples.ecommerce.state.StoreState
import io.github.adaptivekt.examples.ecommerce.navigation.Screen
import io.github.adaptivekt.forms.AdaptiveFormLayout
import io.github.adaptivekt.forms.AdaptiveValidationMessage
import io.github.adaptivekt.layout.AdaptiveContainer

@Composable
fun LoginScreen(state: StoreState, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC)), 
            contentAlignment = Alignment.Center
        ) {
            val compact = maxWidth < 600.dp
            
            AdaptiveCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
                    .padding(if (compact) 16.dp else 24.dp),
                contentPadding = PaddingValues(if (compact) 24.dp else 40.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(if (compact) 40.dp else 48.dp)
                            .background(Color(0xFF3B82F6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = if (compact) 20.sp else 24.sp)
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Welcome back", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(Modifier.height(8.dp))
                    Text("Enter your details to access your account", color = Color(0xFF64748B), fontSize = if (compact) 14.sp else 16.sp)
                    Spacer(Modifier.height(if (compact) 24.dp else 40.dp))

                    AdaptiveFormLayout(modifier = Modifier.fillMaxWidth()) {
                        section {
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
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                )
                            }
                            secondary {
                                AdaptiveButton(
                                    text = "Continue as Guest",
                                    variant = AdaptiveButtonVariant.Secondary,
                                    onClick = { state.navigateTo(Screen.Home) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Don't have an account?", color = Color(0xFF64748B), fontSize = 14.sp)
                        AdaptiveButton(
                            text = "Create Account", 
                            variant = AdaptiveButtonVariant.Ghost, 
                            onClick = { state.navigateTo(Screen.AuthRegister) }
                        )
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
    var showErrors by remember { mutableStateOf(false) }

    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC)), 
            contentAlignment = Alignment.Center
        ) {
            val compact = maxWidth < 600.dp
            
            AdaptiveCard(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
                    .padding(if (compact) 16.dp else 24.dp),
                contentPadding = PaddingValues(if (compact) 24.dp else 40.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Create Account", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(Modifier.height(8.dp))
                    Text("Join Adaptive Store today", color = Color(0xFF64748B), fontSize = if (compact) 14.sp else 16.sp)
                    Spacer(Modifier.height(if (compact) 24.dp else 40.dp))

                    AdaptiveFormLayout(modifier = Modifier.fillMaxWidth()) {
                        section {
                            field(label = "Full Name", required = true) {
                                AdaptiveTextField(value = name, onValueChange = { name = it })
                            }
                            field(label = "Email Address", required = true) {
                                AdaptiveTextField(value = email, onValueChange = { email = it })
                            }
                            field(label = "Password", required = true) {
                                AdaptiveTextField(value = password, onValueChange = { password = it }, placeholder = "At least 8 characters")
                            }
                        }
                        actions {
                            primary {
                                AdaptiveButton(text = "Create Account", onClick = { state.registerDemo() }, modifier = Modifier.fillMaxWidth().height(48.dp))
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Already have an account?", color = Color(0xFF64748B), fontSize = 14.sp)
                        AdaptiveButton(
                            text = "Sign In", 
                            variant = AdaptiveButtonVariant.Ghost, 
                            onClick = { state.navigateTo(Screen.AuthLogin) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(state: StoreState, modifier: Modifier = Modifier) {
    AdaptiveContainer(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC)), contentAlignment = Alignment.Center) {
            val compact = maxWidth < 600.dp
            AdaptiveCard(
                modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth().padding(if (compact) 16.dp else 24.dp), 
                contentPadding = PaddingValues(if (compact) 24.dp else 40.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Reset Password", fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
                    Spacer(Modifier.height(24.dp))
                    AdaptiveFormLayout {
                        section {
                            field(label = "Email Address") {
                                AdaptiveTextField(value = "", onValueChange = {}, placeholder = "name@example.com")
                            }
                        }
                        actions {
                            primary {
                                AdaptiveButton(text = "Send Reset Link", onClick = { state.navigateTo(Screen.AuthLogin) }, modifier = Modifier.fillMaxWidth().height(48.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
