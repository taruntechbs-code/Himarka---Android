package com.example.himarka.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.himarka.R
import com.example.himarka.core.common.ui.HimarkaCard
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaAmber
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet
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
        Spacer(modifier = Modifier.height(20.dp))

        // ==========================================
        // 1. STORAGE HEALTH HERO (DOMINANT SECTION)
        // "Is my storage okay?"
        // ==========================================
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
                    style = MaterialTheme.typography.labelMedium,
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
                text = stringResource(id = uiState.healthHeadlineResId),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${stringResource(id = uiState.healthCoolingNoteResId)} • Target: ${uiState.activePreset.formatTempRange()}",
                style = MaterialTheme.typography.bodyMedium,
                color = HimarkaTextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // 2. UNIFIED STORAGE OVERVIEW
        // Temperature, Humidity, Produce, Power
        // Consolidated into one calm, harmonious card
        // ==========================================
        HimarkaCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 1.dp,
            contentPadding = 18.dp
        ) {
            // --- Temperature & Humidity ---
            Text(
                text = stringResource(id = R.string.dashboard_temp_label),
                style = MaterialTheme.typography.labelMedium,
                color = HimarkaTextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "%.1f°C", uiState.telemetry.temperatureC),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                    fontWeight = FontWeight.Bold,
                    color = HimarkaTextMain
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Target: ${uiState.activePreset.formatTempRange()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaViolet,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(
                    id = R.string.home_humidity_line,
                    uiState.telemetry.humidityPercent.toInt()
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = HimarkaTextMuted
            )

            // --- Divider ---
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(14.dp))

            // --- Stored Produce ---
            Text(
                text = stringResource(id = R.string.dashboard_stored_crop),
                style = MaterialTheme.typography.labelMedium,
                color = HimarkaTextMuted
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (uiState.storedCrops.isEmpty()) {
                    stringResource(id = R.string.dashboard_no_produce)
                } else {
                    uiState.formattedProduceSummary
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (uiState.storedCrops.isEmpty()) HimarkaTextMuted else HimarkaTextMain
            )

            if (uiState.isProduceConflicting && uiState.produceCompatibilityTagResId != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠ ${stringResource(id = uiState.produceCompatibilityTagResId!!)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = HimarkaAmber
                )
            }

            // --- Divider ---
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(14.dp))

            // --- Power ---
            Text(
                text = stringResource(id = R.string.nav_energy),
                style = MaterialTheme.typography.labelMedium,
                color = HimarkaTextMuted
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(
                    id = R.string.power_home_summary,
                    uiState.telemetry.batteryPercent,
                    if (uiState.telemetry.solarGenerationW > 0) {
                        stringResource(id = R.string.power_solar_available)
                    } else {
                        stringResource(id = R.string.power_solar_idle)
                    }
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = HimarkaTextMain
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ==========================================
        // 3. ATTENTION / TASKS
        // "Do I need to do anything?"
        // Only prominent when action is required
        // ==========================================
        val hasAction = uiState.actionMessageResId != null || uiState.actionMessageCustom != null
        val actionText = when {
            uiState.actionMessageResId != null -> stringResource(id = uiState.actionMessageResId!!)
            uiState.actionMessageCustom != null -> uiState.actionMessageCustom!!
            else -> null
        }

        if (hasAction) {
            HimarkaCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 1.dp,
                contentPadding = 18.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚠",
                        color = HimarkaAmber,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.tasks_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HimarkaTextMain
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = actionText ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = HimarkaTextMain
                )
            }
        } else {
            // Calm, single line when no action is needed — no card clutter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✓",
                    color = HimarkaEmerald,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.tasks_no_action_calm),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaTextMuted
                )
            }
        }

        // Guaranteed bottom clearance
        Spacer(modifier = Modifier.height(28.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
