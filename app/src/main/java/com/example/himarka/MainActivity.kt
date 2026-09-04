package com.example.himarka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.himarka.core.localization.LanguageManager
import com.example.himarka.core.navigation.HimarkaBottomBar
import com.example.himarka.core.navigation.HimarkaNavHost
import com.example.himarka.core.navigation.HimarkaTopBar
import com.example.himarka.core.navigation.Screen
import com.example.himarka.core.theme.HimarkaTheme
import com.example.himarka.data.repository.HimarkaRepositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HimarkaApp()
        }
    }
}

@Composable
fun HimarkaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val repository = HimarkaRepositoryImpl.instance
    val telemetry by repository.telemetryFlow.collectAsState()
    val currentLanguage by repository.languageFlow.collectAsState()

    val context = LocalContext.current
    val localizedContext = remember(currentLanguage) {
        LanguageManager.setLocale(context, currentLanguage)
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedContext.resources.configuration,
    ) {
        HimarkaTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    HimarkaTopBar(
                        isDemoMode = telemetry.isDemoMode,
                        onSettingsClick = {
                            navController.navigate(Screen.Settings.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                },
                bottomBar = {
                    HimarkaBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            ) { innerPadding ->
                HimarkaNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
