package com.himanshu.learnpro.data.repository

import  com.himanshu.learnpro.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getUserProfile(
        uid: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val profile = UserProfile(
                        name = doc.getString("name") ?: "",
                        phone = doc.getString("phone") ?: "",
                        bio = doc.getString("bio") ?: "",
                        avatarId = doc.getString("avatarId") ?: ""
                    )
                    onSuccess(profile)
                } else {
                    onError("Profile not found")
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load profile")
            }
    }

    fun updateUserProfile(
        uid: String,
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val updates = mapOf(
            "name" to profile.name,
            "phone" to profile.phone,
            "bio" to profile.bio,
            "avatarId" to profile.avatarId
        )

        firestore.collection("users")
            .document(uid)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to update profile")
            }
    }
}
