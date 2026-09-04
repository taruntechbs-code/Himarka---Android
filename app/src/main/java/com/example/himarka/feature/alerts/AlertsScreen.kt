package com.example.himarka.feature.alerts

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.himarka.core.common.ui.StatusChip
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.core.theme.HimarkaEmerald
import com.example.himarka.core.theme.HimarkaShapes
import com.example.himarka.core.theme.HimarkaTextMain
import com.example.himarka.core.theme.HimarkaTextMuted
import com.example.himarka.core.theme.HimarkaViolet
import com.example.himarka.data.model.AlertSeverity

@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.alerts_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = HimarkaTextMain
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.alerts.isEmpty()) {
            HimarkaCard(modifier = Modifier.fillMaxWidth(), elevation = 1.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = HimarkaEmerald,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.alerts_all_clear),
                        style = MaterialTheme.typography.bodyLarge,
                        color = HimarkaTextMain
                    )
                }
            }
        } else {
            LazyColumn {
                items(uiState.alerts) { alert ->
                    HimarkaCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = alert.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HimarkaTextMain
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = alert.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = HimarkaTextMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            StatusChip(
                                text = alert.severity.name,
                                level = when (alert.severity) {
                                    AlertSeverity.CRITICAL -> StatusLevel.CRITICAL
                                    AlertSeverity.WARNING -> StatusLevel.WARNING
                                    else -> StatusLevel.NEUTRAL
                                }
                            )
                        }

                        if (!alert.isAcknowledged) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.acknowledgeAlert(alert.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = HimarkaViolet),
                                shape = HimarkaShapes.small
                            ) {
                                Text(
                                    text = stringResource(id = R.string.alerts_acknowledge),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
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
}
