package com.example.himarka.core.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.himarka.core.theme.HimarkaCardBackground
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet

@Composable
fun HimarkaBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = HimarkaCardBackground,
        tonalElevation = 4.dp
    ) {
        Screen.bottomNavItems.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(id = screen.titleResId)
                    )
                },
                label = {
                    Text(text = stringResource(id = screen.titleResId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HimarkaViolet,
                    selectedTextColor = HimarkaViolet,
                    unselectedIconColor = HimarkaTextMuted,
                    unselectedTextColor = HimarkaTextMuted,
                    indicatorColor = HimarkaViolet.copy(alpha = 0.12f)
                )
            )
        }
    }
}
