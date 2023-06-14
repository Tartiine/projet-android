package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.net.Uri
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

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

    val isPostLikedByUser = MutableLiveData<Boolean>()

    val likesCountMap = mutableMapOf<String, Long>()

    val isUploading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var i: Long = 10

    init {
        reload()
        fetchCurrentUser()
        loadLikesCountForAllPosts()
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
            db.collection("posts")
                //.whereEqualTo("status", PostStatus.APPROVED.name)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener { documents ->
                    val postList = documents.mapNotNull { document ->
                        val data = document.data
                        Post(
                            id = document.id,
                            text = data["text"] as? String ?: "",
                            timestamp = data["timestamp"] as? Long ?: 0L,
                            author = data["author"] as? String ?: "",
                            likesCount = data["likesCount"] as? Long ?: 0L,
                            imageUrl = data["imageUrl"] as? String ?: "",
                            videoUrl = data["videoUrl"] as? String ?: "",
                            status = PostStatus.PENDING,
                            likes = (data["likes"] as? List<*>)?.map { it.toString() } ?: listOf()
                        )
                        }

                    _posts.value = postList


                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }
    fun loadUserPosts() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            viewModelScope.launch {
                db.collection("users").document(it.uid).get()
                    .addOnSuccessListener { userSnapshot ->
                        val username = userSnapshot["username"] as? String
                        username?.let { uname ->
                            db.collection("posts")
                                .whereEqualTo("author", uname)
                                //.whereEqualTo("status", PostStatus.APPROVED.name)
                                .orderBy("timestamp", Query.Direction.DESCENDING)
                                .get()
                                .addOnSuccessListener { documents ->
                                    val userPostList = documents.mapNotNull { document ->
                                        val data = document.data
                                        data?.let {
                                            Post(
                                                id = document.id,
                                                text = data["text"] as? String ?: "",
                                                timestamp = data["timestamp"] as? Long ?: 0L,
                                                author = data["author"] as? String ?: "",
                                                likesCount = data["likesCount"] as? Long ?: 0L,
                                                imageUrl = data["imageUrl"] as? String ?: "",
                                                videoUrl = data["videoUrl"] as? String ?: "",
                                                status = PostStatus.PENDING,
                                                likes = (data["likes"] as? List<*>)?.map { it.toString() } ?: listOf()
                                            )
                                        }
                                    }
                                    _userPosts.value = userPostList
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "Error getting documents: ", exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting user: ", exception)
                    }
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
                //.whereEqualTo("status", PostStatus.APPROVED.name)
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
                //.whereEqualTo("status", PostStatus.APPROVED.name)
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

    @OptIn(DelicateCoroutinesApi::class)
    fun addPost(post: Post) {
        GlobalScope.launch {
            post.status = PostStatus.PENDING
            val postsRef = db.collection("posts")
            Log.d(TAG, "Sending post: $post")
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

    fun getPost(postId: String): LiveData<Post?> {
        val post = MutableLiveData<Post?>()
        _posts.value?.let { posts ->
            for (p in posts) {
                if (p.id == postId) {
                    post.value = p
                    break
                }
            }
        }
        return post
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

        private fun loadLikesCountForAllPosts() {
            viewModelScope.launch {
                val posts = getPosts()
                posts.forEach { post ->
                    likesCountMap[post.id] = post.likesCount
                }
            }
        }

        suspend fun getPosts(): List<Post> {
            val snapshot = db.collection("posts").get().await()
            return snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
        }


    fun likePost(post: Post) {
        viewModelScope.launch {
            val docSnapshot = db.collection("posts")
                .whereEqualTo("id", post.id)
                .get()
                .await()

            if (!docSnapshot.isEmpty) {
                val docId = docSnapshot.documents[0].id
                db.collection("posts").document(docId)
                    .update("likes", FieldValue.arrayUnion(FirebaseAuth.getInstance().currentUser?.uid))
                    .await()

                likesCountMap[post.id] = likesCountMap[post.id]?.plus(1) ?: 0
                isPostLikedByUser.value = true
            } else {
                // handle error - document not found
            }
        }
    }

    fun unlikePost(post: Post) {
        viewModelScope.launch {
            val docSnapshot = db.collection("posts")
                .whereEqualTo("id", post.id)
                .get()
                .await()

            if (!docSnapshot.isEmpty) {
                val docId = docSnapshot.documents[0].id
                db.collection("posts").document(docId)
                    .update("likes", FieldValue.arrayRemove(FirebaseAuth.getInstance().currentUser?.uid))
                    .await()

                likesCountMap[post.id] = likesCountMap[post.id]?.minus(1) ?: 0
                isPostLikedByUser.value = false
            } else {

            }
        }
    }

        fun isPostLikedByUser(post: Post): Boolean {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            return post.likes.contains(firebaseUser?.uid)
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

    fun reportPost(post : Post) {
        db.collection("posts").document(post.id).update("status", PostStatus.PENDING.name)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Post reported and queued for a new manual review")
            }
            .addOnFailureListener{ exception ->
                Log.w(ContentValues.TAG, "Error by reporting post", exception)
            }
    }
    fun pushImage(image: Uri, post: Post, onCompletion: (() -> Unit)? = null){
        isUploading.value = true
        viewModelScope.launch {
            Log.d(TAG, "Sending image to storage")
            val id = UUID.randomUUID()
            storage.reference.child("Images/${id}")
                .putFile(image)
                .addOnSuccessListener {
                    Log.d(TAG, "Image successfully sent to storage")
                    storage.reference.child("Images/$id").downloadUrl
                        .addOnSuccessListener {
                            Log.d(TAG, "Image url successfully downloaded $it")
                            post.imageUrl = it.toString()
                            if (onCompletion != null) {
                                onCompletion()
                            }
                            isUploading.value = false
                        }
                        .addOnFailureListener {
                            Log.w(TAG, "Error downloading the url of image $it")
                            isUploading.value = false
                        }
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error while sending image $it")
                    isUploading.value = false
                }
        }
    }

    fun refreshPosts() {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimePassed(date: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = Duration.between(date, now)

        val days = duration.toDays()
        if (days > 0) return "$days days ago"

        val hours = duration.toHours()
        if (hours > 0) return "$hours hours ago"

        val minutes = duration.toMinutes()
        if (minutes > 0) return "$minutes minutes ago"

        return "Just now"
    }


    companion object {
        private const val TAG = "FeedViewModel"
    }
}




