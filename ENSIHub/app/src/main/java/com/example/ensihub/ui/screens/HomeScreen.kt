package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.ui.screens.BottomBarScreen.Home.BottomNavigationBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavHost(navController, startDestination = BottomBarScreen.Home.route) {
            composable(BottomBarScreen.Home.route) {
                Column {
                    Text(text = "Home Screen")
                }
            }
            composable(BottomBarScreen.Profile.route) {
                Column {
                    Text(text = "Profile Screen")
                }
            }
            composable(BottomBarScreen.Settings.route) {
                Column {
                    Text(text = "Settings Screen")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    // Create a dummy NavHostController for preview
    val navController = rememberNavController()

    HomeScreen(navController = navController)
}
