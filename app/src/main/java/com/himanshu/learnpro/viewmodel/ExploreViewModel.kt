package com.himanshu.learnpro.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.data.model.Course

class ExploreViewModel : ViewModel() {

    var courses by mutableStateOf<List<Course>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadCourses() {
        isLoading = true
        errorMessage = null

        FirebaseFirestore.getInstance()
            .collection("courses")
            .get()
            .addOnSuccessListener { snapshot ->

                courses = snapshot.documents.mapNotNull { doc ->

                    val title = doc.getString("title") ?: return@mapNotNull null
                    val description = doc.getString("description") ?: ""
                    val category = doc.getString("category") ?: ""
                    val imageUrl = doc.getString("imageUrl") ?: ""
                    val featured = doc.getBoolean("featured") ?: false

                    // 🔥 SAFE PRICE HANDLE (Int / Long / Double)
                    val price = when (val p = doc.get("price")) {
                        is Long -> p.toInt()
                        is Int -> p
                        is Double -> p.toInt()
                        else -> 0
                    }

                    Course(
                        id = doc.id,           // 🔥 document id
                        title = title,
                        description = description,
                        price = price,
                        category = category,
                        featured = featured,
                        imageUrl = imageUrl
                    )
                }

                isLoading = false
            }
            .addOnFailureListener { e ->
                errorMessage = e.message ?: "Failed to load courses"
                isLoading = false
            }
    }
}
