package com.example.ensihub

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import com.example.ensihub.mainClasses.SharedViewModel
import com.example.ensihub.ui.screens.Navigation
import com.example.ensihub.ui.theme.ENSIHubTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.UUID


class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val REQUEST_IMAGE_SELECTION = 3
    private val REQUEST_VIDEO_SELECTION = 4
    private val viewModel: FeedViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.loadInitialData()
        if (!permissionsCheck()) {
            requestPermissions()
        }

        setContent {
            MainActivityContent(viewModel = viewModel)
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            } else {
                //You will not be able to post photos on EnsiHub without these permissions.
                // Please check settings -> Applications -> Permissions, if you want to have the complete experience of EnsiHub.
            }
        }
    }

    fun showImagePicker() {
        val items = arrayOf<CharSequence>("Take photo", "Import from Gallery", "Select video", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Post media content")
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
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    try {
                        startActivityForResult(pickPhoto, REQUEST_IMAGE_SELECTION)
                    } catch (e: ActivityNotFoundException) {
                        //Cannot access to the gallery
                    }
                }

                items[item] == "Select video" -> {
                    val pickVideo = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    try {
                        startActivityForResult(pickVideo, REQUEST_VIDEO_SELECTION)
                    } catch (e : ActivityNotFoundException) {
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
        val sharedViewModel : SharedViewModel by viewModels()
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val photo : Bitmap = data?.extras?.get("data") as Bitmap
                    val lightPhoto = lightImage(photo)
                    val photoArray = ByteArrayOutputStream()
                    lightPhoto.compress(Bitmap.CompressFormat.JPEG, 75, photoArray)
                    val data = photoArray.toByteArray()
                    val storage = Firebase.storage
                    val storageRef = storage.reference.child("Images/${UUID.randomUUID()}.jpg")
                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnFailureListener { exception ->
                        Log.e(TAG, "Upload failed", exception)
                    }.addOnSuccessListener {
                        Log.d(TAG, "Upload successfully")
                    }.addOnCompleteListener{ task ->
                        if(task.isSuccessful) {
                            val downloadUri = task.result
                            val imageUrl = downloadUri.toString()
                            sharedViewModel.imageUrl.value = imageUrl
                        }
                        else {

                        }
                    }
                }

                REQUEST_IMAGE_SELECTION -> {
                    val selectedImageUri : Uri? = data?.data
                    val imageStream = contentResolver.openInputStream(selectedImageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val lightImage = lightImage(selectedImage)
                    val imageArray = ByteArrayOutputStream()
                    lightImage.compress(Bitmap.CompressFormat.JPEG, 75, imageArray)
                    val data = imageArray.toByteArray()
                    val storage = Firebase.storage
                    val storageRef = storage.reference.child("Images/${selectedImageUri?.lastPathSegment}")
                    val uploadTask = storageRef.putBytes(data)
                    uploadTask.addOnFailureListener { exception ->
                        Log.e(TAG, "Upload failed", exception)
                    }.addOnSuccessListener {
                        Log.d(TAG, "Upload successfully")
                    }
                }

                REQUEST_VIDEO_SELECTION -> {
                    val selectedVideoUri : Uri? = data?.data
                    val videoSize = contentResolver.openFileDescriptor(selectedVideoUri!!, "r")?.statSize
                    if (videoSize != null && videoSize > (10 * 1024 * 1024)) {
                        Toast.makeText(this, "You are trying to import a heavier video than 10 Mb, please import a lighter video", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val storage = Firebase.storage
                        val storageRef = storage.reference.child("Videos/${selectedVideoUri?.lastPathSegment}")
                        val uploadTask = storageRef.putFile(selectedVideoUri)
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

    private fun lightImage(image : Bitmap) : Bitmap {
        val width = image.width
        val height = image.height
        if(width <= 1280 && height <= 720) {
            return image
        }
        val ratio : Float
        if(width > height) {
            ratio = 1280.toFloat() / width
        }
        else {
            ratio = 720.toFloat() / height
        }
        val sizedWidth : Int = (width * ratio).toInt()
        val sizedHeight : Int = (height * ratio).toInt()
        return Bitmap.createScaledBitmap(image, sizedWidth, sizedHeight, true)
    }
}



