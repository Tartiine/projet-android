package com.example.ensihub.mainClasses

import kotlinx.coroutines.flow.StateFlow

data class Comment (
    var id : String,
    var text : String,
    val author : StateFlow<User?>,
    var timestamp: Long,
    val likesCount: Int,
)