package com.example.ensihub.mainClasses

data class Comment (
    var id : String,
    var text : String,
    val author : String,
    var timestamp: Long,
    val likesCount: Int,
)