package com.himanshu.learnpro.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.himanshu.learnpro.data.model.BottomNavItem
import com.himanshu.learnpro.ui.profile.ProfileScreen
import com.himanshu.learnpro.viewmodel.ExploreViewModel
import com.himanshu.learnpro.viewmodel.MyCoursesViewModel
import com.himanshu.learnpro.viewmodel.ProfileViewModel

@Composable
fun HomeContainerScreen(userUid: String) {

    var selectedTab by remember { mutableStateOf(BottomNavItem.HOME) }

    // ✅ REMEMBER VIEWMODELS (CRITICAL FIX)
    val exploreViewModel = remember { ExploreViewModel() }
    val myCoursesViewModel = remember { MyCoursesViewModel() }
    val profileViewModel = remember { ProfileViewModel() }

    // 🔍 Debug (temporary – you can remove later)
    LaunchedEffect(Unit) {
        println("HOME CONTAINER UID = $userUid")
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {

                BottomNavItem.HOME -> {
                    HomeScreen() // placeholder
                }

                BottomNavItem.EXPLORE -> {
                    ExploreScreen(
                        viewModel = exploreViewModel,
                        onCourseClick = { /* later */ }
                    )
                }

                BottomNavItem.MY_COURSES -> {
                    MyCoursesScreen(
                        uid = userUid,
                        viewModel = myCoursesViewModel,
                        onCourseClick = { /* later */ }
                    )
                }

                BottomNavItem.PROFILE -> {
                    ProfileScreen(
                        uid = userUid,
                        viewModel = profileViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == BottomNavItem.HOME,
            onClick = { onTabSelected(BottomNavItem.HOME) },
            label = { Text("Home") },
            icon = {}
        )

        NavigationBarItem(
            selected = selectedTab == BottomNavItem.EXPLORE,
            onClick = { onTabSelected(BottomNavItem.EXPLORE) },
            label = { Text("Explore") },
            icon = {}
        )

        NavigationBarItem(
            selected = selectedTab == BottomNavItem.MY_COURSES,
            onClick = { onTabSelected(BottomNavItem.MY_COURSES) },
            label = { Text("My Courses") },
            icon = {}
        )

        NavigationBarItem(
            selected = selectedTab == BottomNavItem.PROFILE,
            onClick = { onTabSelected(BottomNavItem.PROFILE) },
            label = { Text("Profile") },
            icon = {}
        )
    }
}
//@Preview(showBackground = true)
//@Composable
//fun HomeContainerPreview() {
//    MaterialTheme {
//        HomeContainerScreen()
//    }
//}
