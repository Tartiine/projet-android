package com.example.ensihub.mainClasses

import java.util.UUID

data class Post(
    var id: String,
    var text: String,
    val timestamp: Long,
    val author: String,
    var likesCount: Long,
    var imageUrl: String? = null,
    var videoUrl: String? = null,
    var status : PostStatus = PostStatus.PENDING,
    val likes: List<String> = listOf()
) {
    constructor() : this(UUID.randomUUID().toString(), "", System.currentTimeMillis(), "", 0)

    constructor(text: String, author: String) : this(UUID.randomUUID().toString(), text, System.currentTimeMillis(), author, 0)

    constructor(text: String, author: String, imageUrl: String?) : this(UUID.randomUUID().toString(), text, System.currentTimeMillis(), author, 0,imageUrl = imageUrl)

    constructor(text: String, author: String, imageUrl: String?, videoUrl: String?) : this(UUID.randomUUID().toString(), text, System.currentTimeMillis(), author, 0, imageUrl = imageUrl, videoUrl = videoUrl)

    override fun equals(other: Any?): Boolean {
        return (other is Post) && other.id == this.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + likesCount.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (videoUrl?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        result = 31 * result + likes.hashCode()
        return result
    }
}

enum class PostStatus {
    PENDING, APPROVED
}



