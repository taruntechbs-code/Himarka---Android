package com.example.himarka.feature.energy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himarka.data.model.Telemetry
import com.example.himarka.data.repository.HimarkaRepository
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class EnergyUiState(
    val telemetry: Telemetry = Telemetry()
)

class EnergyViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance
) : ViewModel() {

    val uiState: StateFlow<EnergyUiState> = repository.telemetryFlow
        .map { EnergyUiState(telemetry = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EnergyUiState()
        )
}
