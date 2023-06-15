package com.example.ensihub.mainClasses

import java.util.UUID


/**
 * @brief Represents a comment object.
 *
 * This class represents a comment that can be associated with a post.
 */
data class Comment(
    /**
     * @brief The unique identifier of the comment.
     */
    var id: String,

    /**
     * @brief The identifier of the post to which the comment belongs.
     */
    var postId: String,

    /**
     * @brief The text content of the comment.
     */
    var text: String,

    /**
     * @brief The author of the comment.
     */
    val author: String,

    /**
     * @brief The timestamp when the comment was created.
     */
    var timestamp: Long,

    /**
     * @brief The number of likes the comment has received.
     */
    val likesCount: Int,
) {
    /**
     * @brief Default constructor.
     *
     * Initializes a new instance of the [Comment] class with default property values.
     */
    constructor() : this(
        UUID.randomUUID().toString(),
        "",
        "",
        "",
        System.currentTimeMillis(),
        0
    )

    /**
     * @brief Constructor with specific property values.
     *
     * Initializes a new instance of the [Comment] class with the provided property values.
     *
     * @param postId The identifier of the post to which the comment belongs.
     * @param text The text content of the comment.
     * @param author The author of the comment.
     */
    constructor(postId: String, text: String, author: String) : this(
        UUID.randomUUID().toString(),
        postId,
        text,
        author,
        System.currentTimeMillis(),
        0
    )
}
