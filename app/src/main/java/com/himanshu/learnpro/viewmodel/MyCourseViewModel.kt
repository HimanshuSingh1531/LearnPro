package com.himanshu.learnpro.viewmodel


import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.himanshu.learnpro.data.model.Course
import com.himanshu.learnpro.data.repository.CourseRepository
import com.google.firebase.firestore.FirebaseFirestore

class MyCoursesViewModel(
    private val courseRepo: CourseRepository = CourseRepository()
) : ViewModel() {

    var courses by mutableStateOf<List<Course>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private val db = FirebaseFirestore.getInstance()

    fun loadMyCourses(uid: String) {
        isLoading = true
        error = null

        // 🔥 READ PURCHASES (source of truth)
        db.collection("users")
            .document(uid)
            .collection("purchases")
            .get()
            .addOnSuccessListener { snapshot ->

                val purchasedCourseIds = snapshot.documents.map { it.id }

                if (purchasedCourseIds.isEmpty()) {
                    courses = emptyList()
                    isLoading = false
                    return@addOnSuccessListener
                }

                // Fetch all courses & filter
                courseRepo.getAllCourses(
                    onSuccess = { allCourses ->
                        courses = allCourses.filter {
                            it.id in purchasedCourseIds
                        }
                        isLoading = false
                    },
                    onError = { err ->
                        error = err
                        isLoading = false
                    }
                )
            }
            .addOnFailureListener { e ->
                error = e.message ?: "Failed to load purchases"
                isLoading = false
            }
    }
}
