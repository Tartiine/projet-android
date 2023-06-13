package com.example.ensihub.mainClasses

import java.util.UUID

data class Post(
    var id: String,
    var text: String,
    val timestamp: Long,
    val author: String,
    var likesCount: Long,
    var isLiked: Boolean = false,
    var imageUrl: String? = null,
    var videoUrl: String? = null,
    var status : PostStatus = PostStatus.PENDING,
    val likes: Map<String, Boolean> = mapOf()
) {
    constructor() : this(UUID.randomUUID().toString(), "", System.currentTimeMillis(), "", 0)

    constructor(text: String, author: String) : this(UUID.randomUUID().toString(), text, System.currentTimeMillis(), author, 0)

    constructor(text: String, author: String, imageUrl: String?) : this(UUID.randomUUID().toString(), text, System.currentTimeMillis(), author, 0,imageUrl = imageUrl)

    constructor(text: String, author: String, imageUrl: String?, videoUrl: String?) : this(UUID.randomUUID().toString(), text, System.currentTimeMillis(), author, 0, imageUrl = imageUrl, videoUrl = videoUrl)

    override fun equals(other: Any?): Boolean {
        return (other is Post) && other.id == this.id
    }
}

enum class PostStatus {
    PENDING, APPROVED
}



