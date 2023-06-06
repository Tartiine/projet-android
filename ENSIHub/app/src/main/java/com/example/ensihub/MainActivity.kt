package com.example.ensihub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ensihub.login.LoginViewModel
import com.example.ensihub.ui.screens.Navigation
import com.example.ensihub.ui.screens.PostView
import com.example.ensihub.ui.theme.ENSIHubTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a sample Post object
        val post = Post(
            id = "123",
            text = "Yo la street.",
            timestamp = System.currentTimeMillis(),
            author = "Marian chef eco-conception",
            likesCount = 5
        )

        // Call the PostView function to display the Post preview
        setContent {
            //PostView(post)
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            ENSIHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation(loginViewModel = loginViewModel)
                }
            }
        }
    }
}

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
