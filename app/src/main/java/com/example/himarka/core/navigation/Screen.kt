package com.example.himarka.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.himarka.R

sealed class Screen(
    val route: String,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Home : Screen("home", R.string.nav_home, Icons.Filled.Home)
    object Storage : Screen("storage", R.string.nav_storage, Icons.Filled.AcUnit)
    object Produce : Screen("produce", R.string.nav_produce, Icons.Filled.Agriculture)
    object Energy : Screen("energy", R.string.nav_energy, Icons.Filled.Bolt)
    object Alerts : Screen("alerts", R.string.nav_alerts, Icons.Filled.Notifications)
    object Settings : Screen("settings", R.string.nav_settings, Icons.Filled.Settings)

    companion object {
        val bottomNavItems = listOf(Home, Storage, Produce, Energy, Alerts)
    }
}
