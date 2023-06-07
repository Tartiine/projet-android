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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ensihub.MainClasses.Feed
import com.example.ensihub.MainClasses.LoginViewModel
import com.example.ensihub.MainClasses.Post
import com.example.ensihub.ui.screens.MainFeed
import com.example.ensihub.ui.screens.Navigation
import com.example.ensihub.ui.screens.LoginScreen
import com.example.ensihub.ui.screens.PostView
import com.example.ensihub.ui.screens.SignUpScreen
import com.example.ensihub.ui.theme.ENSIHubTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val feed = Feed()

        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            ENSIHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var currentScreen by mutableStateOf("login")
                    when (currentScreen) {
                        "login" -> LoginScreen(
                            loginViewModel = loginViewModel,
                            onNavToHomePage = { currentScreen = "home" },
                            onNavToSignUpPage = { currentScreen = "signUp" }
                        )
                        "signUp" -> SignUpScreen(
                            loginViewModel = loginViewModel,
                            onNavToHomePage = { currentScreen = "home" },
                            onNavToLoginPage = { currentScreen = "login" }
                        )
                        "home" -> MyApp(feed = feed, loginViewModel = loginViewModel)
                    }
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
