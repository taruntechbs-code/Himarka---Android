package com.example.himarka.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.himarka.R
import com.example.himarka.core.common.ui.HimarkaCard
import com.example.himarka.core.common.ui.HimarkaIconContainer
import com.example.himarka.core.common.ui.StatMetric
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaAmber
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaSky
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet
import com.example.himarka.data.model.AlertSeverity
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
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

        // 1. STORAGE HEALTH HERO
        HimarkaCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            contentPadding = 18.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.dashboard_health_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaTextMuted
                )
                StatusChip(
                    text = when (uiState.healthLevel) {
                        StatusLevel.OPTIMAL -> stringResource(id = R.string.status_healthy)
                        StatusLevel.WARNING -> stringResource(id = R.string.status_warning)
                        StatusLevel.CRITICAL -> stringResource(id = R.string.status_critical)
                        else -> stringResource(id = R.string.status_healthy)
                    },
                    level = uiState.healthLevel
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = uiState.healthMsgResId, uiState.currentTemp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(id = R.string.storage_target_prefix, uiState.activePreset.formatTempRange()),
                style = MaterialTheme.typography.bodyMedium,
                color = HimarkaTextMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. POWER AVAILABILITY SUMMARY
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HimarkaIconContainer(
                        icon = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = HimarkaEmerald,
                        size = 32.dp,
                        iconSize = 18.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.dashboard_power_summary),
                            style = MaterialTheme.typography.bodyMedium,
                            color = HimarkaTextMuted
                        )
                        Text(
                            text = "${uiState.telemetry.batteryPercent}% Battery",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HimarkaTextMain,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                StatusChip(
                    text = if (uiState.telemetry.isCoolingActive) stringResource(id = R.string.dashboard_active) else stringResource(id = R.string.dashboard_idle),
                    level = if (uiState.telemetry.isCoolingActive) StatusLevel.OPTIMAL else StatusLevel.NEUTRAL
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. TEMPERATURE + HUMIDITY (2-Column Grid)
        Row(modifier = Modifier.fillMaxWidth()) {
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 0.dp) {
                StatMetric(
                    label = stringResource(id = R.string.dashboard_temp_label),
                    value = String.format(Locale.getDefault(), "%.1f", uiState.telemetry.temperatureC),
                    unit = "°C",
                    icon = Icons.Default.DeviceThermostat,
                    iconTint = HimarkaSky
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 0.dp) {
                StatMetric(
                    label = stringResource(id = R.string.dashboard_humidity_label),
                    value = String.format(Locale.getDefault(), "%.0f", uiState.telemetry.humidityPercent),
                    unit = "% RH",
                    icon = Icons.Default.WaterDrop,
                    iconTint = HimarkaEmerald
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. STORAGE / PRODUCE
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 0.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HimarkaIconContainer(
                        icon = Icons.Default.AcUnit,
                        contentDescription = null,
                        tint = HimarkaViolet,
                        size = 32.dp,
                        iconSize = 18.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.dashboard_active_mode),
                            style = MaterialTheme.typography.bodyMedium,
                            color = HimarkaTextMuted
                        )
                        Text(
                            text = stringResource(id = uiState.activePreset.titleResId).split("—").firstOrNull()?.trim() ?: "Mode 1",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HimarkaTextMain
                        )
                    }
                }
                Text(
                    text = uiState.activePreset.formatTempRange(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = HimarkaViolet
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(12.dp))

            val cropCount = uiState.storedCrops.size
            val storedTitle = if (cropCount <= 1) {
                stringResource(id = R.string.dashboard_stored_crop)
            } else {
                stringResource(id = R.string.dashboard_stored_produce_count, cropCount)
            }
            val storedDesc = when (cropCount) {
                0 -> "None"
                1 -> uiState.storedCrops.first().name
                else -> uiState.storedCrops.take(2).joinToString(", ") { it.name }
            }
            val refDesc = when (cropCount) {
                0 -> ""
                1 -> "Ref: ${uiState.storedCrops.first().scientificReferenceTemp}"
                else -> if (cropCount > 2) "+${cropCount - 2} more" else ""
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    HimarkaIconContainer(
                        icon = Icons.Default.Agriculture,
                        contentDescription = null,
                        tint = HimarkaViolet,
                        size = 32.dp,
                        iconSize = 18.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = storedTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = HimarkaTextMuted
                        )
                        Text(
                            text = storedDesc,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HimarkaTextMain,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (refDesc.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = refDesc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = HimarkaTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 5. PRODUCE CONDITION & COOLING SYSTEM (2-Column Grid)
        Row(modifier = Modifier.fillMaxWidth()) {
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 0.dp) {
                StatMetric(
                    label = stringResource(id = R.string.dashboard_gas_label),
                    value = stringResource(id = R.string.dashboard_gas_fresh),
                    unit = null,
                    icon = Icons.Default.Air,
                    iconTint = HimarkaEmerald
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            HimarkaCard(modifier = Modifier.weight(1f), elevation = 0.dp) {
                StatMetric(
                    label = stringResource(id = R.string.dashboard_cooling_system),
                    value = if (uiState.telemetry.isCoolingActive) stringResource(id = R.string.dashboard_active) else stringResource(id = R.string.dashboard_idle),
                    unit = "${uiState.telemetry.coolingPowerW} W",
                    icon = Icons.Default.Power,
                    iconTint = HimarkaSky
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        // 6. SOLAR STATUS
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 0.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HimarkaIconContainer(
                        icon = Icons.Default.SolarPower,
                        contentDescription = null,
                        tint = HimarkaAmber,
                        size = 32.dp,
                        iconSize = 18.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.dashboard_solar_status),
                            style = MaterialTheme.typography.bodyMedium,
                            color = HimarkaTextMuted
                        )
                        Text(
                            text = if (uiState.telemetry.solarGenerationW > 0) stringResource(id = R.string.dashboard_generating) else stringResource(id = R.string.dashboard_idle),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HimarkaTextMain,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Text(
                    text = "${uiState.telemetry.solarGenerationW.toInt()} W",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = HimarkaAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 7. ACTIVE ALERTS
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 0.dp) {
            Text(
                text = stringResource(id = R.string.dashboard_quick_alerts),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.activeAlerts.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.dashboard_no_alerts),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaTextMuted
                )
            } else {
                val primaryAlert = uiState.activeAlerts.first()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = primaryAlert.title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        Text(text = primaryAlert.message, style = MaterialTheme.typography.bodyMedium, color = HimarkaTextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusChip(
                        text = primaryAlert.severity.name,
                        level = when (primaryAlert.severity) {
                            AlertSeverity.CRITICAL -> StatusLevel.CRITICAL
                            AlertSeverity.WARNING -> StatusLevel.WARNING
                            else -> StatusLevel.NEUTRAL
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
