package com.himanshu.learnpro.data.repository

import com.himanshu.learnpro.data.model.Course
import com.google.firebase.firestore.FirebaseFirestore

class CourseRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getAllCourses(
        onSuccess: (List<Course>) -> Unit,
        onError: (String) -> Unit
    ) {
        firestore.collection("courses")
            .get()
            .addOnSuccessListener { snapshot ->
                val courses = snapshot.documents.map { doc ->
                    Course(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getString("Price") ?: "",

                    )
                }
                onSuccess(courses)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load courses")
            }
    }
}
