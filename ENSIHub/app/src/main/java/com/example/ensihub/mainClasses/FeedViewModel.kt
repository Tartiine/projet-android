package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _comments = MutableLiveData<Map<String, List<Comment>>>()
    val comments: LiveData<Map<String, List<Comment>>>
        get() = _comments

    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()

    private var i: Long = 10

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            db.collection("posts")
                .limit(10)
                .get()
                .addOnSuccessListener { result ->
                    val postList = mutableListOf<Post>()
                    for (document in result) {
                        val data = document.data
                        val post = Post(
                            id = document.id.toLongOrNull() ?: 0L,
                            text = data["text"] as? String ?: "",
                            timestamp = data["timestamp"] as? Long ?: 0,
                            author = data["author"] as? String ?: "",
                            likesCount = data["likesCount"] as? Long ?: 0,
                            imageUrl = data["imageUrl"] as? String ?: ""
                        )
                        postList.add(post)
                        loadComments(post)
                    }
                    _posts.value = postList
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }


    private fun loadComments(post: Post) {
        viewModelScope.launch {
            db.collection("posts")
                .document(post.id.toString())
                .collection("comments")
                .get()
                .addOnSuccessListener { result ->
                    val commentList = mutableListOf<Comment>()
                    for (document in result) {
                        val comment = document.toObject(Comment::class.java)
                        commentList.add(comment)
                    }
                    val updatedComments = _comments.value?.toMutableMap() ?: mutableMapOf()
                    updatedComments[post.id.toString()] = commentList
                    _comments.value = updatedComments
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting comments.", exception)
                }
        }
    }


    fun loadMore() {
        viewModelScope.launch {
            i += 10
            db.collection("posts")
                .limit(i)
                .get()
                .addOnSuccessListener { result ->
                    val postList = mutableListOf<Post>()
                    for (document in result) {
                        val post = document.toObject(Post::class.java)
                        postList.add(post)
                    }
                    _posts.value = _posts.value?.plus(postList)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }

    fun reload() {
        viewModelScope.launch {
            i = 10
            _posts.value = mutableListOf()
            _comments.value = mutableMapOf()
            db.collection("posts")
                .limit(i)
                .get()
                .addOnSuccessListener { result ->
                    val postList = mutableListOf<Post>()
                    for (document in result) {
                        val post = document.toObject(Post::class.java)
                        postList.add(post)
                        loadComments(post)
                    }
                    _posts.value = postList
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            val postsRef = db.collection("posts")
            val lastPostRef = postsRef.orderBy("id", Query.Direction.DESCENDING).limit(1)

            lastPostRef.get()
                .addOnSuccessListener { snapshot ->
                    Log.d(TAG, "Success : $snapshot")
                    var postId = 1L
                    for (document in snapshot) {
                        postId = (document.data["id"] as Long) + 1
                    }
                    post.id = postId

                    postsRef.add(post)
                        .addOnSuccessListener {
                            Log.d(TAG, "Successfully sent post: $it")
                            val updatedPosts = _posts.value?.toMutableList()
                            updatedPosts?.add(post)
                            _posts.value = updatedPosts
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error while sending post: $e")
                        }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error", e)
                    return@addOnFailureListener
                }
        }
    }


    fun addComment(postId: String, comment: Comment) {
        viewModelScope.launch {
            val postCommentsRef = db.collection("posts/${postId}/comments")
                .orderBy("id", Query.Direction.DESCENDING).limit(1)

            postCommentsRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val documents = querySnapshot.documents
                    if (documents.isNotEmpty()) {
                        val lastDocument = documents[0]
                        val lastId = lastDocument.data?.get("id") as? String
                        val newId = lastId?.toIntOrNull()?.plus(1)?.toString()
                        comment.id = newId ?: ""
                    } else {
                        comment.id = "1"
                    }

                    val commentDocumentRef = db.document("posts/${postId}/comments/${comment.id}")
                    commentDocumentRef.set(comment)
                        .addOnSuccessListener {
                            Log.d(TAG, "Successfully added comment: $it")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error while adding comment: $e")
                        }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error", e)
                }
        }
    }



    fun deletePost(post: Post) {
        viewModelScope.launch {
            db.collection("posts").document(post.id.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Post deleted")
                    _posts.value = _posts.value?.filter { it.id != post.id }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error in deleting the post: $e")
                }
        }
    }

    fun updatePostText(post: Post, newText: String) {
        viewModelScope.launch {
            db.collection("posts").document(post.id.toString())
                .update("text", newText)
                .addOnSuccessListener {
                    Log.d(TAG, "Post updated")
                    post.text = newText
                    _posts.value = _posts.value?.map { if (it.id == post.id) post else it }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error in updating the post: $e")
                }
        }
    }

    fun likePost(post: Post) {
        viewModelScope.launch {
            val postRef = db.collection("posts").document(post.id.toString())
            post.likesCount = post.likesCount.plus(1)
            postRef.update("likesCount", post.likesCount)
                .addOnSuccessListener {
                    Log.d(TAG, "+1 like")
                    _posts.value = _posts.value?.map { if (it.id == post.id) post else it }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error in liking the post: $e")
                }
        }
    }


    fun updateCommText(postId: String, comment: Comment, newText: String) {
        viewModelScope.launch {
            db.collection("posts").document(postId).collection("comments").document(comment.id)
                .update("text", newText)
                .addOnSuccessListener {
                    Log.d(TAG, "Comment updated")
                    comment.text = newText
                    val postComments = _comments.value?.get(postId)?.map { if (it.id == comment.id) comment else it }

                    val updatedComments = _comments.value?.toMutableMap()
                    if (postComments != null && updatedComments != null) {
                        updatedComments[postId] = postComments
                        _comments.value = updatedComments
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error in updating the comment: $e")
                }
        }
    }


    fun deleteComment(postId: String, commentId: String)  {
        viewModelScope.launch {
            db.collection("posts").document(postId).collection("comments").document(commentId)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Comment deleted")
                    val postComments = _comments.value?.get(postId)?.toMutableList()
                    postComments?.removeIf { it.id == commentId }
                    val updatedComments = _comments.value?.toMutableMap()
                    if (postComments != null) {
                        updatedComments?.put(postId, postComments)
                    }
                    if (updatedComments != null) {
                        _comments.value = updatedComments
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error in deleting the comment: $e")
                }
        }
    }


    fun getCommentsLiveData(): MutableLiveData<Map<String, List<Comment>>> {
        return _comments
    }

    fun getComments(postId: String): List<Comment>? {
        return _comments.value?.get(postId)
    }

    fun search(key: String) {
        viewModelScope.launch {
            _posts.value = _posts.value?.filter { p ->
                val keyAsLong = key.toLongOrNull()
                if (keyAsLong != null) {
                    p.id == keyAsLong
                } else {
                    p.text.contains(key)
                }
            }
        }
    }


    fun getImage(post: Post) {
        val storageRef = storage.reference.child("image/${post.id}")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            viewModelScope.launch {
                post.imageUrl = uri.toString()
                db.collection("posts").document(post.id.toString()).set(post)
                    .addOnSuccessListener {
                        Log.d(TAG, "Image URL updated for the post: $it")
                        _posts.value = _posts.value?.map { if (it.id == post.id) post else it }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating the image URL for the post: $e")
                    }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error while uploading image: $exception")
            }
    }

    fun sendUser(user: User) {
        viewModelScope.launch {
            db.collection("users").add(user)
                .addOnSuccessListener {
                    Log.d(TAG, "User sent: $it")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error sending the user: $e")
                }
        }
    }

    fun refreshPosts() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val TAG = "FeedViewModel"
    }

}




