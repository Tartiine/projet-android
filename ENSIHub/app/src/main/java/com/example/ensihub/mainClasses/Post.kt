package com.example.ensihub.mainClasses

import java.util.UUID

/**
 * @class Post
 * @brief Represents a post in the application.
 *
 * This data class contains information about a post, including its ID, text, timestamp, author, likes count,
 * image URL, video URL, status, and the list of user IDs who liked the post.
 *
 * @property id The ID of the post.
 * @property text The text content of the post.
 * @property timestamp The timestamp of when the post was created.
 * @property author The author of the post.
 * @property likesCount The number of likes received by the post.
 * @property imageUrl The URL of an image attached to the post (nullable).
 * @property videoUrl The URL of a video attached to the post (nullable).
 * @property status The status of the post (default: PostStatus.PENDING).
 * @property likes The list of user IDs who liked the post (default: empty list).
 *
 * @constructor Creates a Post instance with the provided parameters.
 * @constructor Creates a Post instance with default values.
 *
 * @function equals Checks if this Post is equal to another object.
 * @function hashCode Generates the hash code for this Post.
 */
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



