package com.example.ensihub.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomBarScreen.Home.BottomNavigationBar(navController = navController) }
    ) {
        // Content of your screen
        Column {
            Text(text = "Home Screen")
            // Add more UI components as needed
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
