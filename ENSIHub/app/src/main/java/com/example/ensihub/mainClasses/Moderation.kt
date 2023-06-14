package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Moderation : ViewModel() {
    private val db = Firebase.firestore
    private val _pendingPosts = MutableLiveData<List<Post>>()
    private var i: Long = 10
    val pendingPosts : MutableLiveData<List<Post>> = MutableLiveData()
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    init {
        db.collection("posts").whereEqualTo("status", PostStatus.PENDING.name).orderBy("timestamp", Query.Direction.DESCENDING).limit(i).get()
            .addOnSuccessListener { result ->
                val pendingPostList = mutableListOf<Post>()
                for (document in result) {
                    if (document == null) continue
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val post = document.toObject(Post::class.java)
                    pendingPostList.add(post)
                }
                pendingPosts.value = pendingPostList
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun approvePost(post : Post) {
        val userId = Firebase.auth.currentUser?.uid
        currentUser?.let { user ->
            if (userId != null) {
                db.collection("posts")
                    .whereEqualTo("id", post.id)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.update("status", PostStatus.APPROVED.name)
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "Post approved and will be load in the feed")
                                }
                                .addOnFailureListener{ exception ->
                                    Log.w(ContentValues.TAG, "Error by reporting post", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    }
            }
        }
    }

    fun rejectPost(post : Post) {
        val userId = Firebase.auth.currentUser?.uid
        currentUser?.let { user ->
            if (userId != null) {
                db.collection("posts")
                    .whereEqualTo("id", post.id)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.delete()
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "Post rejected and permanently erased")
                                }
                                .addOnFailureListener{ exception ->
                                    Log.w(ContentValues.TAG, "Error by rejecting post", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    }
            }
        }
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
                pendingPosts.value = pendingPostList
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
                pendingPosts.value = pendingPostList
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error in loading more pending posts.", exception)
            }
        }
    }
}