package com.example.ensihub.mainClasses

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Moderation {
    private val db = Firebase.firestore

    fun approvePost(post : Post) {
        db.collection("posts").document(post.id.toString()).update("status", PostStatus.APPROVED.name)
            .addOnSuccessListener {
                Log.d(TAG, "Post approved and can be load in the feed")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG, "Error approving post", exception)
            }
    }

    fun rejectPost(post : Post) {
        db.collection("posts").document(post.id.toString()).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Post rejected and deleted from the database")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG, "Error rejecting post", exception)
            }
    }

    fun getPendingPosts() : MutableList<Post> {
        val pendingPosts = mutableListOf<Post>()
        db.collection("posts").whereEqualTo("status", PostStatus.PENDING.name).get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    val post = Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Long, data["imageUrl"] as String?, data["videoUrl"] as String?, data["status"] as PostStatus)
                    pendingPosts.add(post)
                }
            }
        return pendingPosts
    }
}