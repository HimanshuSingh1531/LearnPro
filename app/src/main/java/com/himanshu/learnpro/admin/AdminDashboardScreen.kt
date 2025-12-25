package com.himanshu.learnpro.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshu.learnpro.ui.theme.*

@Composable
fun AdminDashboardScreen(
    onHomeClick: () -> Unit,
    onAddCourseClick: () -> Unit,
    onManageCourseClick: () -> Unit,
    onExploreClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 🔝 Header
            Text(
                text = "Admin Dashboard",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = SoftBlack
            )

            Text(
                text = "Manage app content from here",
                fontSize = 14.sp,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 🔲 Dashboard Buttons
            DashboardCard(
                title = "Home",
                subtitle = "View app overview",
                onClick = onHomeClick
            )

            DashboardCard(
                title = "Add Course",
                subtitle = "Create new course for users",
                onClick = onAddCourseClick
            )

            DashboardCard(
                title = "Manage Courses",
                subtitle = "Edit or delete existing courses",
                onClick = onManageCourseClick
            )

            DashboardCard(
                title = "Explore",
                subtitle = "Preview user explore screen",
                onClick = onExploreClick
            )

            Spacer(modifier = Modifier.weight(1f))

            // 🚪 Logout
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Logout",
                    fontSize = 15.sp,
                    color = SoftBlack
                )
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = SoftBlack
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = SoftGray
            )
        }
    }
}
