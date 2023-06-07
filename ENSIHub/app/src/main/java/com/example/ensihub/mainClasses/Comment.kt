package com.example.ensihub.mainClasses

data class Comment (
    val id : String,
    val text : String,
    val author : String,
    var timestamp: Long,
    val likesCount: Int,
)