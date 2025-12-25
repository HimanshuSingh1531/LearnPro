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
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { snapshot ->
                courses = snapshot.documents.map { doc ->
                    Course(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getString("price") ?: ""
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
