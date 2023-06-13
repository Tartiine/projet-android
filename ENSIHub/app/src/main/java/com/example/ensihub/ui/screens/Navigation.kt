package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.LoginViewModel
import com.example.ensihub.mainClasses.Moderation
import com.example.ensihub.ui.screens.BottomBarScreen.Home.BottomNavigationBar
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.ensihub.mainClasses.Post
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.collectAsState
import com.example.ensihub.mainClasses.User

enum class LoginRoutes{
    SignUp,
    SignIn,
    ForgotPassword
}

enum class HomeRoutes{
    Home,
    Profile,
    Settings,
    Moderation
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    viewModel: FeedViewModel,
    moderation : Moderation
) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val user by loginViewModel.currentUser.collectAsState()

    Scaffold(
        bottomBar = {
            if (isLoggedIn) {
                BottomNavigationBar(navController = navController, user = user)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) BottomBarScreen.Home.route else LoginRoutes.SignIn.name,
            modifier = Modifier.padding(innerPadding)
        ) {
                composable(route = BottomBarScreen.Home.route) {
                    HomeScreen(viewModel = viewModel)
                }
                composable(route = BottomBarScreen.Profile.route) {
                    UserProfileScreen(user)
                }
                composable(route = BottomBarScreen.Settings.route) {
                    SettingsView(user, navController, loginViewModel)
                }
                composable(route = LoginRoutes.SignIn.name) {
                    LoginScreen(
                        onNavToHomePage = { navController.navigate(BottomBarScreen.Home.route) },
                        loginViewModel = loginViewModel,
                        onNavToSignUpPage = { navController.navigate(LoginRoutes.SignUp.name) },
                        onNavToForgotPasswordPage = { navController.navigate(LoginRoutes.ForgotPassword.name) }
                    )
                }
                composable(route = BottomBarScreen.Moderation.route) {
                    ModerationScreen(moderationViewModel = moderation)
                }

                composable(route = LoginRoutes.SignUp.name) {
                    SignUpScreen(
                        onNavToHomePage = { navController.navigate(BottomBarScreen.Home.route) },
                        loginViewModel = loginViewModel,
                        onNavToLoginPage = { navController.navigate(LoginRoutes.SignIn.name) }
                    )
                }

                composable(route = LoginRoutes.ForgotPassword.name) {
                    ForgotPasswordScreen(
                        onNavToLoginPage = { navController.navigate(LoginRoutes.SignIn.name) },
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }



