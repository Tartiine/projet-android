package com.example.ensihub.mainClasses

enum class Role {
    ADMIN, MODERATOR, USER
}

/**
 * @class User
 * @brief Represents a user in the application.
 *
 * This data class contains information about a user, including their ID, username, email, and role.
 *
 * @property id The ID of the user.
 * @property username The username of the user.
 * @property email The email address of the user.
 * @property role The role of the user (default: Role.USER).
 */
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val role: Role = Role.USER
)
