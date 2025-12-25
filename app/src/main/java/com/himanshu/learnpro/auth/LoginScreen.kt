package com.himanshu.learnpro.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshu.learnpro.ui.theme.*

@Composable
fun LoginScreen( onAuthSuccess: () -> Unit) {

    // 🔁 Toggle state
    var isLogin by remember { mutableStateOf(true) }

    // Fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Errors
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    var successMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "LearnPro",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = SoftBlack
            )

            Text(
                text = if (isLogin)
                    "Welcome back, login to continue"
                else
                    "Create your account to get started",
                fontSize = 14.sp,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 👤 Name field (Signup only)
            if (!isLogin) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = ""
                    },
                    placeholder = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nameError.isNotEmpty(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoftBlack,
                        unfocusedBorderColor = LightGray
                    )
                )

                if (nameError.isNotEmpty()) {
                    Text(
                        text = nameError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            // 📧 Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                placeholder = { Text("Email address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailError.isNotEmpty(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoftBlack,
                    unfocusedBorderColor = LightGray
                )
            )

            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            // 🔑 Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                placeholder = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoftBlack,
                    unfocusedBorderColor = LightGray
                )
            )

            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            // 🔘 Login / Signup Button (🔥 FIREBASE CONNECTED)
            Button(
                onClick = {

                    successMessage = ""
                    emailError = ""
                    passwordError = ""
                    nameError = ""

                    when {
                        !isLogin && name.isBlank() -> {
                            nameError = "Name is required"
                        }

                        email.isBlank() -> {
                            emailError = "Email is required"
                        }

                        password.isBlank() -> {
                            passwordError = "Password is required"
                        }

                        else -> {
                            loading = true

                            if (isLogin) {
                                AuthManager.loginUser(
                                    email = email,
                                    password = password,
                                    onSuccess = {
                                        loading = false
                                        onAuthSuccess()

                            },
                                    onError = {
                                        loading = false
                                        emailError = it
                                    }
                                )
                            } else {
                                AuthManager.signupUser(
                                    email = email,
                                    password = password,
                                    onSuccess = {
                                        loading = false
                                        onAuthSuccess()
                                    },
                                    onError = {
                                        loading = false
                                        emailError = it
                                    }
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !loading,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftBlack,
                    contentColor = SurfaceWhite
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = SurfaceWhite,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = if (isLogin) "Login" else "Sign Up",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ✅ Success message
            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = Color(0xFF2E7D32),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 🔁 Toggle text
            TextButton(
                onClick = {
                    isLogin = !isLogin
                    nameError = ""
                    emailError = ""
                    passwordError = ""
                    successMessage = ""
                }
            ) {
                Text(
                    text = if (isLogin)
                        "New here? Create an account"
                    else
                        "Already have an account? Login",
                    color = SoftBlack
                )
            }

            Divider(color = LightGray)

            OutlinedButton(
                onClick = {
                    // Google auth later
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Continue with Google",
                    fontSize = 15.sp,
                    color = SoftBlack
                )
            }
        }
    }
}
