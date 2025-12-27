package com.himanshu.learnpro.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.ui.theme.*

@Composable
fun ManageLecturesScreen(
    courseId: String,
    courseTitle: String,
    onBack: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    var lectures by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        db.collection("courses")
            .document(courseId)
            .collection("lectures")
            .orderBy("order")
            .get()
            .addOnSuccessListener { snapshot ->
                lectures = snapshot.documents.map { it.data ?: emptyMap() }
            }
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Text(
            text = "Lectures – $courseTitle",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        lectures.forEach { lecture ->
            Text(
                text = "• ${lecture["order"]}. ${lecture["title"]} (${lecture["type"]})",
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = { showAddDialog = true }) {
            Text("Add Lecture")
        }

        Spacer(Modifier.height(10.dp))

        TextButton(onClick = onBack) {
            Text("Back")
        }
    }

    if (showAddDialog) {
        AddLectureDialog(
            courseId = courseId,
            onDismiss = { showAddDialog = false }
        )
    }
}
@Composable
fun AddLectureDialog(
    courseId: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance().reference

    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("video") }
    var order by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // File picker
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Lecture") },
        text = {
            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Lecture Title") }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = order,
                    onValueChange = { order = it },
                    label = { Text("Order (number)") }
                )

                Spacer(Modifier.height(8.dp))

                Row {
                    listOf("video", "audio").forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            RadioButton(
                                selected = type == it,
                                onClick = { type = it }
                            )
                            Text(it)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // 🔹 Upload button
                OutlinedButton(
                    onClick = {
                        pickerLauncher.launch(
                            if (type == "video") "video/*" else "audio/*"
                        )
                    }
                ) {
                    Text(
                        text = if (selectedUri == null)
                            "Select ${type.capitalize()} file"
                        else
                            "File selected"
                    )
                }

                if (error.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !loading,
                onClick = {
                    val orderValue = order.toIntOrNull()

                    if (
                        title.isBlank() ||
                        orderValue == null ||
                        selectedUri == null
                    ) {
                        error = "All fields and file are required"
                        return@Button
                    }

                    loading = true
                    error = ""

                    val lectureId = UUID.randomUUID().toString()
                    val extension =
                        if (type == "video") "mp4" else "mp3"

                    val fileRef = storage
                        .child("courses/$courseId/lectures/$lectureId.$extension")

                    // Upload file
                    fileRef.putFile(selectedUri!!)
                        .addOnSuccessListener {

                            // Save lecture metadata
                            db.collection("courses")
                                .document(courseId)
                                .collection("lectures")
                                .document(lectureId)
                                .set(
                                    mapOf(
                                        "title" to title,
                                        "type" to type,
                                        "order" to orderValue,
                                        "filePath" to fileRef.path,
                                        "createdAt" to Timestamp.now()
                                    )
                                )
                                .addOnSuccessListener {
                                    loading = false
                                    onDismiss()
                                }
                                .addOnFailureListener {
                                    loading = false
                                    error = "Failed to save lecture data"
                                }
                        }
                        .addOnFailureListener {
                            loading = false
                            error = "File upload failed"
                        }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
