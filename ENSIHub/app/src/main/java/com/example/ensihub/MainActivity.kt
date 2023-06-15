package com.example.ensihub

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.LoginViewModel
import com.example.ensihub.mainClasses.Moderation
import com.example.ensihub.ui.screens.Navigation
import com.example.ensihub.ui.theme.ENSIHubTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val REQUEST_IMAGE_SELECTION = 3
    private val REQUEST_VIDEO_SELECTION = 4
    private val viewModel: FeedViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            // Call the changeLikesFieldType function and wait for it to complete
            changeLikesFieldType()
        }

        if (!permissionsCheck()) {
            requestPermissions()
        }

        setContent {
            MainActivityContent(viewModel = viewModel)
        }
    }

 override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null) {
            Firebase.auth.signOut()
        }

    }

    private suspend fun changeLikesFieldType() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("posts")

        try {
            val querySnapshot = collectionRef.get().await()

            val batch = firestore.batch()

            for (document in querySnapshot.documents) {
                val postId = document.id
                val likesArray = document["likes"] as? List<*>
                val likesList = likesArray?.filterIsInstance<String>() ?: emptyList()

                val updatedData = mapOf("likes" to likesList)
                val postRef = collectionRef.document(postId)
                batch.update(postRef, updatedData)
            }

            batch.commit().await()
            println("Likes field type updated for all posts.")
        } catch (exception: Exception) {
            println("Error updating likes field type: ${exception.message}")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainActivityContent(viewModel: FeedViewModel) {
        val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
        val navController = rememberNavController()
        ENSIHubTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Navigation(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    viewModel = viewModel,
                    moderation = Moderation()
                )
            }
        }
    }

    private fun permissionsCheck(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}



