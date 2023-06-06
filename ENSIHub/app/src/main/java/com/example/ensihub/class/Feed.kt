package com.example.ensihub.back

import android.content.ContentValues.TAG
import android.util.Log
import com.example.ensihub.Post
import com.example.ensihub.login.LoginUiState

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Feed {
    private val posts = mutableListOf<Post>()
    private val db = Firebase.firestore
    private var i: Long = 10

    init {
        db.collection("posts").limit(10).orderBy("timestamp").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    posts.add(Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Int))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun loadMore() {
        i += 10
        db.collection("posts").limit(i).whereNotIn("id", posts).orderBy("timestamp").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    posts.add(Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Int))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun reload() {
        i = 10
        posts.clear()
        db.collection("posts").limit(i).orderBy("timestamp").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    posts.add(Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Int))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun addPost(log: LoginUiState, post: Post) {
        if (!log.isSuccessLogin) return

        db.collection("posts").add(post)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully sent post: $it")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error while sending post: $it")
            }
    }

    fun deletePost(post: Post){
        db.collection("posts").document(post.id)
            .delete()
            .addOnSuccessListener{
                Log.d(TAG, "Post deleted")
            }
            .addOnFailureListener{
                Log.w(TAG, "Error in deleting the post")
            }
    }

    fun getData(): List<Post> {
        return this.posts
    }

    fun search(key: String): List<Post> {
        return this.posts.filter { p ->  p.author.contains(key) || p.text.contains(key) }
    }

}