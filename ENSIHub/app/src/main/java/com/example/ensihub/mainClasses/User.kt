package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

enum class Role {
    ADMIN, MODERATOR, USER
}

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val role: Role = Role.USER
)
