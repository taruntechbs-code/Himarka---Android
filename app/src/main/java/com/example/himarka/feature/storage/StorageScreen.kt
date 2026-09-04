package com.example.himarka.feature.storage

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.himarka.R
import com.example.himarka.core.common.ui.ConfirmationDialog
import com.example.himarka.core.common.ui.HimarkaCard
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaAmber
import com.example.himarka.core.theme.HimarkaCardBackground
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaShapes
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet
import com.example.himarka.data.model.StoragePreset

@Composable
fun StorageScreen(
    viewModel: StorageViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var pendingPresetToConfirm by remember { mutableStateOf<StoragePreset?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Current Active Storage Overview
        Text(
            text = stringResource(id = R.string.storage_current_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = HimarkaTextMain
        )
        Spacer(modifier = Modifier.height(10.dp))

        HimarkaCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 1.dp,
            contentPadding = 16.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = uiState.activePreset.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HimarkaTextMain
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            id = R.string.storage_target_prefix,
                            stringResource(id = uiState.activePreset.rangeResId)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = HimarkaViolet
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(id = uiState.activePreset.descResId),
                        style = MaterialTheme.typography.bodySmall,
                        color = HimarkaTextMuted
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                StatusChip(
                    text = stringResource(id = R.string.status_active),
                    level = StatusLevel.OPTIMAL
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = HimarkaCardBorder)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.storage_produce_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = HimarkaTextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.storedCrops.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.produce_no_crops),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaTextMuted
                )
            } else {
                uiState.storedCrops.forEach { crop ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${crop.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = HimarkaTextMain
                        )
                        Text(
                            text = "Ref: ${crop.scientificReferenceTemp}",
                            style = MaterialTheme.typography.bodySmall,
                            color = HimarkaTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val isCompatible = uiState.compatibility?.isCompatible ?: true
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.storage_crop_compatibility),
                    style = MaterialTheme.typography.labelMedium,
                    color = HimarkaTextMuted
                )
                if (uiState.storedCrops.isNotEmpty()) {
                    Text(
                        text = if (isCompatible) {
                            "✓ " + stringResource(id = R.string.produce_compatible)
                        } else {
                            "⚠ " + stringResource(id = R.string.produce_conflict)
                        },
                        color = if (isCompatible) HimarkaEmerald else HimarkaAmber,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Other Storage Modes Section
        val otherPresets = uiState.allPresets.filter { it != uiState.activePreset }
        if (otherPresets.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.storage_other_modes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(10.dp))

            otherPresets.forEach { preset ->
                HimarkaCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    backgroundColor = HimarkaCardBackground,
                    borderColor = HimarkaCardBorder,
                    borderWidth = 1.dp,
                    elevation = 0.dp,
                    contentPadding = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(id = preset.titleResId),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HimarkaTextMain
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(
                                    id = R.string.storage_target_prefix,
                                    stringResource(id = preset.rangeResId)
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = HimarkaViolet,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { pendingPresetToConfirm = preset },
                            colors = ButtonDefaults.buttonColors(containerColor = HimarkaViolet),
                            shape = HimarkaShapes.small
                        ) {
                            Text(
                                text = stringResource(id = R.string.storage_select_mode),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = preset.descResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HimarkaTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Hardware Safety & Command Boundary Notice Card
        HimarkaCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
            contentPadding = 16.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = HimarkaViolet,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.storage_safety_notice),
                        style = MaterialTheme.typography.bodySmall,
                        color = HimarkaTextMuted
                    )
                }
            }
        }

        // Generous bottom clearance ensuring the final card is never obscured by the bottom navigation bar
        Spacer(modifier = Modifier.height(36.dp))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }

    // Farmer Confirmation Dialog before submitting mode change command intent
    pendingPresetToConfirm?.let { preset ->
        ConfirmationDialog(
            title = stringResource(id = R.string.action_request_mode, preset.formatTempRange()),
            message = stringResource(
                id = R.string.produce_conflict_warn,
                stringResource(id = preset.titleResId),
                preset.formatTempRange(),
                stringResource(id = uiState.activePreset.titleResId),
                uiState.activePreset.formatTempRange()
            ),
            onConfirm = {
                viewModel.requestModeChange(preset)
                pendingPresetToConfirm = null
            },
            onDismiss = {
                pendingPresetToConfirm = null
            }
        )
    }
}
