package com.example.ensihub.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.mainClasses.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

enum class LoginRoutes{
    SignUp,
    SignIn
}

enum class HomeRoutes{
    Home,
    Profile,
    Settings
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) HomeRoutes.Home.name else LoginRoutes.SignIn.name
    ) {
        composable(route = LoginRoutes.SignIn.name) {
            if (isLoggedIn) {
                LoginScreen(
                    onNavToHomePage = {
                        navController.navigate(HomeRoutes.Home.name) {
                            launchSingleTop = true
                            popUpTo(route = LoginRoutes.SignIn.name) {
                                inclusive = true
                            }
                        }
                    },
                    loginViewModel = loginViewModel
                ) {
                    navController.navigate(LoginRoutes.SignUp.name) {
                        launchSingleTop = true
                        popUpTo(LoginRoutes.SignIn.name) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        composable(route = LoginRoutes.SignUp.name) {
            if (isLoggedIn) {
                SignUpScreen(
                    onNavToHomePage = {
                        navController.navigate(HomeRoutes.Home.name) {
                            popUpTo(LoginRoutes.SignUp.name) {
                                inclusive = true
                            }
                        }
                    },
                    loginViewModel = loginViewModel
                ) {
                    navController.navigate(LoginRoutes.SignIn.name)
                }
            }
        }

        composable(route = HomeRoutes.Home.name) {
            if (true) {
                HomeScreen()
            } else {
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }
    }

}
