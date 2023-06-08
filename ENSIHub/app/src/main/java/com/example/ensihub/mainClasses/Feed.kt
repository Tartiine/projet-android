package com.example.ensihub.mainClasses

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Feed {
    private val posts = mutableListOf<Post>()
    private val comments = mutableMapOf<String, MutableList<Comment>>()
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private var i: Long = 10

    init {
        db.collection("posts").limit(10).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    val post = Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Int)
                    posts.add(post)
                    loadComments(post)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun loadMore() {
        i += 10
        db.collection("posts").limit(i).whereNotIn("id", posts).get()
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

    fun loadComments(post : Post) {
        db.collection("posts").document(post.id).collection("comments").limit(10).get()
            .addOnSuccessListener { result ->
                val postComments = mutableListOf<Comment>()
                for (document in result) {
                    val data = document.data
                    val comment = Comment(data["id"] as String, data["text"] as String, data["author"] as String, data["timestamp"] as Long, data["likesCount"] as Int)
                    postComments.add(comment)
                }
                comments[post.id] = postComments
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting comments.", exception)
            }
    }

    fun reload() {
        i = 10
        posts.clear()
        comments.clear()
        db.collection("posts").limit(i).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val data = document.data
                    val post = Post(data["id"] as String, data["text"] as String, data["timestamp"] as Long, data["author"] as String, data["likesCount"] as Int)
                    posts.add(post)
                    loadComments(post)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun addPost(post: Post) {
        db.collection("posts").orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener {
                Log.d(TAG, "Success : $it")
                for (document in it) {
                    post.id = (document.data["id"] as Int + 1).toString()
                }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error", it)
                return@addOnFailureListener
            }

        db.collection("posts").add(post)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully sent post: $it")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error while sending post: $it")
            }
    }

    fun addImagePost(post : Post, imageUri : Uri) {
        db.collection("posts").add(post)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Successfully sent image: ${documentReference.id}")
                val postId = documentReference.id
                getImage(imageUri, post)
            }
            .addOnFailureListener {
                Log.w(TAG, "Error while adding post: $it")
            }
    }

    fun addComment(postId : String, comment : Comment) {
        val post = posts.find {it.id == postId}
        if(post != null) {
            db.collection("posts").document(postId).collection("comments").orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener {
                    Log.d(TAG, "Success : $it")
                    for (document in it) {
                        comment.id = (document.data["id"] as Int + 1).toString()
                    }
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error", it)
                    return@addOnFailureListener
                }

            //Update the database with the commented post, if the post exist
            db.collection("posts").document(postId).collection("comments").add(comment)
                .addOnSuccessListener {
                    Log.d(TAG, "Success : $it")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error", e)
                }
        }
    }


    fun deletePost(post:Post){
        db.collection("posts").document(post.id)
            .delete()
            .addOnSuccessListener{
                Log.d(TAG, "Post deleted")
            }
            .addOnFailureListener{
                Log.w(TAG, "Error in deleting the post")
            }
    }

    fun updatePostText(post: Post, newText: String){
        db.collection("posts").document(post.id)
            .update(post.text, newText)
            .addOnSuccessListener {
                println("Post updated")
            }
            .addOnFailureListener{
                println("error in updating the post")

            }
    }

    fun likePost(post: Post){
        val postRef = db.collection("posts").document(post.id)
        val updateData = hashMapOf<String, Any>(
            "likesCount" to post.likesCount + 1)
        postRef.update(updateData)
            .addOnSuccessListener {
                println("+1 like")
            }
            .addOnFailureListener { e ->
                println("Error")
            }
    }


    fun updateCommText(postId : String, comm: Comment, newText: String){
        db.collection("posts").document(postId).collection("comments").document(comm.id)
            .update(comm.text, newText)
            .addOnSuccessListener {
            println("Comment updated")
             }
            .addOnFailureListener{
                println("error in updating the comment")

            }
    }


    fun deleteComment(postId : String, commentId : String) {
        val post = posts.find { it.id == postId}
        if (post != null) {
            db.collection("posts").document(postId).collection("comments").document(commentId).delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Comment deleted")
                }
                .addOnFailureListener {
                    Log.w(TAG, "Cannot deleting the comment")
                }
        }
    }

    fun getData(): MutableList<Post> {
        return this.posts
    }

    fun getComments(postId : String) : MutableList<Comment>? {
        return comments[postId]
    }

    fun search(key: String): List<Post> {
        return this.posts.filter { p ->  p.id == key || p.text?.contains(key) == true }
    }

    fun getImage(imageUri : Uri, post : Post) {
        val storageRef = storage.reference.child("image/${post.id}")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            post.imageUrl = uri.toString()
            db.collection("posts").document(post.id).set(post)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error while uploading image : $exception")
                }
    }

}

