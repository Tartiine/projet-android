package com.example.ensihub.mainClasses

import android.util.Log
import com.google.firebase.firestore.Query
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val postId: String): ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments


    private val db = Firebase.firestore

    private val _currentUser = MutableStateFlow<User?>( null)
    val currentUser: StateFlow<User?> get() = _currentUser


    init {
        loadInitialComments()
    }


    private fun loadInitialComments() {
        viewModelScope.launch {
            Log.d(TAG, "Loading")
            db.collection("comments")
                .whereEqualTo("postId", postId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "Document ${it.documents}")
                    val mutableComments = mutableListOf<Comment>()
                    for (document in it) {
                        val comment = document.toObject(Comment::class.java)
                        mutableComments.add(comment)
                    }
                    _comments.value = mutableComments
                    Log.d(TAG, "Successfully loaded comments")
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error while loading comments: $it")
                }
        }

    }


    fun addComment(comment: Comment) {
        viewModelScope.launch {
            db.collection("comments")
                .add(comment)
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully sent comment to firebase")
                    _comments.value = _comments.value?.plus(comment)
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error while sending comment: $it")
                }
        }
    }

    companion object {
        private const val TAG = "CommentViewModel"
    }

}