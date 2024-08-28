package com.gotravel.gotravelina.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Favorite : Screen("favorite")
    data object Profile : Screen("profile")
    data object DetailDestination : Screen("home/{destinationId}") {
        fun createRoute(destinationId: Int) = "home/$destinationId"
    }
}