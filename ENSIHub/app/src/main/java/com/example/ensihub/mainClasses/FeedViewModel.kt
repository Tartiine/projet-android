package com.example.ensihub.mainClasses

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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

/**
 * @class FeedViewModel
 * @brief ViewModel for managing the feed and posts in an application.
 *
 * This class is responsible for loading, managing, and interacting with posts, comments, and user information.
 */
class FeedViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    private val _comments = MutableLiveData<Map<String, List<Comment>>>()

    private val comments = MutableLiveData<List<Comment>>()


    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val _userPosts = MutableLiveData<List<Post>>()
    val userPosts: LiveData<List<Post>> = _userPosts

    private val _isLikedByUser = MutableStateFlow(false)
    val isLikedByUser: StateFlow<Boolean> get() = _isLikedByUser

    private val _isLikedByUserMap = mutableStateMapOf<String, Boolean>()
    val isLikedByUserMap: Map<String, Boolean> = _isLikedByUserMap

    private val _likesCountMap = mutableStateMapOf<String, Int>()
    val likesCountMap: Map<String, Int> = _likesCountMap

    val isUploading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var i: Long = 10

    private val error = "Error getting documents: "

    /**
     * @brief Initializes the FeedViewModel and fetches the current user's information.
     */
    init {
        reload()
        fetchCurrentUser()
        loadLikesCountForAllPosts()
    }

    /**
     * @brief Fetches the information of the current user from the Firebase Firestore.
     */
    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = Firebase.firestore

        val docRef = uid?.let { db.collection("users").document(it) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                val user = document.toObject(User::class.java)
                _currentUser.value = user
            }
        }?.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    /**
     * @brief Loads the initial data for the feed by fetching approved posts from the Firebase Firestore.
     */
    fun loadInitialData() {
        viewModelScope.launch {
            db.collection("posts")
                .whereEqualTo("status", PostStatus.APPROVED.name)
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
                    Log.w(TAG, error, exception)
                }
        }
    }
    /**
     * @brief Loads the posts authored by the current user from the Firebase Firestore.
     */
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
                                    Log.w(TAG, error, exception)
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting user: ", exception)
                    }
            }
        }
    }


    /**
     * @brief Loads the comments for a specific post from the Firebase Firestore.
     *
     * @param post The post for which to load the comments.
     */
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

    /**
     * @brief Loads more posts for pagination purposes from the Firebase Firestore.
     *
     * @param onCompletion Callback function to be called after loading the additional posts.
     */
    fun loadMore(onCompletion: (() -> Unit)? = null) {
        i += 10
        viewModelScope.launch {
            db.collection("posts")
                .whereEqualTo("status", PostStatus.APPROVED.name)
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
                    Log.w(TAG, error, exception)
                }
        }
    }

    /**
     * @brief Reloads the feed by clearing the existing data and fetching posts from the Firebase Firestore.
     *
     * @param onCompletion Callback function to be called after reloading the feed.
     */
    fun reload(onCompletion: (() -> Unit)? = null) {
        i = 10
        viewModelScope.launch {
            _posts.value = mutableListOf()
            _comments.value = mutableMapOf()
            db.collection("posts")
                .whereEqualTo("status", PostStatus.APPROVED.name)
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
                    Log.w(TAG, error, exception)
                }
        }
    }

    /**
     * @brief Adds a new post to the Firebase Firestore.
     *
     * @param post The post to be added.
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun addPost(post: Post) {
        GlobalScope.launch {
            post.status = PostStatus.PENDING
            val postsRef = db.collection("posts")
            Log.d(TAG, "Sending post: $post")
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
    }

    /**
     * @brief Retrieves a specific post by its ID.
     *
     * @param postId The ID of the post to retrieve.
     * @return LiveData object containing the post.
     */
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

    /**
     * @brief Adds a comment to a specific post in the Firebase Firestore.
     *
     * @param postId The ID of the post to add the comment to.
     * @param comment The comment to be added.
     */
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

    fun isPostLikedByUser(post: Post) {
        viewModelScope.launch {
            val docSnapshot = db.collection("posts")
                .whereEqualTo("id", post.id)
                .get()
                .await()

            if (!docSnapshot.isEmpty) {
                val post = docSnapshot.documents[0].toObject(Post::class.java)
                _isLikedByUserMap[post?.id ?: ""] = post?.likes?.contains(FirebaseAuth.getInstance().currentUser?.uid) ?: false
            } else {
                // handle error - document not found
                _isLikedByUserMap[post.id] = false
            }
        }
    }

    fun loadLikesCountForAllPosts() {
        viewModelScope.launch {
            val posts = getPosts()
            _likesCountMap.clear() // Clear the existing map before updating
            posts.forEach { post ->
                _likesCountMap[post.id] = post.likesCount.toInt()
            }
        }
    }

    private suspend fun getPosts(): List<Post> {
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

                _likesCountMap[post.id] = (_likesCountMap[post.id] ?: 0).plus(1)

                Log.d("FeedViewModel", "Post liked: ${post.id}")
            } else {
                // handle error - document not found
                Log.d("FeedViewModel", "Error: Post not found")
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

                _likesCountMap[post.id] = (_likesCountMap[post.id] ?: 0).minus(1).coerceAtLeast(0)

                Log.d("FeedViewModel", "Post unliked: ${post.id}")
            } else {
                // handle error - document not found
                Log.d("FeedViewModel", "Error: Post not found")
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

    fun getComments(postId: String) {
        viewModelScope.launch {
            val commentsRef = db.collection("posts/${postId}/comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)

            commentsRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val commentsList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Comment::class.java)
                    }
                    comments.value = commentsList
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting comments", e)
                }
        }
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

    /**
     * @brief Reports a post for manual review by changing its status to PENDING in the Firebase Firestore.
     *
     * @param post The post to be reported.
     */
    fun reportPost(post : Post) {
        val userId = Firebase.auth.currentUser?.uid
        currentUser.let {
            if (userId != null) {
                println("look:"+post.id)
                db.collection("posts")
                    .whereEqualTo("id", post.id)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot.documents) {
                            document.reference.update("status", PostStatus.PENDING.name)
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "Post reported and queued for a new manual review")
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

    /**
     * @brief Pushes an image to the Firebase Storage and updates the post's image URL in the Firebase Firestore.
     *
     * @param image The URI of the image to be uploaded.
     * @param post The post to associate the image with.
     * @param onCompletion Callback function to be called after the image upload is completed.
     */
    fun pushImage(image: Uri, post: Post, onCompletion: (() -> Unit)? = null){
        isUploading.value = true
        post.status = PostStatus.PENDING
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

    /**
     * @brief Pushes a video to the Firebase Storage and updates the post's video URL in the Firebase Firestore.
     *
     * @param video The URI of the video to be uploaded.
     * @param post The post to associate the video with.
     * @param onCompletion Callback function to be called after the video upload is completed.
     */
    fun pushVideo(video: Uri, post: Post, onCompletion: (() -> Unit)? = null){
        isUploading.value = true
        post.status = PostStatus.PENDING
        viewModelScope.launch {
            Log.d(TAG, "Sending video to storage")
            val id = UUID.randomUUID()
            storage.reference.child("Videos/${id}")
                .putFile(video)
                .addOnSuccessListener {
                    Log.d(TAG, "Video successfully sent to storage")
                    storage.reference.child("Videos/$id").downloadUrl
                        .addOnSuccessListener {
                            Log.d(TAG, "Video url successfully downloaded $it")
                            post.videoUrl = it.toString()
                            if (onCompletion != null) {
                                onCompletion()
                            }
                            isUploading.value = false
                        }
                        .addOnFailureListener {
                            Log.w(TAG, "Error downloading the url of video $it")
                            isUploading.value = false
                        }
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error while sending video $it")
                    isUploading.value = false
                }
        }
    }

    /**
     * @brief Converts a timestamp to a LocalDateTime object in the local time zone.
     *
     * @param timestamp The timestamp to convert.
     * @return The LocalDateTime object representing the converted timestamp.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    }

    /**
     * @brief Calculates the time passed since a given LocalDateTime object until now.
     *
     * @param date The LocalDateTime object representing the starting date.
     * @return A string indicating the time passed, such as "X days ago", "X hours ago", etc.
     */
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




