package com.example.ensihub.mainClasses

import java.util.UUID


data class Comment (
    var id : String,
    var postId: String,
    var text : String,
    val author : String,
    var timestamp: Long,
    val likesCount: Int,
){
    constructor(): this(UUID.randomUUID().toString(), "", "", "", System.currentTimeMillis(), 0)
    constructor(postId: String, text: String, author: String): this(UUID.randomUUID().toString(), postId, text, author, System.currentTimeMillis(), 0)

}
