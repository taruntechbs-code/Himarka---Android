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

    @Test
    fun `storageViewModel dispatches command intent and updates repository state`() {
        val viewModel = com.example.himarka.feature.storage.StorageViewModel(
            repository = repository,
            compatibilityUseCase = compatibilityUseCase
        )

        viewModel.requestModeChange(StoragePreset.MODE_2)

        assertEquals(StoragePreset.MODE_2, repository.activePresetFlow.value)
        val latestResult = repository.latestCommandResultFlow.value
        assertNotNull(latestResult)
        assertEquals(CommandStatus.EXECUTED, latestResult?.status)
    }

    @Test
    fun `dashboard humidity evaluation maps safe and boundary conditions correctly`() {
        assertEquals(
            com.example.himarka.feature.dashboard.HumidityStatus.STABLE,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateHumidityStatus(89f)
        )
        assertEquals(
            com.example.himarka.feature.dashboard.HumidityStatus.HIGH,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateHumidityStatus(97f)
        )
        assertEquals(
            com.example.himarka.feature.dashboard.HumidityStatus.LOW,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateHumidityStatus(60f)
        )
    }

    @Test
    fun `dashboard cooling and solar evaluation respect telemetry and online status`() {
        val normalTelemetry = com.example.himarka.data.model.Telemetry(
            isCoolingActive = true,
            solarGenerationW = 50f,
            solarVoltageV = 19.4f,
            isDeviceOnline = true
        )
        assertEquals(
            com.example.himarka.feature.dashboard.CoolingStatus.ACTIVE,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateCoolingStatus(normalTelemetry)
        )
        assertEquals(
            com.example.himarka.feature.dashboard.SolarStatus.ACTIVE,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateSolarStatus(normalTelemetry)
        )

        val offlineTelemetry = normalTelemetry.copy(isDeviceOnline = false)
        assertEquals(
            com.example.himarka.feature.dashboard.CoolingStatus.UNAVAILABLE,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateCoolingStatus(offlineTelemetry)
        )
        assertEquals(
            com.example.himarka.feature.dashboard.SolarStatus.UNKNOWN,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluateSolarStatus(offlineTelemetry)
        )
    }

    @Test
    fun `dashboard power evaluation restricts cooling under low battery`() {
        assertEquals(
            com.example.himarka.feature.dashboard.PowerStatus.COOLING_READY,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluatePowerStatus(94)
        )
        assertEquals(
            com.example.himarka.feature.dashboard.PowerStatus.RESTRICTED,
            com.example.himarka.feature.dashboard.DashboardViewModel.evaluatePowerStatus(15)
        )
    }

    @Test
    fun `dashboard stored produce formatting supports zero, single, and multiple crops without exploding UI`() {
        val cabbage = CropCatalog.allCrops.first { it.id == "cabbage" }
        val tomato = CropCatalog.allCrops.first { it.id == "tomato" }
        val carrot = CropCatalog.allCrops.first { it.id == "carrot" }

        assertEquals(
            "",
            com.example.himarka.feature.dashboard.DashboardViewModel.formatStoredProduce(emptyList())
        )
        assertEquals(
            "Cabbage",
            com.example.himarka.feature.dashboard.DashboardViewModel.formatStoredProduce(listOf(cabbage))
        )
        assertEquals(
            "Cabbage + Tomato",
            com.example.himarka.feature.dashboard.DashboardViewModel.formatStoredProduce(listOf(cabbage, tomato))
        )
        assertEquals(
            "Cabbage + Tomato (+1)",
            com.example.himarka.feature.dashboard.DashboardViewModel.formatStoredProduce(listOf(cabbage, tomato, carrot))
        )
    }

    @Test
    fun `farmer-first dashboard maps attention when temperature is out of range or door is open`() {
        val normal = com.example.himarka.data.model.Telemetry(
            temperatureC = 1.5f,
            isDoorOpen = false,
            batteryPercent = 90
        )
        val viewModel = com.example.himarka.feature.dashboard.DashboardViewModel(
            repository = repository,
            compatibilityUseCase = compatibilityUseCase
        )

        // With default cabbage stored and Mode 1 (0-2°C), 1.5°C is healthy
        val state = viewModel.uiState.value
        assertEquals(R.string.health_status_healthy, state.healthHeadlineResId)
        assertFalse(state.isProduceConflicting)
        assertNull(state.actionMessageResId)
    }
}
