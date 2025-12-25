package com.himanshu.learnpro.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AuthManager {

    // Firebase instances
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // -----------------------------
    // USER LOGIN (EMAIL + PASSWORD)
    // -----------------------------
    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "User login failed")
            }
    }

    // -----------------------------
    // USER SIGNUP (EMAIL + PASSWORD)
    // -----------------------------
    fun signupUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "User signup failed")
            }
    }

    // -----------------------------
    // ADMIN LOGIN (EMAIL + PASSWORD)
    // + FIRESTORE WHITELIST CHECK
    // -----------------------------
    fun loginAdmin(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        // Step 1: Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                // Step 2: Firestore admin whitelist check
                val safeEmail = email
                    .replace("@", "_")
                    .replace(".", "_")

                db.collection("admins")
                    .document(safeEmail)
                    .get()
                    .addOnSuccessListener { document ->

                        val isAllowed = document.exists()
                                && document.getBoolean("allowed") == true

                        if (isAllowed) {
                            onSuccess()
                        } else {
                            auth.signOut()
                            onError("You are not authorized as admin")
                        }
                    }
                    .addOnFailureListener {
                        auth.signOut()
                        onError("Admin permission check failed")
                    }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Admin login failed")
            }
    }

    // -----------------------------
    // LOGOUT (USER / ADMIN BOTH)
    // -----------------------------
    fun logout() {
        auth.signOut()
    }

    // -----------------------------
    // CHECK CURRENT LOGIN STATE
    // -----------------------------
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // -----------------------------
    // GET CURRENT USER EMAIL
    // -----------------------------
    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}
