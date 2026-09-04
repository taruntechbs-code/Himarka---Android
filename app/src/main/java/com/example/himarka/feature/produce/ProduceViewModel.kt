package com.example.himarka.feature.produce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himarka.data.model.CommandIntent
import com.example.himarka.data.model.CropCatalog
import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.repository.HimarkaRepository
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import com.example.himarka.domain.usecase.EvaluateMultiCropCompatibilityUseCase
import com.example.himarka.domain.usecase.MultiCropCompatibilityResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProduceUiState(
    val storedCrops: List<CropProfile> = emptyList(),
    val activePreset: StoragePreset = StoragePreset.MODE_1,
    val compatibility: MultiCropCompatibilityResult? = null,
    val cropCatalog: List<CropProfile> = CropCatalog.allCrops,
    val searchQuery: String = "",
    val isAddingProduce: Boolean = false
)

class ProduceViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance,
    private val compatibilityUseCase: EvaluateMultiCropCompatibilityUseCase = EvaluateMultiCropCompatibilityUseCase()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isAddingProduce = MutableStateFlow(false)

    val uiState: StateFlow<ProduceUiState> = combine(
        repository.storedCropsFlow,
        repository.activePresetFlow,
        _searchQuery,
        _isAddingProduce
    ) { storedCrops, activePreset, query, isAdding ->
        val filteredCatalog = if (query.isBlank()) {
            CropCatalog.allCrops.filter { crop -> !storedCrops.any { it.id == crop.id } }
        } else {
            CropCatalog.allCrops.filter { crop -> 
                !storedCrops.any { it.id == crop.id } && crop.name.contains(query, ignoreCase = true) 
            }
        }
        ProduceUiState(
            storedCrops = storedCrops,
            activePreset = activePreset,
            compatibility = compatibilityUseCase(storedCrops),
            cropCatalog = filteredCatalog,
            searchQuery = query,
            isAddingProduce = isAdding
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProduceUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun setAddingProduce(isAdding: Boolean) {
        _isAddingProduce.value = isAdding
        if (!isAdding) _searchQuery.value = ""
    }

    fun addStoredCrop(crop: CropProfile) {
        repository.addStoredCrop(crop)
        setAddingProduce(false)
    }
    
    fun removeStoredCrop(cropId: String) {
        repository.removeStoredCrop(cropId)
    }

    fun requestPresetChange(preset: StoragePreset) {
        repository.submitPresetCommand(
            CommandIntent.SetStoragePresetCommand(
                targetPreset = preset
            )
        )
    }
}
