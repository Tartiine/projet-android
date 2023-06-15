package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ensihub.mainClasses.CommentViewModel
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.LoginViewModel
import com.example.ensihub.mainClasses.Moderation
import com.example.ensihub.ui.screens.BottomBarScreen.Home.BottomNavigationBar

enum class LoginRoutes {
    SignUp,
    SignIn,
    ForgotPassword
}

/**
 * @brief Gestion de la navigation de l'application.
 *
 * @param navController Contrôleur de navigation.
 * @param loginViewModel ViewModel de connexion.
 * @param viewModel ViewModel du flux.
 * @param moderation Composant de modération.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    viewModel: FeedViewModel,
    moderation: Moderation
) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    Scaffold(
        bottomBar = {
            if (isLoggedIn) {
                BottomNavigationBar(navController = navController, viewModel = viewModel)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) BottomBarScreen.Home.route else LoginRoutes.SignIn.name,
            modifier = Modifier.padding(innerPadding)
        ) {
                composable(route = BottomBarScreen.Home.route) {
                    HomeScreen(viewModel = viewModel, navController = navController)
                }
                composable(route = BottomBarScreen.Profile.route) {
                    UserProfileScreen()
                }
                composable(route = BottomBarScreen.Settings.route) {
                    SettingsView(navHostController = navController, loginViewModel = loginViewModel)
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

                composable(route = "newPost") {
                    NewPostView(navController = navController)
                }

                composable(route = "settings/changeUsername"){
                    ChangeUsernameView(navHostController = navController)
                }
                composable(route = "settings/changePassword"){
                    ChangePasswordView(navHostController = navController)
                }
                composable(route = "settings/contact"){
                    ContactView(navHostController = navController)
                }
                composable(route = "settings/aboutUs"){
                AboutUsView(navHostController = navController)
                }
                composable(route = "postDetails/{postId}"){navBackStackEntry ->
                    PostDetailScreen(navBackStackEntry.arguments?.getString("postId") ?: "", viewModel = viewModel, commentViewModel = CommentViewModel(navBackStackEntry.arguments?.getString("postId") ?: "")
                    )
                }
            }
        }
    }



