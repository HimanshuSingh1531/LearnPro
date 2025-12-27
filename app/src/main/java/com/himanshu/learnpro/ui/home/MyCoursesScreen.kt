package com.himanshu.learnpro.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himanshu.learnpro.data.model.Course
import com.himanshu.learnpro.viewmodel.MyCoursesViewModel

@Composable
fun MyCoursesScreen(
    uid: String,
    viewModel: MyCoursesViewModel,
    onCourseClick: (Course) -> Unit   // 🔥 FIXED
) {
    LaunchedEffect(uid) {
        viewModel.loadMyCourses(uid)
    }

    when {
        viewModel.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        viewModel.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(viewModel.error ?: "Something went wrong")
            }
        }

        viewModel.courses.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("You have not purchased any courses yet")
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "My Courses",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(viewModel.courses) { course ->
                    MyCourseCard(
                        course = course,
                        onClick = { onCourseClick(course) }   // 🔥 FIXED
                    )
                }
            }
        }
    }
}

@Composable
private fun MyCourseCard(
    course: Course,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            Text(
                text = "Price: ₹${course.price}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
