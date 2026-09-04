package com.example.himarka.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.himarka.R
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaCardBackground
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HimarkaTopBar(
    isDemoMode: Boolean,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "HIMARKA",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = HimarkaViolet
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                StatusChip(
                    text = stringResource(id = if (isDemoMode) R.string.status_demo else R.string.status_live),
                    level = if (isDemoMode) StatusLevel.DEMO else StatusLevel.OPTIMAL
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.nav_settings),
                    tint = HimarkaTextMain
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HimarkaCardBackground
        )
    )
}
