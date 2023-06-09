package com.example.ensihub.mainClasses

data class Post (
    var id: Long,
    val text: String,
    val timestamp: Long,
    val author: String,
    var likesCount: Long,
    var imageUrl : String? = null,
    var videoUrl : String? = null
)

