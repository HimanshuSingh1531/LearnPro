package com.himanshu.learnpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.admin.AddCourseScreen
import com.himanshu.learnpro.admin.AdminDashboardScreen
import com.himanshu.learnpro.auth.AdminLoginScreen
import com.himanshu.learnpro.auth.LoginScreen
import com.himanshu.learnpro.auth.RoleSelectScreen
import com.himanshu.learnpro.ui.home.HomeContainerScreen
import com.himanshu.learnpro.ui.profile.ProfileSetupScreen
import com.himanshu.learnpro.ui.theme.LearnProTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearnProTheme {

                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()

                var currentScreen by remember { mutableStateOf("role") }
                var userUid by remember { mutableStateOf<String?>(null) }

                when (currentScreen) {

                    // ---------------- ROLE SELECT ----------------
                    "role" -> {
                        RoleSelectScreen(
                            onUserClick = {
                                currentScreen = "user_login"
                            },
                            onAdminClick = {
                                currentScreen = "admin_login"
                            }
                        )
                    }

                    // ---------------- USER LOGIN ----------------
                    "user_login" -> {
                        LoginScreen(
                            onAuthSuccess = {
                                val uid = auth.currentUser?.uid ?: return@LoginScreen
                                userUid = uid

                                firestore.collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        val completed =
                                            doc.getBoolean("profileCompleted") ?: false

                                        currentScreen = if (completed) {
                                            "user_home"
                                        } else {
                                            "profile_setup"
                                        }
                                    }
                            }
                        )
                    }

                    // ---------------- PROFILE SETUP ----------------
                    "profile_setup" -> {
                        ProfileSetupScreen(
                            uid = userUid!!,
                            onProfileSaved = {
                                currentScreen = "user_home"
                            }
                        )
                    }

                    // ---------------- USER HOME ----------------
                    "user_home" -> {
                        HomeContainerScreen(
                            userUid = userUid!!
                        )
                    }

                    // ---------------- ADMIN LOGIN ----------------
                    "admin_login" -> {
                        AdminLoginScreen(
                            onAdminLoginSuccess = {
                                currentScreen = "admin_dashboard"
                            }
                        )
                    }

                    // ---------------- ADMIN DASHBOARD ----------------
                    "admin_dashboard" -> {
                        AdminDashboardScreen(
                            onHomeClick = {
                                // optional
                            },
                            onAddCourseClick = {
                                currentScreen = "add_course"
                            },
                            onManageCourseClick = {
                                // future
                            },
                            onExploreClick = {
                                // future
                            },
                            onLogoutClick = {
                                auth.signOut()
                                currentScreen = "role"
                            }
                        )
                    }

                    // ---------------- ADD COURSE ----------------
                    "add_course" -> {
                        AddCourseScreen(
                            onBack = {
                                currentScreen = "admin_dashboard"
                            }
                        )
                    }
                }
            }
        }
    }
}
