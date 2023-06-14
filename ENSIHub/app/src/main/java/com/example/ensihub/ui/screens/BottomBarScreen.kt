package com.example.ensihub.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ensihub.mainClasses.FeedViewModel
import com.example.ensihub.mainClasses.Role
import com.example.ensihub.mainClasses.User
/**
 * @file
 * @brief Fichier définissant les écrans de la barre de navigation inférieure.
 */

/**
 * @brief Classe scellée représentant un écran de la barre de navigation inférieure.
 *
 * @param route Route de l'écran.
 * @param title Titre de l'écran.
 * @param icon Vecteur d'icône de l'écran.
 * @param user Utilisateur associé à l'écran (facultatif).
 */
sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val user : User? = null
) {
    /**
     * @brief Écran "Home" de la barre de navigation inférieure.
     */
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    /**
     * @brief Écran "Profile" de la barre de navigation inférieure.
     */
    object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    /**
     * @brief Écran "Settings" de la barre de navigation inférieure.
     */
    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
    /**
     * @brief Écran "Moderation" de la barre de navigation inférieure.
     */
    object Moderation : BottomBarScreen(
        route = "moderation",
        title = "Manage",
        icon = Icons.Default.Search
    )

    /**
     * @brief Composant de la barre de navigation inférieure.
     *
     * @param navController Contrôleur de navigation.
     * @param viewModel ViewModel du flux.
     */
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
                listOf(Home, Profile, Settings, Moderation)
            } else {
                listOf(Home, Profile, Settings)
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
                            tint = Color.White
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
