package com.example.himarka.feature.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himarka.data.model.AlertItem
import com.example.himarka.data.repository.HimarkaRepository
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AlertsUiState(
    val alerts: List<AlertItem> = emptyList()
)

class AlertsViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance
) : ViewModel() {

    val uiState: StateFlow<AlertsUiState> = repository.alertsFlow
        .map { AlertsUiState(alerts = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AlertsUiState()
        )

    fun acknowledgeAlert(alertId: String) {
        repository.acknowledgeAlert(alertId)
    }
}
