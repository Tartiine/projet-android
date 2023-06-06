package com.example.ensihub

enum class Role {
    ADMIN, MODERATOR, USER
}

data class User(val id: String, val username: String, val email: String, val role: Role)


