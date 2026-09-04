package com.example.himarka.feature.energy

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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.himarka.R
import com.example.himarka.core.common.ui.HimarkaCard
import com.example.himarka.core.common.ui.StatMetric
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaAmber
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaSky
import com.example.himarka.core.theme.HimarkaTextMain
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
        Spacer(modifier = Modifier.height(16.dp))
        
        // Energy Overview Header Card
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 2.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.energy_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = HimarkaTextMain
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.energy_status_good),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                StatusChip(
                    text = stringResource(id = R.string.energy_solar_active),
                    level = StatusLevel.OPTIMAL
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid 1: Battery Level & Solar Generation
        Row(modifier = Modifier.fillMaxWidth()) {
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 1.dp) {
                StatMetric(
                    label = stringResource(id = R.string.energy_battery_level),
                    value = "${t.batteryPercent}%",
                    unit = "${String.format(Locale.getDefault(), "%.1f", t.batteryVoltageV)} V",
                    icon = Icons.Default.BatteryChargingFull,
                    iconTint = HimarkaEmerald
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 1.dp) {
                StatMetric(
                    label = stringResource(id = R.string.energy_solar_gen),
                    value = "${t.solarGenerationW.toInt()} W",
                    unit = "${String.format(Locale.getDefault(), "%.1f", t.solarVoltageV)} V",
                    icon = Icons.Default.SolarPower,
                    iconTint = HimarkaAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid 2: Cooling Power & DC Bus Status
        Row(modifier = Modifier.fillMaxWidth()) {
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 1.dp) {
                StatMetric(
                    label = stringResource(id = R.string.energy_cooling_power),
                    value = "${t.coolingPowerW} W",
                    unit = if (t.isCoolingActive) stringResource(id = R.string.status_cooling_active) else stringResource(id = R.string.status_cooling_idle),
                    icon = Icons.Default.Power,
                    iconTint = HimarkaSky
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 1.dp) {
                StatMetric(
                    label = stringResource(id = R.string.energy_dc_bus),
                    value = "24 V DC",
                    unit = stringResource(id = R.string.energy_nominal),
                    icon = Icons.Default.Bolt,
                    iconTint = HimarkaEmerald
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
