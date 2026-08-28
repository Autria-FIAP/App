package com.fiap.autria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fiap.autria.navigation.AppNavigation
import com.fiap.autria.ui.theme.AutriaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            var isDarkTheme by remember {
                mutableStateOf(false)
            }

            AutriaTheme(
                darkTheme = isDarkTheme
            ) {

                AppNavigation(
                    onToggleTheme = {
                        isDarkTheme = !isDarkTheme
                    }
                )
            }
        }
    }
}