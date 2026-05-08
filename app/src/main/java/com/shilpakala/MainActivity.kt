package com.shilpakala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.data.preferences.UserPreferences
import com.shilpakala.ui.navigation.NavGraph
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.ShilpaKalaTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = UserPreferences(this)

        lifecycleScope.launch {
            // Load saved language before UI starts
            val language = prefs.selectedLanguage.first()
            val isFirstLaunch = prefs.isFirstLaunch.first()
            LanguageManager.currentLanguage = language

            setContent {
                ShilpaKalaTheme {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        startDestination = if (isFirstLaunch)
                            Screen.Splash.route
                        else
                            Screen.Home.route
                    )
                }
            }
        }
    }
}