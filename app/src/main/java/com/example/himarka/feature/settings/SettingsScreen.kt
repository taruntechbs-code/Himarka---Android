package com.example.himarka.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.himarka.R
import com.example.himarka.core.common.ui.HimarkaCard
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaShapes
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 1. Language Selection Card
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = HimarkaViolet,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.settings_language_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HimarkaTextMain
                    )
                    Text(
                        text = stringResource(
                            id = R.string.settings_current_language_prefix,
                            uiState.currentLanguage.displayName
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = HimarkaTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = HimarkaCardBorder)

            uiState.allLanguages.forEachIndexed { index, lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clip(HimarkaShapes.small)
                        .clickable { viewModel.setLanguage(lang) }
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${lang.displayName} (${lang.nativeName})",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (lang == uiState.currentLanguage) HimarkaViolet else HimarkaTextMain,
                        fontWeight = if (lang == uiState.currentLanguage) FontWeight.SemiBold else FontWeight.Normal
                    )
                    RadioButton(
                        selected = lang == uiState.currentLanguage,
                        onClick = { viewModel.setLanguage(lang) },
                        colors = RadioButtonDefaults.colors(selectedColor = HimarkaViolet)
                    )
                }
                if (index < uiState.allLanguages.lastIndex) {
                    HorizontalDivider(color = HimarkaCardBorder.copy(alpha = 0.5f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Demo Mode Toggle Card
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.settings_demo_mode),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HimarkaTextMain
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.settings_demo_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HimarkaTextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            id = R.string.settings_data_source_prefix,
                            if (uiState.telemetry.isDemoMode) {
                                stringResource(id = R.string.settings_status_demo)
                            } else {
                                "Live Hardware"
                            }
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = HimarkaViolet
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Switch(
                    checked = uiState.telemetry.isDemoMode,
                    onCheckedChange = { viewModel.setDemoMode(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = HimarkaViolet)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. About HIMARKA Card
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Text(
                text = stringResource(id = R.string.settings_about),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(id = R.string.settings_about_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = HimarkaTextMuted
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
