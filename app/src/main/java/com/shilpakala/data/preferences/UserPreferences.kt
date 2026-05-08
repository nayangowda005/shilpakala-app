package com.shilpakala.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ── DataStore instance ────────────────────────────────
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "shilpakala_prefs"
)

class UserPreferences(private val context: Context) {

    companion object {
        val ARTISAN_NAME = stringPreferencesKey("artisan_name")
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
    }

    // ── Read ─────────────────────────────────────────
    val artisanName: Flow<String> = context.dataStore.data
        .map { it[ARTISAN_NAME] ?: "" }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data
        .map { it[IS_FIRST_LAUNCH] ?: true }

    val selectedLanguage: Flow<String> = context.dataStore.data
        .map { it[SELECTED_LANGUAGE] ?: "en" }

    // ── Write ────────────────────────────────────────
    suspend fun saveArtisanName(name: String) {
        context.dataStore.edit { it[ARTISAN_NAME] = name }
    }

    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { it[IS_FIRST_LAUNCH] = false }
    }

    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { it[SELECTED_LANGUAGE] = language }
    }
}