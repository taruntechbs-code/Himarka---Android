package com.example.himarka.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.himarka.feature.alerts.AlertsScreen
import com.example.himarka.feature.dashboard.DashboardScreen
import com.example.himarka.feature.energy.EnergyScreen
import com.example.himarka.feature.produce.ProduceScreen
import com.example.himarka.feature.settings.SettingsScreen
import com.example.himarka.feature.storage.StorageScreen

@Composable
fun HimarkaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            DashboardScreen()
        }
        composable(Screen.Storage.route) {
            StorageScreen()
        }
        composable(Screen.Produce.route) {
            ProduceScreen()
        }
        composable(Screen.Energy.route) {
            EnergyScreen()
        }
        composable(Screen.Alerts.route) {
            AlertsScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
