package com.himanshu.learnpro.admin
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.ui.theme.*

@Composable
fun ManageCoursesScreen(
    onCourseClick: (courseId: String, courseTitle: String) -> Unit,
    onBack: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var courses by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("courses")
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { snapshot ->
                courses = snapshot.documents.map {
                    it.id to (it.getString("title") ?: "Untitled")
                }
                loading = false
            }
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Text(
            text = "Manage Courses",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            courses.forEach { (id, title) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onCourseClick(id, title) }
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = onBack) {
            Text("Back")
        }
    }
}
