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
import com.example.himarka.domain.usecase.EvaluateMultiCropCompatibilityUseCase
import com.example.himarka.domain.usecase.EvaluateStorageHealthUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class HumidityStatus(val labelResId: Int, val level: StatusLevel) {
    STABLE(R.string.humidity_stable, StatusLevel.OPTIMAL),
    HIGH(R.string.humidity_high, StatusLevel.WARNING),
    LOW(R.string.humidity_low, StatusLevel.WARNING)
}

enum class CoolingStatus(val labelResId: Int, val level: StatusLevel) {
    ACTIVE(R.string.cooling_active, StatusLevel.OPTIMAL),
    INACTIVE(R.string.cooling_inactive, StatusLevel.NEUTRAL),
    UNAVAILABLE(R.string.cooling_unavailable, StatusLevel.CRITICAL)
}

enum class SolarStatus(val labelResId: Int, val level: StatusLevel) {
    ACTIVE(R.string.solar_active, StatusLevel.OPTIMAL),
    AVAILABLE(R.string.solar_available, StatusLevel.NEUTRAL),
    UNAVAILABLE(R.string.solar_unavailable, StatusLevel.NEUTRAL),
    UNKNOWN(R.string.solar_unknown, StatusLevel.NEUTRAL)
}

enum class PowerStatus(val labelResId: Int, val level: StatusLevel) {
    COOLING_READY(R.string.power_cooling_ready, StatusLevel.OPTIMAL),
    RESTRICTED(R.string.power_cooling_restricted, StatusLevel.WARNING)
}

data class DashboardUiState(
    val telemetry: Telemetry = Telemetry(),
    val activePreset: StoragePreset = StoragePreset.MODE_1,
    val storedCrops: List<CropProfile> = emptyList(),
    val activeAlerts: List<AlertItem> = emptyList(),
    val healthLevel: StatusLevel = StatusLevel.OPTIMAL,
    val healthHeadlineResId: Int = R.string.health_status_healthy,
    val healthCoolingNoteResId: Int = R.string.health_cooling_working,
    val currentTemp: Float = 2.6f,
    val humidityStatus: HumidityStatus = HumidityStatus.STABLE,
    val coolingStatus: CoolingStatus = CoolingStatus.ACTIVE,
    val solarStatus: SolarStatus = SolarStatus.ACTIVE,
    val powerStatus: PowerStatus = PowerStatus.COOLING_READY,
    val formattedProduceSummary: String = "Cabbage",
    val isProduceConflicting: Boolean = false,
    val produceCompatibilityTagResId: Int? = null,
    val primaryAlert: AlertItem? = null,
    val actionMessageResId: Int? = null,
    val actionMessageCustom: String? = null
)

class DashboardViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance,
    private val evaluateHealthUseCase: EvaluateStorageHealthUseCase = EvaluateStorageHealthUseCase(),
    private val compatibilityUseCase: EvaluateMultiCropCompatibilityUseCase = EvaluateMultiCropCompatibilityUseCase()
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.telemetryFlow,
        repository.activePresetFlow,
        repository.storedCropsFlow,
        repository.alertsFlow
    ) { telemetry, preset, crops, alerts ->
        val health = evaluateHealthUseCase(telemetry, preset)
        val unresolvedAlerts = alerts.filter { !it.isResolved }
        val primary = unresolvedAlerts.firstOrNull()

        val isCoolingFailure = !telemetry.isCoolingActive && (health.currentTemp > preset.targetTempMax)
        val hasCriticalAlert = unresolvedAlerts.any { it.severity == com.example.himarka.data.model.AlertSeverity.CRITICAL }

        val headline = when {
            !telemetry.isDeviceOnline -> R.string.health_offline
            isCoolingFailure || hasCriticalAlert -> R.string.health_status_critical
            health.level == StatusLevel.CRITICAL || health.level == StatusLevel.WARNING -> R.string.health_status_temp_attention
            else -> R.string.health_status_healthy
        }

        val effectiveHealthLevel = when {
            !telemetry.isDeviceOnline || isCoolingFailure || hasCriticalAlert -> StatusLevel.CRITICAL
            health.level == StatusLevel.CRITICAL || health.level == StatusLevel.WARNING -> StatusLevel.WARNING
            else -> StatusLevel.OPTIMAL
        }

        val coolingNote = when {
            !telemetry.isDeviceOnline -> R.string.health_cooling_off
            telemetry.isCoolingActive -> R.string.health_cooling_working
            else -> R.string.health_cooling_idle
        }

        val compatibility = if (crops.size > 1) compatibilityUseCase(crops) else null
        val isConflicting = compatibility?.isCompatible == false
        val compatTag = when {
            crops.isEmpty() -> null
            isConflicting -> R.string.produce_conflict_mode
            else -> R.string.produce_suitable_mode
        }

        val actionResId: Int? = when {
            primary?.title?.contains("Door", ignoreCase = true) == true -> R.string.action_close_door
            health.level != StatusLevel.OPTIMAL -> R.string.action_check_cooling
            telemetry.batteryPercent < 20 -> R.string.action_save_power
            else -> null
        }
        val customMsg = if (actionResId == null && primary != null) primary.message else null

        DashboardUiState(
            telemetry = telemetry,
            activePreset = preset,
            storedCrops = crops,
            activeAlerts = unresolvedAlerts,
            healthLevel = effectiveHealthLevel,
            healthHeadlineResId = headline,
            healthCoolingNoteResId = coolingNote,
            currentTemp = health.currentTemp,
            humidityStatus = evaluateHumidityStatus(telemetry.humidityPercent),
            coolingStatus = evaluateCoolingStatus(telemetry),
            solarStatus = evaluateSolarStatus(telemetry),
            powerStatus = evaluatePowerStatus(telemetry.batteryPercent),
            formattedProduceSummary = formatStoredProduce(crops),
            isProduceConflicting = isConflicting,
            produceCompatibilityTagResId = compatTag,
            primaryAlert = primary,
            actionMessageResId = actionResId,
            actionMessageCustom = customMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    companion object {
        fun evaluateHumidityStatus(humidity: Float): HumidityStatus = when {
            humidity > 95f -> HumidityStatus.HIGH
            humidity < 70f -> HumidityStatus.LOW
            else -> HumidityStatus.STABLE
        }

        fun evaluateCoolingStatus(telemetry: Telemetry): CoolingStatus = when {
            !telemetry.isDeviceOnline -> CoolingStatus.UNAVAILABLE
            telemetry.isCoolingActive -> CoolingStatus.ACTIVE
            else -> CoolingStatus.INACTIVE
        }

        fun evaluateSolarStatus(telemetry: Telemetry): SolarStatus = when {
            !telemetry.isDeviceOnline -> SolarStatus.UNKNOWN
            telemetry.solarGenerationW > 0f -> SolarStatus.ACTIVE
            telemetry.solarVoltageV > 12f -> SolarStatus.AVAILABLE
            else -> SolarStatus.UNAVAILABLE
        }

        fun evaluatePowerStatus(batteryPercent: Int): PowerStatus = when {
            batteryPercent >= 20 -> PowerStatus.COOLING_READY
            else -> PowerStatus.RESTRICTED
        }

        fun formatStoredProduce(crops: List<CropProfile>): String = when {
            crops.isEmpty() -> ""
            crops.size == 1 -> crops[0].name
            crops.size == 2 -> "${crops[0].name} + ${crops[1].name}"
            else -> "${crops[0].name} + ${crops[1].name} (+${crops.size - 2})"
        }
    }
}
