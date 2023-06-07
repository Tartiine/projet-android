package com.example.ensihub

import android.annotation.SuppressLint
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
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.MainClasses.Feed
import com.example.ensihub.MainClasses.LoginViewModel
import com.example.ensihub.MainClasses.Post
import com.example.ensihub.ui.screens.MainFeed
import com.example.ensihub.ui.screens.Navigation
import com.example.ensihub.ui.screens.PostView
import com.example.ensihub.ui.theme.ENSIHubTheme


class MainActivity : ComponentActivity() {
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

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun MyApp(feed: Feed, loginViewModel: LoginViewModel) {
    MaterialTheme {
        MainFeed(feed)
        Navigation(loginViewModel = loginViewModel)
    }
}
