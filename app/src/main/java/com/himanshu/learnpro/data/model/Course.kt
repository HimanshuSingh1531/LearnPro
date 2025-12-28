package com.himanshu.learnpro.data.model

data class Course(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Int = 0,
    val category: String,
    val featured: Boolean,
    val imageUrl: String
)
