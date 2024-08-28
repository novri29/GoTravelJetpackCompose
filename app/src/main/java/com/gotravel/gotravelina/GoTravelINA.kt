package com.gotravel.gotravelina

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gotravel.gotravelina.navigation.NavigationItem
import com.gotravel.gotravelina.navigation.Screen
import com.gotravel.gotravelina.ui.screen.About
import com.gotravel.gotravelina.ui.screen.Detail
import com.gotravel.gotravelina.ui.screen.Favorite
import com.gotravel.gotravelina.ui.screen.Home
import androidx.compose.ui.res.stringResource as stringResource

@Composable
fun GoTravelINA(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold (
        bottomBar = {
            if (currentRoute != Screen.DetailDestination.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                Home(
                    navigateToDetail = { destinationId ->
                        navController.navigate(Screen.DetailDestination.createRoute(destinationId))
                    }
                )
            }
            composable(Screen.Favorite.route) {
                Favorite(
                    navigateToDetail = { destinationId ->
                        navController.navigate(Screen.DetailDestination.createRoute(destinationId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                About()
            }
            composable(
                route = Screen.DetailDestination.route,
                arguments = listOf(
                    navArgument("destinationId") { type = NavType.IntType }
                )
            ) {
                val id = it.arguments?.getInt("destinationId") ?: -1
                Detail(
                    destinationId = id,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.home_menu),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.favorite_menu),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.profile_menu),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile,
            ),
        )
        BottomNavigation {
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

