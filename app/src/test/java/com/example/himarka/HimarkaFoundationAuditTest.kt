package com.example.himarka

import com.example.himarka.data.model.CommandIntent
import com.example.himarka.data.model.CommandStatus
import com.example.himarka.data.model.CropCatalog
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import com.example.himarka.domain.usecase.EvaluateMultiCropCompatibilityUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HimarkaFoundationAuditTest {

    private lateinit var repository: HimarkaRepositoryImpl
    private val compatibilityUseCase = EvaluateMultiCropCompatibilityUseCase()

    @Before
    fun setUp() {
        repository = HimarkaRepositoryImpl()
    }

    @Test
    fun `adding crop MUST NOT automatically change active storage preset`() {
        val tomato = CropCatalog.allCrops.first { it.id == "tomato" } // Recommended: Mode 3
        val initialActivePreset = repository.activePresetFlow.value // Initial: Mode 1

        repository.addStoredCrop(tomato)

        assertTrue(repository.storedCropsFlow.value.any { it.id == "tomato" })
        assertEquals(initialActivePreset, repository.activePresetFlow.value)
    }

    @Test
    fun `farmer command intent submission changes active storage preset`() {
        val command = CommandIntent.SetStoragePresetCommand(
            targetPreset = StoragePreset.MODE_3
        )

        val result = repository.submitPresetCommand(command)

        assertEquals(CommandStatus.EXECUTED, result.status)
        assertEquals(StoragePreset.MODE_3, repository.activePresetFlow.value)
    }

    @Test
    fun `multiple crops with same preset are compatible`() {
        val cabbage = CropCatalog.allCrops.first { it.id == "cabbage" } // Mode 1
        val carrot = CropCatalog.allCrops.first { it.id == "carrot" } // Mode 1

        val result = compatibilityUseCase(listOf(cabbage, carrot))

        assertTrue(result.isCompatible)
        assertEquals(StoragePreset.MODE_1, result.recommendedPreset)
    }

    @Test
    fun `multiple crops with different presets are conflicting and no average is made`() {
        val tomato = CropCatalog.allCrops.first { it.id == "tomato" } // Mode 3
        val cabbage = CropCatalog.allCrops.first { it.id == "cabbage" } // Mode 1

        val result = compatibilityUseCase(listOf(tomato, cabbage))

        assertFalse(result.isCompatible)
        assertNull(result.recommendedPreset)
        assertEquals(2, result.conflictingPresets.size)
        assertTrue(result.conflictingPresets.contains(StoragePreset.MODE_1))
        assertTrue(result.conflictingPresets.contains(StoragePreset.MODE_3))
    }

    @Test
    fun `garlic crop profile remains explicitly unverified`() {
        val garlic = CropCatalog.allCrops.first { it.id == "garlic" }

        assertFalse(garlic.isVerified)
        assertEquals("Unverified", garlic.scientificReferenceTemp)
    }

    @Test
    fun `demo telemetry mode state toggle updates telemetry model`() {
        repository.setDemoMode(true)
        assertTrue(repository.telemetryFlow.value.isDemoMode)

        repository.setDemoMode(false)
        assertFalse(repository.telemetryFlow.value.isDemoMode)
    }
}
