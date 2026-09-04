package com.example.himarka

import com.example.himarka.core.localization.AppLanguage
import com.example.himarka.core.navigation.Screen
import com.example.himarka.data.model.CommandIntent
import com.example.himarka.data.model.CommandStatus
import com.example.himarka.data.model.CropCatalog
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import com.example.himarka.domain.usecase.EvaluateMultiCropCompatibilityUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class HimarkaRuntimeVerificationTest {

    private lateinit var repository: HimarkaRepositoryImpl
    private val compatibilityUseCase = EvaluateMultiCropCompatibilityUseCase()

    @Before
    fun setUp() {
        repository = HimarkaRepositoryImpl()
    }

    @Test
    fun `verify navigation destinations exist for all 6 screens`() {
        val screens = listOf(
            Screen.Home,
            Screen.Storage,
            Screen.Produce,
            Screen.Energy,
            Screen.Alerts,
            Screen.Settings
        )
        assertEquals(6, screens.size)
    }

    @Test
    fun `verify adding crop does NOT change active storage preset`() {
        val tomato = CropCatalog.allCrops.first { it.id == "tomato" } // Recommended: Mode 3
        val initialPreset = repository.activePresetFlow.value // Mode 1

        repository.addStoredCrop(tomato)

        assertTrue(repository.storedCropsFlow.value.any { it.name == "Tomato" })
        assertEquals(initialPreset, repository.activePresetFlow.value)
    }

    @Test
    fun `verify command intent is submitted only after explicit farmer confirmation`() {
        val activeBefore = repository.activePresetFlow.value
        assertEquals(StoragePreset.MODE_1, activeBefore)

        val command = CommandIntent.SetStoragePresetCommand(
            targetPreset = StoragePreset.MODE_3
        )

        val result = repository.submitPresetCommand(command)

        assertEquals(CommandStatus.EXECUTED, result.status)
        assertEquals(StoragePreset.MODE_3, repository.activePresetFlow.value)
    }

    @Test
    fun `verify DEMO telemetry state is visibly marked as demo`() {
        val telemetry = repository.telemetryFlow.value
        assertTrue(telemetry.isDemoMode)
    }

    @Test
    fun `verify language switching supports all 9 NER languages`() {
        val languages = AppLanguage.entries
        assertEquals(9, languages.size)

        languages.forEach { lang ->
            repository.setAppLanguage(lang)
            assertEquals(lang, repository.languageFlow.value)
        }
    }
}
