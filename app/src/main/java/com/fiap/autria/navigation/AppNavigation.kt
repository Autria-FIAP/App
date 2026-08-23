package com.fiap.autria.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiap.autria.ui.screens.home.HomeScreen
import com.fiap.autria.ui.screens.ia.IaScreen
import com.fiap.autria.ui.screens.settings.SettingScreen
import com.fiap.autria.ui.screens.about.AboutScreen
import com.fiap.autria.ui.theme.AutriaTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val IA = "ia"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isDarkTheme by remember { mutableStateOf(false) }

    AutriaTheme(darkTheme = isDarkTheme) {
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
                    onToggleTheme = {
                        isDarkTheme = !isDarkTheme
                    },
                    onAboutClick = {
                        navController.navigate(Routes.ABOUT)
                    }
                )
            }
            composable(Routes.ABOUT) {
                AboutScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
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
}