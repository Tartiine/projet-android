package com.example.ensihub.mainClasses

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ensihub.ui.screens.BottomBarScreen
import com.example.ensihub.ui.screens.BottomBarScreen.Home.BottomNavigationBar
import com.example.ensihub.ui.screens.HomeScreen
import com.example.ensihub.ui.screens.SettingsView
import com.example.ensihub.ui.screens.UserProfileScreen
import com.example.ensihub.ui.screens.user

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            UserProfileScreen(user)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsView(user)
        }
    }

    // Call the BottomNavigationBar function from BottomBarScreen
    BottomNavigationBar(navController = navController)
}
