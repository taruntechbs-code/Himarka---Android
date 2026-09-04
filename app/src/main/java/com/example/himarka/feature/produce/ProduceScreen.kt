package com.example.himarka.feature.produce

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.himarka.core.common.ui.HimarkaIconContainer
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaAmber
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaShapes
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet
import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset

@Composable
fun ProduceScreen(
    viewModel: ProduceViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isAddingProduce) {
        ProduceSelectionView(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onCropSelected = viewModel::addStoredCrop,
            onCancel = { viewModel.setAddingProduce(false) },
            modifier = modifier
        )
    } else {
        ProduceManagementView(
            uiState = uiState,
            onRemoveCrop = viewModel::removeStoredCrop,
            onAddProduceClicked = { viewModel.setAddingProduce(true) },
            onRequestPresetChange = viewModel::requestPresetChange,
            modifier = modifier
        )
    }
}

@Composable
private fun ProduceManagementView(
    uiState: ProduceUiState,
    onRemoveCrop: (String) -> Unit,
    onAddProduceClicked: () -> Unit,
    onRequestPresetChange: (StoragePreset) -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.produce_stored_produce),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = HimarkaTextMain
                )
                TextButton(onClick = onAddProduceClicked) {
                    Text(
                        text = stringResource(id = R.string.produce_add_produce),
                        style = MaterialTheme.typography.labelLarge,
                        color = HimarkaViolet
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            if (uiState.storedCrops.isEmpty()) {
                HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 0.dp) {
                    Text(
                        text = stringResource(id = R.string.produce_no_crops),
                        style = MaterialTheme.typography.bodyLarge,
                        color = HimarkaTextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onAddProduceClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = HimarkaViolet),
                        shape = HimarkaShapes.small
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.produce_add_produce))
                    }
                }
            }
        }

        items(uiState.storedCrops) { crop ->
            HimarkaCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = crop.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HimarkaTextMain
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(
                                id = R.string.produce_reference_prefix,
                                crop.scientificReferenceTemp,
                                stringResource(id = crop.recommendedPreset.titleResId)
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = HimarkaTextMuted
                        )
                    }
                    IconButton(onClick = { onRemoveCrop(crop.id) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.produce_remove),
                            tint = HimarkaTextMuted
                        )
                    }
                }
                if (!crop.isVerified) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = HimarkaAmber)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(id = R.string.produce_unverified_warn),
                            style = MaterialTheme.typography.bodyMedium,
                            color = HimarkaAmber
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.produce_compatibility),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 2.dp) {
                if (uiState.storedCrops.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.produce_no_crops),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HimarkaTextMuted
                    )
                } else {
                    val comp = uiState.compatibility
                    if (comp != null && comp.isCompatible && comp.recommendedPreset != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✓ " + stringResource(id = R.string.produce_compatible),
                                color = HimarkaEmerald,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.produce_compatible_desc, stringResource(id = comp.recommendedPreset.titleResId)),
                            style = MaterialTheme.typography.bodyLarge,
                            color = HimarkaTextMain
                        )
                        
                        if (comp.recommendedPreset != uiState.activePreset) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showConfirmationDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HimarkaViolet),
                                shape = HimarkaShapes.small,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = stringResource(id = R.string.action_request_mode, comp.recommendedPreset.formatTempRange()))
                            }
                        }
                    } else if (comp != null && !comp.isCompatible) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⚠ " + stringResource(id = R.string.produce_conflict),
                                color = HimarkaAmber,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.produce_conflict_desc),
                            style = MaterialTheme.typography.bodyLarge,
                            color = HimarkaTextMain
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(36.dp))
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog) {
        val recPreset = uiState.compatibility?.recommendedPreset
        if (recPreset != null) {
            ConfirmationDialog(
                title = stringResource(id = R.string.action_request_mode, recPreset.formatTempRange()),
                message = stringResource(
                    id = R.string.produce_conflict_warn,
                    stringResource(id = recPreset.titleResId),
                    recPreset.formatTempRange(),
                    stringResource(id = uiState.activePreset.titleResId),
                    uiState.activePreset.formatTempRange()
                ),
                onConfirm = {
                    onRequestPresetChange(recPreset)
                    showConfirmationDialog = false
                },
                onDismiss = {
                    showConfirmationDialog = false
                }
            )
        }
    }
}

@Composable
private fun ProduceSelectionView(
    uiState: ProduceUiState,
    onSearchQueryChange: (String) -> Unit,
    onCropSelected: (CropProfile) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.produce_select_to_add),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HimarkaTextMain
            )
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.btn_cancel),
                    tint = HimarkaTextMain
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(text = stringResource(id = R.string.produce_search_hint)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            shape = HimarkaShapes.small,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(uiState.cropCatalog, key = { it.id }) { crop ->
                HimarkaCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onCropSelected(crop) },
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = crop.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HimarkaTextMain
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stringResource(
                                    id = R.string.produce_reference_prefix,
                                    crop.scientificReferenceTemp,
                                    stringResource(id = crop.recommendedPreset.titleResId)
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = HimarkaTextMuted
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.produce_add_produce),
                            tint = HimarkaViolet
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(36.dp))
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}
