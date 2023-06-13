package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val _userPosts = MutableLiveData<List<Post>>()
    val userPosts: LiveData<List<Post>> = _userPosts

    var i: Long = 10


    init {
        reload()
        fetchCurrentUser()
    }

    fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = Firebase.firestore

        val docRef = uid?.let { db.collection("users").document(it) }
        if (docRef != null) {
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject(User::class.java)
                    _currentUser.value = user
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        }
    }

    fun loadInitialData() {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { snapshots ->
//                    if (e != null) {
//                        Log.w(TAG, "Listen failed.", e)
//                        return@addSnapshotListener
//                    }

                    val postList = mutableListOf<Post>()
                    for (document in snapshots) {
                        val data = document.data
                        val post = Post(
                            id = document.id,
                            text = data["text"] as? String ?: "",
                            timestamp = data["timestamp"] as? Long ?: 0,
                            author = data["author"] as? String ?: "",
                            likesCount = data["likesCount"] as? Long ?: 0,
                            isLiked = data["isLiked"] as? Boolean ?: 0,
                            imageUrl = data["imageUrl"] as? String ?: "",
                            videoUrl = data["videoUrl"] as? String ?: "",
                            status = PostStatus.PENDING,
                            likes = data["likes"] as? Map<*, *> ?: 0
                        )
                        postList.add(post)
                    }

                    _posts.value = if (postList.size > 10) postList.take(10) else postList

                    if (currentUser != null) {
                        loadUserPosts(currentUser)
                    }
                }
        }
    }

    private fun loadUserPosts(currentUser: FirebaseUser) {
        viewModelScope.launch {
            // fetch the user object for currentUser
            db.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    val username = user?.username

                    // now fetch the posts for this username
                    db.collection("posts")
                        .whereEqualTo("author", username)
                        .get()
                        .addOnSuccessListener { result ->
                            val userPosts = mutableListOf<Post>()
                            Log.d(TAG, "Successfully fetched user posts from Firebase, found ${result.size()} posts")

                            for (document in result) {
                                val data = document.data
                                val post = Post(
                                    id = document.id,
                                    text = data["text"] as? String ?: "",
                                    timestamp = data["timestamp"] as? Long ?: 0,
                                    author = data["author"] as? String ?: "",
                                    likesCount = data["likesCount"] as? Long ?: 0,
                                    imageUrl = data["imageUrl"] as? String ?: "",
                                    videoUrl = data["videoUrl"] as? String ?: "",
                                    status = PostStatus.PENDING
                                )
                                userPosts.add(post)
                            }

                            _userPosts.value = userPosts
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "Error getting user's posts.", exception)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting user data.", exception)
                }
        }
    }


    private fun loadComments(post: Post) {
        post.id?.let { postId ->  // Let block will execute only if postId is not null
            viewModelScope.launch {
                db.collection("posts")
                    .document(postId)
                    .collection("comments")
                    .get()
                    .addOnSuccessListener { result ->
                        val commentList = mutableListOf<Comment>()
                        for (document in result) {
                            val comment = document.toObject(Comment::class.java)
                            commentList.add(comment)
                        }
                        val updatedComments = _comments.value?.toMutableMap() ?: mutableMapOf()
                        updatedComments[postId] = commentList
                        _comments.value = updatedComments
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting comments.", exception)
                    }
            }
        }
    }


    fun loadMore(onCompletion: (() -> Unit)? = null) {
        i += 10
        viewModelScope.launch {
            db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(i)
                .get()
                .addOnSuccessListener { result ->
                    val postList = mutableListOf<Post>()
                    for (document in result) {
                        val post = document.toObject(Post::class.java)
                        postList.add(post)
                    }
                    _posts.value = postList
                    if (onCompletion != null) {
                        onCompletion()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }



        }
    }

    fun reload(onCompletion: (() -> Unit)? = null) {
        i = 10
        viewModelScope.launch {
            _posts.value = mutableListOf()
            _comments.value = mutableMapOf()
            db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
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
                    if (onCompletion != null) {
                        onCompletion()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            post.status = PostStatus.PENDING
            val postsRef = db.collection("posts")

            postsRef.add(post)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Successfully sent post: $documentReference")
                    post.id = documentReference.id  // Set the Firestore document's ID to the post's ID field

                    val updatedPosts = _posts.value?.toMutableList()
                    updatedPosts?.add(post)
                    _posts.value = updatedPosts
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error while sending post: $e")
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
            db.collection("posts").whereEqualTo("id", post.id.toString())
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val documentSnapshot = documents.documents[0] // Assuming only one document will match
                        val postRef = db.collection("posts").document(documentSnapshot.id)
                        val updatedLikesCount = post.likesCount + 1
                        postRef.update("likesCount", updatedLikesCount)
                            .addOnSuccessListener {
                                Log.d(TAG, "+1 like")
                                val updatedPosts = _posts.value?.toMutableList()
                                val updatedPostIndex = updatedPosts?.indexOfFirst { it.id == post.id }
                                if (updatedPostIndex != null && updatedPostIndex != -1) {
                                    val updatedPost = post.copy(likesCount = updatedLikesCount)
                                    updatedPosts[updatedPostIndex] = updatedPost
                                    _posts.value = updatedPosts
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error in liking the post: $e")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    fun unlikePost(post: Post) {
        try {
            Log.d(TAG, "likePost method started for postId = ${post.id}")
            // ... rest of method ...
        } catch (e: Exception) {
            Log.e(TAG, "Exception in likePost method for postId = ${post.id}", e)
        }
        viewModelScope.launch {
            db.collection("posts").whereEqualTo("id", post.id.toString())
                .get()
                .addOnSuccessListener { documents ->
                    Log.w(TAG, "WESH ALORS !!!")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val documentSnapshot = documents.documents[0] // Assuming only one document will match
                        val postRef = db.collection("posts").document(documentSnapshot.id)
                        val updatedLikesCount = if (post.likesCount > 0) post.likesCount - 1 else 0  // Ensure that likesCount never goes below 0
                        postRef.update("likesCount", updatedLikesCount)
                            .addOnSuccessListener {
                                Log.d(TAG, "-1 like")
                                val updatedPosts = _posts.value?.toMutableList()
                                val updatedPostIndex = updatedPosts?.indexOfFirst { it.id == post.id }
                                if (updatedPostIndex != null && updatedPostIndex != -1) {
                                    val updatedPost = post.copy(likesCount = updatedLikesCount)
                                    updatedPosts[updatedPostIndex] = updatedPost
                                    _posts.value = updatedPosts
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error in unliking the post: $e")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
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
                p.id == key || p.text.contains(key)
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




