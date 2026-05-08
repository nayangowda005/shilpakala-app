package com.shilpakala.data.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LanguageManager {
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguageFlow: StateFlow<String> = _currentLanguage.asStateFlow()
    
    var currentLanguage: String
        get() = _currentLanguage.value
        set(value) {
            _currentLanguage.value = value
        }

    fun isKannada() = currentLanguage == "kn"

    // ── Helper to pick right text ─────────────────────
    fun text(english: String, kannada: String): String {
        return if (isKannada()) kannada else english
    }
}