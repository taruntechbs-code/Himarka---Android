package com.example.himarka.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himarka.R
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.data.model.AlertItem
import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.model.Telemetry
import com.example.himarka.data.repository.HimarkaRepository
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import com.example.himarka.domain.usecase.EvaluateStorageHealthUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val telemetry: Telemetry = Telemetry(),
    val activePreset: StoragePreset = StoragePreset.MODE_1,
    val storedCrops: List<CropProfile> = emptyList(),
    val activeAlerts: List<AlertItem> = emptyList(),
    val healthLevel: StatusLevel = StatusLevel.OPTIMAL,
    val healthMsgResId: Int = R.string.health_optimal,
    val currentTemp: Float = 2.6f
)

class DashboardViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance,
    private val evaluateHealthUseCase: EvaluateStorageHealthUseCase = EvaluateStorageHealthUseCase()
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.telemetryFlow,
        repository.activePresetFlow,
        repository.storedCropsFlow,
        repository.alertsFlow
    ) { telemetry, preset, crops, alerts ->
        val health = evaluateHealthUseCase(telemetry, preset)
        DashboardUiState(
            telemetry = telemetry,
            activePreset = preset,
            storedCrops = crops,
            activeAlerts = alerts.filter { !it.isResolved },
            healthLevel = health.level,
            healthMsgResId = health.statusMsgResId,
            currentTemp = health.currentTemp
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}
