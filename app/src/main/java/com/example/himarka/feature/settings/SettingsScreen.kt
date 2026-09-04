package com.example.himarka.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import com.example.himarka.core.common.ui.HimarkaIconContainer
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
        Spacer(modifier = Modifier.height(16.dp))
        
        // Language Selection Card
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HimarkaIconContainer(
                    icon = Icons.Default.Language,
                    contentDescription = null,
                    size = 32.dp,
                    iconSize = 18.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.settings_language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HimarkaTextMain
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            uiState.allLanguages.forEach { lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(HimarkaShapes.small)
                        .clickable { viewModel.setLanguage(lang) }
                        .padding(vertical = 6.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${lang.displayName} (${lang.nativeName})",
                        style = MaterialTheme.typography.bodyLarge,
                        color = HimarkaTextMain
                    )
                    RadioButton(
                        selected = lang == uiState.currentLanguage,
                        onClick = { viewModel.setLanguage(lang) },
                        colors = RadioButtonDefaults.colors(selectedColor = HimarkaViolet)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Demo Mode Toggle Card
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

        // Technical Hardware & Sensor Diagnostics
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HimarkaIconContainer(
                    icon = Icons.Default.Memory,
                    contentDescription = null,
                    size = 32.dp,
                    iconSize = 18.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.settings_technical_info),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HimarkaTextMain
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(12.dp))

            val isDemo = uiState.telemetry.isDemoMode
            val demoText = stringResource(id = R.string.settings_status_demo)
            val notCalibratedText = stringResource(id = R.string.settings_status_not_calibrated)
            val notConfiguredText = stringResource(id = R.string.settings_status_not_configured)

            TechItem(label = stringResource(id = R.string.settings_esp32_status), value = demoText)
            TechItem(label = stringResource(id = R.string.settings_sht31_status), value = demoText)
            TechItem(
                label = stringResource(id = R.string.settings_mq135_status),
                value = if (isDemo) "$demoText (${uiState.telemetry.gasPpm.toInt()} PPM)" else notCalibratedText
            )
            TechItem(label = stringResource(id = R.string.settings_ov2640_status), value = notConfiguredText)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About HIMARKA Card
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
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TechItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label, 
            style = MaterialTheme.typography.bodyMedium, 
            color = HimarkaTextMuted,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        StatusChip(text = value, level = StatusLevel.NEUTRAL)
    }
}
