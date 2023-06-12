package com.example.ensihub.mainClasses

data class Post(
    var id: String? = null,  // Make the id nullable
    var text: String,
    val timestamp: Long,
    val author: String,
    var likesCount: Long,
    var imageUrl: String? = null,
    var videoUrl: String? = null,
    var status : PostStatus = PostStatus.PENDING
)

enum class PostStatus {
    PENDING, APPROVED
}



