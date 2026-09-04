package com.example.himarka.feature.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himarka.data.model.CommandIntent
import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.repository.HimarkaRepository
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import com.example.himarka.domain.usecase.EvaluateMultiCropCompatibilityUseCase
import com.example.himarka.domain.usecase.MultiCropCompatibilityResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class StorageUiState(
    val activePreset: StoragePreset = StoragePreset.MODE_1,
    val storedCrops: List<CropProfile> = emptyList(),
    val compatibility: MultiCropCompatibilityResult? = null,
    val allPresets: List<StoragePreset> = StoragePreset.entries
)

class StorageViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance,
    private val compatibilityUseCase: EvaluateMultiCropCompatibilityUseCase = EvaluateMultiCropCompatibilityUseCase()
) : ViewModel() {

    val uiState: StateFlow<StorageUiState> = combine(
        repository.activePresetFlow,
        repository.storedCropsFlow
    ) { preset, crops ->
        StorageUiState(
            activePreset = preset,
            storedCrops = crops,
            compatibility = compatibilityUseCase(crops)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StorageUiState()
    )

    fun requestModeChange(preset: StoragePreset) {
        repository.submitPresetCommand(
            CommandIntent.SetStoragePresetCommand(
                targetPreset = preset
            )
        )
    }
}
