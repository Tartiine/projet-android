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


class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            val navController = rememberNavController()

            ENSIHubTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation(navController = navController, loginViewModel = loginViewModel)
                }
            }
        }

        if(!permissionsCheck()) {
            requestPermissions()
        }
    }

    private fun permissionsCheck() : Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<String>, grantResults : IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {

            } else {
                //You will not be able to post photos on EnsiHub without these permissions.
                // Please check settings -> Applications -> Permissions, if you want to have the complete experience of EnsiHub.
            }
        }
    }
}

/* Test Post
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
*/


