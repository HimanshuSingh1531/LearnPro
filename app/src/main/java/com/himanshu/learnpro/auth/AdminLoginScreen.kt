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
fun AdminLoginScreen(
    onAdminLoginSuccess: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
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
                text = "Admin Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SoftBlack
            )

            Text(
                text = "Only authorized admins allowed",
                fontSize = 14.sp,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 📧 Admin Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                    errorMessage = ""
                },
                placeholder = { Text("Admin email") },
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
                    errorMessage = ""
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

            // 🔘 Login as Admin Button (🔥 SAME LOGIC)
            Button(
                onClick = {

                    emailError = ""
                    passwordError = ""
                    errorMessage = ""
                    successMessage = ""

                    when {
                        email.isBlank() -> {
                            emailError = "Admin email is required"
                        }

                        password.isBlank() -> {
                            passwordError = "Password is required"
                        }

                        else -> {
                            loading = true

                            AuthManager.loginAdmin(
                                email = email,
                                password = password,
                                onSuccess = {
                                    loading = false
                                    successMessage = "Admin Login Successful"
                                    onAdminLoginSuccess()   // 🔥 ONLY NEW LINE
                                },
                                onError = { error ->
                                    loading = false
                                    errorMessage = error
                                }
                            )
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
                        text = "Login as Admin",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ❌ Error message
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
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
        }
    }
}
