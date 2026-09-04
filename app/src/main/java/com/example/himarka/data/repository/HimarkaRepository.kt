package com.example.himarka.data.repository

import com.example.himarka.core.localization.AppLanguage
import com.example.himarka.data.model.AlertItem
import com.example.himarka.data.model.CommandIntent
import com.example.himarka.data.model.CommandResult
import com.example.himarka.data.model.CropProfile
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.model.Telemetry
import kotlinx.coroutines.flow.StateFlow

interface HimarkaRepository {
    val telemetryFlow: StateFlow<Telemetry>
    val activePresetFlow: StateFlow<StoragePreset>
    val storedCropsFlow: StateFlow<List<CropProfile>>
    val alertsFlow: StateFlow<List<AlertItem>>
    val languageFlow: StateFlow<AppLanguage>
    val latestCommandResultFlow: StateFlow<CommandResult?>

    fun setDemoMode(isDemo: Boolean)
    fun addStoredCrop(crop: CropProfile)
    fun removeStoredCrop(cropId: String)
    fun submitPresetCommand(command: CommandIntent.SetStoragePresetCommand): CommandResult
    fun setAppLanguage(language: AppLanguage)
    fun acknowledgeAlert(alertId: String)
}
