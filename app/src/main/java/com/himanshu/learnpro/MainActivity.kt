//package com.himanshu.learnpro
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.runtime.*
//import com.himanshu.learnpro.admin.AdminDashboardScreen
//import com.himanshu.learnpro.admin.AddCourseScreen
//import com.himanshu.learnpro.auth.AdminLoginScreen
//import com.himanshu.learnpro.auth.LoginScreen
//import com.himanshu.learnpro.auth.RoleSelectScreen
//import com.himanshu.learnpro.auth.AuthManager
//import com.himanshu.learnpro.ui.theme.LearnProTheme
//
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            LearnProTheme {
//
//                var currentScreen by remember { mutableStateOf("role") }
//
//                when (currentScreen) {
//
//                    "role" -> {
//                        RoleSelectScreen(
//                            onUserClick = { currentScreen = "user_login" },
//                            onAdminClick = { currentScreen = "admin_login" }
//                        )
//                    }
//
//                    "user_login" -> {
//                        LoginScreen()
//                    }
//
//                    "admin_login" -> {
//                        AdminLoginScreen(
//                            onAdminLoginSuccess = {
//                                currentScreen = "admin_dashboard"
//                            }
//                        )
//                    }
//
//                    "admin_dashboard" -> {
//                        AdminDashboardScreen(
//                            onHomeClick = { /* later */ },
//                            onAddCourseClick = {
//                                currentScreen = "add_course"
//                            },
//                            onManageCourseClick = { /* next step */ },
//                            onExploreClick = { /* preview later */ },
//                            onLogoutClick = {
//                                AuthManager.logout()
//                                currentScreen = "role"
//                            }
//                        )
//                    }
//
//                    "add_course" -> {
//                        AddCourseScreen(
//                            onBack = {
//                                currentScreen = "admin_dashboard"
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }

//}
