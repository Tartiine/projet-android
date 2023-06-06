package com.example.ensihub

data class Post (
    val id: String,
    val text: String,
    val timestamp: Long,
    val author: String,
    val likesCount: Int,
)
