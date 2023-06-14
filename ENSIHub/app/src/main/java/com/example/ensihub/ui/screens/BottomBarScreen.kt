package com.example.ensihub.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Role
import com.example.ensihub.mainClasses.User
import com.example.ensihub.ui.screens.BottomBarScreen.Home.BottomNavigationBar

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val user : User? = null
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )

    object Moderation : BottomBarScreen(
        route = "moderation",
        title = "Manage",
        icon = Icons.Default.Search
    )

    @Composable
    fun BottomNavigationBar(navController: NavHostController, viewModel: FeedViewModel) {
        val currentUser = viewModel.currentUser.collectAsState().value
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(
                alpha = 255,
                red = 247,
                green = 152,
                blue = 23
            )
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val items = if (currentUser?.role == Role.MODERATOR || currentUser?.role == Role.ADMIN) {
                listOf(BottomBarScreen.Home, BottomBarScreen.Profile, BottomBarScreen.Settings, BottomBarScreen.Moderation)
            } else {
                listOf(BottomBarScreen.Home, BottomBarScreen.Profile, BottomBarScreen.Settings)
            }

            items.forEach { screen ->
                BottomNavigationItem(
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = if (currentRoute == screen.route) {
                                Color.White
                            } else {
                                Color.White
                            }
                        )
                    },
                    label = {
                        Text(
                            screen.title
                        )
                    }
                )
            }
        }
        println("Current user: $currentUser, role: ${currentUser?.role}")
    }
}
