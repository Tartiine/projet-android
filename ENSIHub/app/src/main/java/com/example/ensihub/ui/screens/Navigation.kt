package com.example.ensihub.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.MainClasses.LoginViewModel

enum class LoginRoutes{
    Signup,
    Signin
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
    loginViewModel: LoginViewModel
){
    NavHost(navController = navController, startDestination = LoginRoutes.Signin.name){
        composable(route = LoginRoutes.Signin.name){
            LoginScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name){
                    launchSingleTop = true
                    popUpTo(LoginRoutes.Signin.name){
                        inclusive = true
                    }
                }
            }) {
                navController.navigate(LoginRoutes.Signup.name){
                    launchSingleTop = true
                    popUpTo(LoginRoutes.Signin.name){
                        inclusive = true
                    }
                }
            }

        }

        composable(route = LoginRoutes.Signup.name) {
            SignUpScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name) {
                    popUpTo(LoginRoutes.Signup.name) {
                        inclusive = true
                    }
                }
            }) {
                navController.navigate(LoginRoutes.Signin.name) {
                    popUpTo(LoginRoutes.Signup.name) {
                    }
                }
            }
        }

        composable(route = HomeRoutes.Home.name){
            Home()
        }

    }
}