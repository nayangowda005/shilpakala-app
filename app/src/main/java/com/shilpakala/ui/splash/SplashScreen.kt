package com.shilpakala.ui.splash

import com.shilpakala.data.preferences.LanguageManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    isFirstLaunch: Boolean = true   // we'll wire this to DataStore later
) {
    // ── Animation ────────────────────────────────────
    val alpha = remember { Animatable(0f) }
    // ── Observe language changes ──────────────────────
    val currentLanguage by LanguageManager.currentLanguageFlow.collectAsState()

    LaunchedEffect(Unit) {
        // Fade in
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        // Hold for 1.5 seconds
        delay(1500)
        // Navigate based on first launch
        if (isFirstLaunch) {
            navController.navigate(Screen.Onboarding.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    // ── UI ───────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Terracotta)
            .padding(32.dp)
            .alpha(alpha.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon Placeholder (emoji for now)
        Text(
            text = "🪵",
            style = MaterialTheme.typography.displayLarge,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // App Name
        // App Name
        Text(
            text = LanguageManager.text("Shilpa-Kala", "ಶಿಲ್ಪ-ಕಲಾ"),
            style = MaterialTheme.typography.displayLarge,
            color = WarmCream,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Kannada Subtitle
        Text(
            text = "ಶಿಲ್ಪ ಕಲೆ",
            style = MaterialTheme.typography.headlineMedium,
            color = HeritagGold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tagline
        Text(
            text = LanguageManager.text(
                "Your Digital Portfolio Assistant",
                "ನಿಮ್ಮ ಡಿಜಿಟಲ್ ಪೋರ್ಟ್‌ಫೋಲಿಯೋ ಸಹಾಯಕ"
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = WarmCream.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        // Kannada Tagline
        Text(
            text = "ನಿಮ್ಮ ಡಿಜಿಟಲ್ ಪೋರ್ಟ್‌ಫೋಲಿಯೋ ಸಹಾಯಕ",
            style = MaterialTheme.typography.bodyMedium,
            color = HeritagGold.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}