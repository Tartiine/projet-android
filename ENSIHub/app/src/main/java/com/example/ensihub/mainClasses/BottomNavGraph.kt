package com.example.ensihub.mainClasses

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
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


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavGraph(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route
        ) {
            composable(route = BottomBarScreen.Home.route) {
                HomeScreen()
            }
            composable(route = BottomBarScreen.Profile.route) {
                // Replace with your actual ProfileScreen here
                UserProfileScreen(user)
            }
            composable(route = BottomBarScreen.Settings.route) {
                // Replace with your actual SettingsScreen here
                SettingsView(user)
            }
        }
    }
}
