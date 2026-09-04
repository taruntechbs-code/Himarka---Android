package com.example.himarka.domain.usecase

import com.example.himarka.R
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.model.Telemetry

data class StorageHealthResult(
    val level: StatusLevel,
    val statusMsgResId: Int,
    val currentTemp: Float,
    val tempDiff: Float
)

class EvaluateStorageHealthUseCase {
    operator fun invoke(telemetry: Telemetry, activePreset: StoragePreset): StorageHealthResult {
        val currentTemp = telemetry.temperatureC
        val minTarget = activePreset.targetTempMin
        val maxTarget = activePreset.targetTempMax

        return when {
            !telemetry.isDeviceOnline -> StorageHealthResult(
                level = StatusLevel.CRITICAL,
                statusMsgResId = R.string.health_offline,
                currentTemp = currentTemp,
                tempDiff = 0f
            )
            currentTemp in minTarget..maxTarget -> StorageHealthResult(
                level = StatusLevel.OPTIMAL,
                statusMsgResId = R.string.health_optimal,
                currentTemp = currentTemp,
                tempDiff = 0f
            )
            currentTemp < minTarget -> {
                val diff = minTarget - currentTemp
                StorageHealthResult(
                    level = if (diff > 2f) StatusLevel.CRITICAL else StatusLevel.WARNING,
                    statusMsgResId = R.string.health_below_target,
                    currentTemp = currentTemp,
                    tempDiff = -diff
                )
            }
            else -> {
                val diff = currentTemp - maxTarget
                StorageHealthResult(
                    level = if (diff > 3f) StatusLevel.CRITICAL else StatusLevel.WARNING,
                    statusMsgResId = R.string.health_above_target,
                    currentTemp = currentTemp,
                    tempDiff = diff
                )
            }
        }
    }
}
