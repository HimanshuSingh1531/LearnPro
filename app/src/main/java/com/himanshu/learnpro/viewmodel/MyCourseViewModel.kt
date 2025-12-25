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

    fun loadMyCourses(uid: String) {
        isLoading = true
        error = null

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val enrolledIds =
                    doc.get("enrolledCourses") as? List<String> ?: emptyList()

                if (enrolledIds.isEmpty()) {
                    courses = emptyList()
                    isLoading = false
                    return@addOnSuccessListener
                }

                courseRepo.getAllCourses(
                    onSuccess = { allCourses ->
                        courses = allCourses.filter { it.id in enrolledIds }
                        isLoading = false
                    },
                    onError = {
                        error = it
                        isLoading = false
                    }
                )
            }
            .addOnFailureListener {
                error = it.message
                isLoading = false
            }
    }
}
