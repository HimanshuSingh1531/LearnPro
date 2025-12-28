package com.himanshu.learnpro.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.ui.theme.*

@Composable
fun AddCourseScreen(
    onBack: () -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    // 🔁 Toggle between LIST and FORM
    var showForm by remember { mutableStateOf(false) }

    // 🔹 Course list
    var courses by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    // 🔹 Listen courses (real-time)
    LaunchedEffect(Unit) {
        db.collection("courses")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    courses = snapshot.documents.mapNotNull { it.data }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {

        // ===============================
        // 📋 COURSE LIST VIEW (DEFAULT)
        // ===============================
        if (!showForm) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                Text(
                    text = "Added Courses",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftBlack
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (courses.isEmpty()) {
                    Text(
                        text = "No courses added yet",
                        color = SoftGray
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(courses) { course ->

                            val title = course["title"]?.toString() ?: ""
                            val price = course["price"]?.toString() ?: ""
                            val category = course["category"]?.toString() ?: ""
                            val imageUrl = course["imageUrl"]?.toString() ?: ""

                            // 🔥 BIG PROFESSIONAL COURSE CARD
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = SurfaceWhite
                                ),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Column {

                                    // 🖼️ BIG BANNER IMAGE
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .background(Color.LightGray),
                                        contentScale = ContentScale.Crop
                                    )

                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {

                                        Text(
                                            text = title,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SoftBlack
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = category,
                                            fontSize = 13.sp,
                                            color = SoftGray
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "₹$price",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SoftBlack
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "For full batch",
                                                fontSize = 12.sp,
                                                color = SoftGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ➕ Floating Action Button
            FloatingActionButton(
                onClick = { showForm = true },
                containerColor = SoftBlack,
                contentColor = SurfaceWhite,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        }

        // ===============================
        // ➕ ADD COURSE FORM
        // ===============================
        if (showForm) {

            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var price by remember { mutableStateOf("") }
            var category by remember { mutableStateOf("") }
            var featured by remember { mutableStateOf(false) }
            var imageUrl by remember { mutableStateOf("") }

            var loading by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

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

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Course title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Course description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    placeholder = { Text("Price (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    placeholder = { Text("Category (e.g. Android, DSA)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = { Text("Thumbnail Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = featured,
                        onCheckedChange = { featured = it }
                    )
                    Text("Mark as Featured", color = SoftBlack)
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = {

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
                            "imageUrl" to imageUrl,
                            "createdAt" to Timestamp.now()
                        )

                        db.collection("courses")
                            .add(courseData)
                            .addOnSuccessListener {
                                loading = false
                                showForm = false
                            }
                            .addOnFailureListener {
                                loading = false
                                errorMessage = it.message ?: "Failed to add course"
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftBlack,
                        contentColor = SurfaceWhite
                    )
                ) {
                    Text("Add Course")
                }

                TextButton(onClick = { showForm = false }) {
                    Text("Cancel")
                }
            }
        }
    }
}
