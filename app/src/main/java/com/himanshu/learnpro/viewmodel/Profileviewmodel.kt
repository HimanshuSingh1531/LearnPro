package com.himanshu.learnpro.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.himanshu.learnpro.data.model.UserProfile
import com.himanshu.learnpro.data.repository.UserRepository

class ProfileViewModel(
    private val repo: UserRepository = UserRepository()
) : ViewModel() {

    //NULL means "not loaded / not found"
    var profile by mutableStateOf<UserProfile?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var isEditMode by mutableStateOf(false)
        private set

    fun loadProfile(uid: String) {
        isLoading = true
        error = null

        repo.getUserProfile(
            uid = uid,
            onSuccess = { userProfile ->
                profile = userProfile
                isLoading = false
            },
            onError = { message ->
                profile = null
                error = message
                isLoading = false
            }
        )
    }

    fun toggleEdit() {
        isEditMode = !isEditMode
    }

    fun saveEdits(uid: String, updated: UserProfile) {
        isLoading = true
        error = null

        repo.updateUserProfile(
            uid,
            updated,
            onSuccess = {
                profile = updated
                isEditMode = false
                isLoading = false
            },
            onError = { message ->
                error = message
                isLoading = false
            }
        )
    }
}