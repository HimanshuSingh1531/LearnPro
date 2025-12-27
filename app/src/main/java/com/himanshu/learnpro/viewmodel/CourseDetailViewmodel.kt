package com.himanshu.learnpro.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.himanshu.learnpro.data.model.Course
import com.himanshu.learnpro.data.repository.CourseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.data.model.Lecture


class CourseDetailViewModel(
    private val courseId: String,
    private val userId: String
) : ViewModel() {

    var course by mutableStateOf<Course?>(null)
        private set

    var lectures by mutableStateOf<List<Lecture>>(emptyList())
        private set

    var hasPurchased by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(true)
        private set

    private val db = FirebaseFirestore.getInstance()

    init {
        loadCourse()
        checkPurchase()
        loadLectures()
    }

    private fun loadCourse() {
        db.collection("courses")
            .document(courseId)
            .get()
            .addOnSuccessListener { doc ->
                course = Course(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    description = doc.getString("description") ?: "",
                    price = doc.getLong("price")?.toInt() ?: 0
                )

                isLoading = false
            }
    }

    private fun loadLectures() {
        db.collection("courses")
            .document(courseId)
            .collection("lectures")
            .orderBy("order")
            .get()
            .addOnSuccessListener { snapshot ->
                lectures = snapshot.documents.map {
                    Lecture(
                        id = it.id,
                        title = it.getString("title") ?: "",
                        type = it.getString("type") ?: "",
                        order = it.getLong("order")?.toInt() ?: 0
                    )
                }
            }
    }

    private fun checkPurchase() {
        db.collection("users")
            .document(userId)
            .collection("purchases")
            .document(courseId)
            .get()
            .addOnSuccessListener {
                hasPurchased = it.exists()
            }
    }
    fun refreshPurchase() {
        checkPurchase()
    }

}
