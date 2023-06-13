package com.example.ensihub.mainClasses

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ensihub.ui.screens.posts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.firebase.firestore.Query

class Moderation : ViewModel() {
    private val db = Firebase.firestore
    private val _pendingPosts = MutableLiveData<List<Post>>()
    private var i: Long = 10
    val pendingPosts : MutableLiveData<List<Post>> = MutableLiveData()


    fun approvePost(post : Post) {
        db.collection("posts").document(post.id).update("status", PostStatus.APPROVED.name)
            .addOnSuccessListener {
                Log.d(TAG, "Post approved and can be load in the feed")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG, "Error by approving post", exception)
            }
    }

    fun rejectPost(post : Post) {
        db.collection("posts").document(post.id).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Post rejected and deleted from the database")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG, "Error by rejecting post", exception)
            }
    }

    fun reportPost(post : Post) {
        db.collection("posts").document(post.id).update("status", PostStatus.PENDING.name)
            .addOnSuccessListener {
                Log.d(TAG, "Post reported and queued for a new manual review")
            }
            .addOnFailureListener{ exception ->
                Log.w(TAG, "Error by reporting post", exception)
            }
    }

    fun getPendingPosts() {
        db.collection("posts").whereEqualTo("status", PostStatus.PENDING.name).get()
            .addOnSuccessListener { result ->
                val posts = mutableListOf<Post>()
                for(document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    val post = Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Long, data["imageUrl"] as String?, data["videoUrl"] as String?, data["status"] as PostStatus)
                    posts.add(post)
                }
            }
            pendingPosts.value = posts
    }

    fun reloadPendingPosts() {
        viewModelScope.launch {
            i = 10
            _pendingPosts.value = mutableListOf()
            db.collection("posts").whereEqualTo("status", PostStatus.PENDING.name).orderBy("timestamp", Query.Direction.DESCENDING).limit(i).get().addOnSuccessListener { result ->
                val pendingPostList = mutableListOf<Post>()
                for (document in result) {
                    val post = document.toObject(Post::class.java)
                    pendingPostList.add(post)
                }
                _pendingPosts.value = pendingPostList
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error in reloading more pending posts.", exception)
                }
        }
    }

    fun loadMorePendingPosts() {
        viewModelScope.launch {
            i+=10
            db.collection("posts").whereEqualTo("status", PostStatus.PENDING.name).orderBy("timestamp", Query.Direction.DESCENDING).limit(i).get().addOnSuccessListener { result ->
                val pendingPostList = mutableListOf<Post>()
                for (document in result) {
                    val post = document.toObject(Post::class.java)
                    pendingPostList.add(post)
                }
                _pendingPosts.value = _pendingPosts.value?.plus(pendingPostList)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error in loading more pending posts.", exception)
            }
        }
    }
}