package com.fiap.autria.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiap.autria.ui.screens.home.HomeScreen
import com.fiap.autria.ui.screens.ia.IaScreen
import com.fiap.autria.ui.screens.settings.SettingScreen

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val IA = "ia"
}

@Composable
fun AppNavigation(
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME

    ) {

        composable(Routes.HOME) {
            HomeScreen(
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                },
                onIaClick = {
                    navController.navigate(Routes.IA)
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onToggleTheme = onToggleTheme
            )
        }
        composable(Routes.IA) {
            IaScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}