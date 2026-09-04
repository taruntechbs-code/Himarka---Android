package com.example.himarka.data.repository

import com.example.himarka.core.localization.AppLanguage
import com.example.himarka.data.model.AlertItem
import com.example.himarka.data.model.AlertSeverity
import com.example.himarka.data.model.CommandIntent
import com.example.himarka.data.model.CommandResult
import com.example.himarka.data.model.CommandStatus
import com.example.himarka.data.model.CropCatalog
import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.model.Telemetry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HimarkaRepositoryImpl : HimarkaRepository {

    private val _telemetry = MutableStateFlow(
        Telemetry(
            temperatureC = 2.6f,
            humidityPercent = 89.0f,
            gasPpm = 145.0f,
            solarVoltageV = 19.4f,
            solarGenerationW = 50.0f,
            batteryVoltageV = 26.8f,
            batteryPercent = 94,
            coolingPowerW = 38.5f,
            isCoolingActive = true,
            isDoorOpen = false,
            isDemoMode = true,
            isDeviceOnline = true
        )
    )
    override val telemetryFlow: StateFlow<Telemetry> = _telemetry.asStateFlow()

    private val _activePreset = MutableStateFlow(StoragePreset.MODE_1)
    override val activePresetFlow: StateFlow<StoragePreset> = _activePreset.asStateFlow()

    private val defaultCrops = listOfNotNull(CropCatalog.allCrops.find { it.id == "cabbage" })
    private val _storedCrops = MutableStateFlow<List<CropProfile>>(defaultCrops)
    override val storedCropsFlow: StateFlow<List<CropProfile>> = _storedCrops.asStateFlow()

    private val _alerts = MutableStateFlow<List<AlertItem>>(
        listOf(
            AlertItem(
                id = "alert-01",
                title = "Door Open Notice",
                message = "Storage door opened for 45s. Temperature stable.",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 15),
                severity = AlertSeverity.INFO
            )
        )
    )
    override val alertsFlow: StateFlow<List<AlertItem>> = _alerts.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    override val languageFlow: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _latestCommandResult = MutableStateFlow<CommandResult?>(null)
    override val latestCommandResultFlow: StateFlow<CommandResult?> = _latestCommandResult.asStateFlow()

    override fun setDemoMode(isDemo: Boolean) {
        _telemetry.value = _telemetry.value.copy(isDemoMode = isDemo)
    }

    override fun addStoredCrop(crop: CropProfile) {
        if (!_storedCrops.value.any { it.id == crop.id }) {
            _storedCrops.value = _storedCrops.value + crop
        }
    }

    override fun removeStoredCrop(cropId: String) {
        _storedCrops.value = _storedCrops.value.filter { it.id != cropId }
    }

    override fun submitPresetCommand(command: CommandIntent.SetStoragePresetCommand): CommandResult {
        val result = CommandResult(
            command = command,
            status = CommandStatus.EXECUTED,
            message = "Command intent validated and applied to active chamber preset."
        )
        _activePreset.value = command.targetPreset
        _latestCommandResult.value = result
        return result
    }

    override fun setAppLanguage(language: AppLanguage) {
        _language.value = language
    }

    override fun acknowledgeAlert(alertId: String) {
        _alerts.value = _alerts.value.map { alert ->
            if (alert.id == alertId) alert.copy(isAcknowledged = true) else alert
        }
    }

    companion object {
        val instance: HimarkaRepository by lazy { HimarkaRepositoryImpl() }
    }
}
