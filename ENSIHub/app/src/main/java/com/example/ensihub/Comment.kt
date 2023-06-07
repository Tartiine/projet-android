package com.example.ensihub

import java.sql.Timestamp

data class Comment (
    val id : String,
    val text : String,
    val author : String,
    var timestamp: Long,
    val likesCount: Int,
)