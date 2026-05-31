package com.shilpakala.ui.home

import com.shilpakala.ui.components.ShilpaKalaTopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.DeepBrown
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.MediumBrown
import com.shilpakala.ui.theme.SoftParchment
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ── Observe language changes ──────────────────────
    val currentLanguage by LanguageManager.currentLanguageFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // ── Top Header ───────────────────────────────
        HomeHeader(artisanName = uiState.artisanName)

        // ── Main Content ─────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = LanguageManager.text(
                    "What would you like to do?",
                    "ನೀವು ಏನು ಮಾಡಲು ಬಯಸುತ್ತೀರಿ?"
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MediumBrown
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Take Photo Card ───────────────────────
            ActionCard(
                emoji = "📸",
                title = LanguageManager.text(
                    "Take Product Photo",
                    "ಉತ್ಪನ್ನದ ಫೋಟೋ ತೆಗೆಯಿರಿ"
                ),
                description = LanguageManager.text(
                    "Capture your craft with guided framing",
                    "ಮಾರ್ಗದರ್ಶಿ ಚೌಕಟ್ಟಿನೊಂದಿಗೆ ನಿಮ್ಮ ಕರಕುಶಲವನ್ನು ಸೆರೆಹಿಡಿಯಿರಿ"
                ),
                backgroundColor = Terracotta,
                textColor = WarmCream,
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.CameraGuide.route) }
            )

            // ── Gallery Card ──────────────────────────
            ActionCard(
                emoji = "🖼️",
                title = LanguageManager.text(
                    "My Gallery",
                    "ನನ್ನ ಗ್ಯಾಲರಿ"
                ),
                description = LanguageManager.text(
                    "View and share your branded photos",
                    "ನಿಮ್ಮ ಬ್ರಾಂಡೆಡ್ ಫೋಟೋಗಳನ್ನು ವೀಕ್ಷಿಸಿ ಮತ್ತು ಹಂಚಿಕೊಳ್ಳಿ"
                ),
                backgroundColor = SoftParchment,
                textColor = DeepBrown,
                modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.Gallery.route) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Quick Tips ────────────────────────────
            Text(
                text = "💡 ${LanguageManager.text("Quick Tip", "ತ್ವರಿತ ಸಲಹೆ")}",
                style = MaterialTheme.typography.titleSmall,
                color = HeritagGold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = SoftParchment
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = LanguageManager.text(
                        "Place your product on a plain surface " +
                                "and use natural light for best results.",
                        "ಉತ್ತಮ ಫಲಿತಾಂಶಕ್ಕಾಗಿ ನಿಮ್ಮ ಉತ್ಪನ್ನವನ್ನು " +
                                "ಸರಳ ಮೇಲ್ಮೈಯಲ್ಲಿ ಇರಿಸಿ ಮತ್ತು ನೈಸರ್ಗಿಕ " +
                                "ಬೆಳಕನ್ನು ಬಳಸಿ."
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MediumBrown,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// ── Header Composable ─────────────────────────────────
@Composable
fun HomeHeader(artisanName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Terracotta)
    ) {
        ShilpaKalaTopBar(
            title = "Shilpa-Kala 🪵"
        )
        if (artisanName.isNotEmpty()) {
            Text(
                text = LanguageManager.text(
                    "Welcome, $artisanName!",
                    "ಸ್ವಾಗತ, $artisanName!"
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = HeritagGold,
                modifier = Modifier.padding(
                    start = 20.dp,
                    bottom = 12.dp
                )
            )
        }
    }
}

// ── Action Card Composable ────────────────────────────
@Composable
fun ActionCard(
    emoji: String,
    title: String,
    description: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = emoji,
                modifier = Modifier.size(48.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                )
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "→",
                style = MaterialTheme.typography.titleLarge,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}