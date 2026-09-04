package com.example.himarka.feature.storage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.himarka.core.theme.HimarkaCardBorder
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
        
        // Current Storage Overview
        Text(
            text = stringResource(id = R.string.storage_current_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = HimarkaTextMain
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = stringResource(id = uiState.activePreset.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HimarkaTextMain
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.storage_target_prefix, stringResource(id = uiState.activePreset.rangeResId)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HimarkaTextMuted
                    )
                }
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
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "• ${crop.name}", style = MaterialTheme.typography.bodyLarge, color = HimarkaTextMain)
                        Text(text = "Ref: ${crop.scientificReferenceTemp}", style = MaterialTheme.typography.bodyMedium, color = HimarkaTextMuted)
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
                    style = MaterialTheme.typography.labelLarge,
                    color = HimarkaTextMuted
                )
                if (uiState.storedCrops.isNotEmpty()) {
                    StatusChip(
                        text = if (isCompatible) stringResource(id = R.string.produce_compatible) else stringResource(id = R.string.produce_conflict),
                        level = if (isCompatible) StatusLevel.OPTIMAL else StatusLevel.WARNING
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.storage_preset_header),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = HimarkaTextMain
        )
        Spacer(modifier = Modifier.height(12.dp))

        uiState.allPresets.forEach { preset ->
            val isCurrent = preset == uiState.activePreset
            val containerColor = if (isCurrent) HimarkaViolet.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
            val borderColor = if (isCurrent) HimarkaViolet else HimarkaCardBorder
            
            val animatedElevation by animateDpAsState(
                targetValue = if (isCurrent) 2.dp else 0.dp,
                animationSpec = tween(durationMillis = 300),
                label = "preset_card_elevation"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = HimarkaShapes.medium,
                colors = CardDefaults.cardColors(containerColor = containerColor),
                border = BorderStroke(if (isCurrent) 2.dp else 1.dp, borderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
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
                                text = stringResource(id = R.string.storage_target_prefix, stringResource(id = preset.rangeResId)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = HimarkaViolet,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        if (!isCurrent) {
                            Button(
                                onClick = { pendingPresetToConfirm = preset },
                                colors = ButtonDefaults.buttonColors(containerColor = HimarkaViolet),
                                shape = HimarkaShapes.small
                            ) {
                                Text(
                                    text = stringResource(id = R.string.storage_select_mode),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = preset.descResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HimarkaTextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hardware Safety Boundary Notice Card
        HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = HimarkaViolet
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(id = R.string.storage_safety_notice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HimarkaTextMain
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    // Farmer Confirmation Dialog before submitting mode change intent
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
