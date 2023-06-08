package com.example.ensihub

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.mainClasses.LoginViewModel
import com.example.ensihub.ui.screens.Navigation
import com.example.ensihub.ui.theme.ENSIHubTheme
import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ensihub.mainClasses.BottomNavGraph
import com.google.firebase.auth.ktx.auth
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ensihub.mainClasses.BottomNavGraph
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.UUID


class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val REQUEST_IMAGE_SELECTION = 3

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            val navController = rememberNavController()
            val navController1 = rememberNavController()

            // Collect the login state
            val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

            ENSIHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isLoggedIn) {
                        // User is logged in, show the BottomNavGraph
                        BottomNavGraph(navController = navController)
                    } else {
                        // User is not logged in, show the Login/SignUp screens
                        Navigation(navController = navController1, loginViewModel = loginViewModel)
                    }
                }
            }
        }

        if (!permissionsCheck()) {
            requestPermissions()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            Firebase.auth.signOut()
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            } else {
                //You will not be able to post photos on EnsiHub without these permissions.
                // Please check settings -> Applications -> Permissions, if you want to have the complete experience of EnsiHub.
            }
        }
    }

    fun showImagePicker() {
        val items = arrayOf<CharSequence>("Take photo", "Import from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Post Photo")
        builder.setItems(items) { dialog, item ->
            when {
                items[item] == "Take photo" -> {
                    val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    try {
                        startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE)
                    } catch (e: ActivityNotFoundException) {
                        //Cannot access to the camera
                    }
                }

                items[item] == "Import from Gallery" -> {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    try {
                        startActivityForResult(pickPhoto, REQUEST_IMAGE_SELECTION)
                    } catch (e: ActivityNotFoundException) {
                        //Cannot access to the gallery
                    }
                }

                items[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val photo: Bitmap = data?.extras?.get("data") as Bitmap
                    val lightPhoto = Bitmap.createScaledBitmap(photo, photo.width / 2, photo.height / 2, true)
                    val photoArray = ByteArrayOutputStream()
                    lightPhoto.compress(Bitmap.CompressFormat.JPEG, 50, photoArray)
                    val data = photoArray.toByteArray()
                    val storage = Firebase.storage
                    val storageRef = storage.reference.child("images/${UUID.randomUUID()}.jpg")
                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnFailureListener { exception ->
                        Log.e(TAG, "Upload failed", exception)
                    }.addOnSuccessListener {
                        Log.d(TAG, "Upload successfully")
                    }
                }

                REQUEST_IMAGE_SELECTION -> {
                    val selectedImageUri : Uri? = data?.data
                    val imageStream = contentResolver.openInputStream(selectedImageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val lightImage = Bitmap.createScaledBitmap(selectedImage, selectedImage.width / 2, selectedImage.height / 2, true)
                    val imageArray = ByteArrayOutputStream()
                    lightImage.compress(Bitmap.CompressFormat.JPEG, 50, imageArray)
                    val data = imageArray.toByteArray()
                    val storage = Firebase.storage
                    val storageRef = storage.reference.child("images/${selectedImageUri?.lastPathSegment}")
                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnFailureListener { exception ->
                        Log.e(TAG, "Upload failed", exception)
                    }.addOnSuccessListener {
                        Log.d(TAG, "Upload successfully")
                    }
                }
            }
        }
    }
}

/*
Test Post
@Preview
@Composable
fun PostViewPreview() {
val post = Post(
id = "123",
text = "Yo la street.",
timestamp = System.currentTimeMillis(),
author = "Marian chef eco-conception",
likesCount = 5
)
PostView(post)
}

Button(onClick = { yourViewModel.showImagePicker() }) {
Text("Import an image")
}

*/



