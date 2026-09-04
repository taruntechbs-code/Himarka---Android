package com.example.himarka.feature.energy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.himarka.R
import com.example.himarka.core.common.ui.HimarkaCard
import com.example.himarka.core.theme.HimarkaAmber
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaSky
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import java.util.Locale

@Composable
fun EnergyScreen(
    viewModel: EnergyViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val t = uiState.telemetry

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 1. Energy Overview Header Card (Clean, no redundant pill badge)
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.energy_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = HimarkaTextMain
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.energy_status_good),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaTextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Power Overview Section
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Text(
                text = stringResource(id = R.string.energy_power_overview),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(8.dp))

            EnergyMetricRow(
                icon = Icons.Default.BatteryChargingFull,
                iconTint = HimarkaEmerald,
                label = stringResource(id = R.string.energy_battery_level),
                value = "${t.batteryPercent}%"
            )

            HorizontalDivider(color = HimarkaCardBorder)

            EnergyMetricRow(
                icon = Icons.Default.SolarPower,
                iconTint = HimarkaAmber,
                label = stringResource(id = R.string.energy_solar_gen),
                value = "${t.solarGenerationW.toInt()} W"
            )

            HorizontalDivider(color = HimarkaCardBorder)

            EnergyMetricRow(
                icon = Icons.Default.Power,
                iconTint = HimarkaSky,
                label = stringResource(id = R.string.energy_cooling_power),
                value = "${String.format(Locale.getDefault(), "%.1f", t.coolingPowerW)} W",
                subValue = if (t.isCoolingActive) {
                    stringResource(id = R.string.status_cooling_active)
                } else {
                    stringResource(id = R.string.status_cooling_idle)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Technical Details Section
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Text(
                text = stringResource(id = R.string.energy_tech_details),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(8.dp))

            EnergyMetricRow(
                icon = Icons.Default.BatteryChargingFull,
                iconTint = HimarkaTextMuted,
                label = stringResource(id = R.string.energy_battery_volt),
                value = "${String.format(Locale.getDefault(), "%.1f", t.batteryVoltageV)} V"
            )

            HorizontalDivider(color = HimarkaCardBorder)

            EnergyMetricRow(
                icon = Icons.Default.SolarPower,
                iconTint = HimarkaTextMuted,
                label = stringResource(id = R.string.energy_solar_volt),
                value = "${String.format(Locale.getDefault(), "%.1f", t.solarVoltageV)} V"
            )

            HorizontalDivider(color = HimarkaCardBorder)

            EnergyMetricRow(
                icon = Icons.Default.Bolt,
                iconTint = HimarkaTextMuted,
                label = stringResource(id = R.string.energy_dc_bus),
                value = "24 V DC",
                subValue = stringResource(id = R.string.energy_nominal)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun EnergyMetricRow(
    icon: ImageVector? = null,
    iconTint: Color = HimarkaTextMuted,
    label: String,
    value: String,
    subValue: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = HimarkaTextMain
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            if (subValue != null) {
                Text(
                    text = subValue,
                    style = MaterialTheme.typography.bodySmall,
                    color = HimarkaTextMuted
                )
            }
        }
    }
}
