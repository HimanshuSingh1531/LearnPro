package com.himanshu.learnpro.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.himanshu.learnpro.ui.theme.*

@Composable
fun AddCourseScreen(
    onBack: () -> Unit
) {

    // 🔹 Form fields
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var featured by remember { mutableStateOf(false) }

    // 🔹 UI states
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // 🔝 Header
            Text(
                text = "Add New Course",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SoftBlack
            )

            Text(
                text = "This course will be visible to users",
                fontSize = 14.sp,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 📘 Course Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Course title") },
                modifier = Modifier.fillMaxWidth()
            )

            // 📝 Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Course description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // 💰 Price
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                placeholder = { Text("Price (₹)") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🏷 Category
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                placeholder = { Text("Category (e.g. Android, DSA)") },
                modifier = Modifier.fillMaxWidth()
            )

            // ⭐ Featured
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = featured,
                    onCheckedChange = { featured = it }
                )
                Text(
                    text = "Mark as Featured",
                    fontSize = 14.sp,
                    color = SoftBlack
                )
            }

            // ❌ Error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            // ✅ Success
            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = Color(0xFF2E7D32),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ➕ Add Course Button
            Button(
                onClick = {

                    errorMessage = ""
                    successMessage = ""

                    if (title.isBlank() || description.isBlank() ||
                        price.isBlank() || category.isBlank()
                    ) {
                        errorMessage = "All fields are required"
                        return@Button
                    }

                    val priceValue = price.toIntOrNull()
                    if (priceValue == null) {
                        errorMessage = "Price must be a number"
                        return@Button
                    }

                    loading = true

                    val courseData = hashMapOf(
                        "title" to title,
                        "description" to description,
                        "price" to priceValue,
                        "category" to category,
                        "featured" to featured,
                        "createdBy" to "admin",
                        "createdAt" to Timestamp.now()
                    )

                    db.collection("courses")
                        .add(courseData)
                        .addOnSuccessListener {
                            loading = false
                            successMessage = "Course added successfully"

                            // Reset form
                            title = ""
                            description = ""
                            price = ""
                            category = ""
                            featured = false
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            errorMessage = e.message ?: "Failed to add course"
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
                        text = "Add Course",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 🔙 Back
            TextButton(onClick = onBack) {
                Text("Back to Dashboard")
            }
        }
    }
}
