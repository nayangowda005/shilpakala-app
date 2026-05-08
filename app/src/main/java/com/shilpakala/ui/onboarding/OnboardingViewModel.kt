package com.shilpakala.ui.onboarding

import com.shilpakala.data.preferences.LanguageManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shilpakala.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val artisanName: String = "",
    val selectedLanguage: String = "en",
    val isSaved: Boolean = false
)

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    // ── Update name as user types ─────────────────────
    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(artisanName = name)
    }

    // ── Update language selection ─────────────────────
    fun onLanguageChange(language: String) {
        _uiState.value = _uiState.value.copy(selectedLanguage = language)
    }

    // ── Save and navigate ─────────────────────────────
    fun saveAndContinue() {
        viewModelScope.launch {
            prefs.saveArtisanName(_uiState.value.artisanName)
            prefs.saveLanguage(_uiState.value.selectedLanguage)
            prefs.setFirstLaunchDone()
            // ── Set language globally ──────────────────
            LanguageManager.currentLanguage = _uiState.value.selectedLanguage
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}