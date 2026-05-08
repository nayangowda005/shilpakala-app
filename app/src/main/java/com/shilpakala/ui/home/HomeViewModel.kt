package com.shilpakala.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shilpakala.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val artisanName: String = "",
    val selectedLanguage: String = "en"
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            prefs.artisanName.collect { name ->
                _uiState.value = _uiState.value.copy(artisanName = name)
            }
        }
    }
}