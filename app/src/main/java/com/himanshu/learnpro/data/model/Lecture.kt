package com.himanshu.learnpro.data.model

data class Lecture(
    val id: String,
    val title: String,
    val type: String,   // "audio" | "video"
    val order: Int
)
