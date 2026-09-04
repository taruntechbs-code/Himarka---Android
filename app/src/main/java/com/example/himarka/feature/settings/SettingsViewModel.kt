package com.example.himarka.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.himarka.core.localization.AppLanguage
import com.example.himarka.data.model.Telemetry
import com.example.himarka.data.repository.HimarkaRepository
import com.example.himarka.data.repository.HimarkaRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SettingsUiState(
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val telemetry: Telemetry = Telemetry(),
    val allLanguages: List<AppLanguage> = AppLanguage.entries
)

class SettingsViewModel(
    private val repository: HimarkaRepository = HimarkaRepositoryImpl.instance
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        repository.languageFlow,
        repository.telemetryFlow
    ) { language, telemetry ->
        SettingsUiState(
            currentLanguage = language,
            telemetry = telemetry
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setLanguage(language: AppLanguage) {
        repository.setAppLanguage(language)
    }

    fun setDemoMode(isDemo: Boolean) {
        repository.setDemoMode(isDemo)
    }
}
