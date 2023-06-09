package com.example.ensihub.mainClasses

data class Post (
    var id: String,
    val text: String,
    val timestamp: Long,
    val author: String,
    var likesCount: Int,
    var imageUrl : String? = null,
    var videoUrl : String? = null
)

