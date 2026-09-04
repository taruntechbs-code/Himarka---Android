package com.example.himarka.domain.usecase

import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset

data class MultiCropCompatibilityResult(
    val storedCrops: List<CropProfile>,
    val isCompatible: Boolean,
    val recommendedPreset: StoragePreset?,
    val conflictingPresets: Set<StoragePreset>
)

class EvaluateMultiCropCompatibilityUseCase {
    operator fun invoke(crops: List<CropProfile>): MultiCropCompatibilityResult {
        if (crops.isEmpty()) {
            return MultiCropCompatibilityResult(crops, true, null, emptySet())
        }
        val requiredPresets = crops.map { it.recommendedPreset }.toSet()
        val isCompatible = requiredPresets.size == 1
        return MultiCropCompatibilityResult(
            storedCrops = crops,
            isCompatible = isCompatible,
            recommendedPreset = if (isCompatible) requiredPresets.first() else null,
            conflictingPresets = if (isCompatible) emptySet() else requiredPresets
        )
    }
}
