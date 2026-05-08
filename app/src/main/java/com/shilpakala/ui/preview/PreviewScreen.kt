package com.shilpakala.ui.preview

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.ui.components.ShilpaKalaTopBar
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.DeepBrown
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.LightSand
import com.shilpakala.ui.theme.SoftParchment
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun PreviewScreen(
    navController: NavHostController,
    encodedImagePath: String,
    viewModel: PreviewViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ── Observe language changes ──────────────────────
    val currentLanguage by LanguageManager.currentLanguageFlow.collectAsState()
    val imagePath = URLDecoder.decode(
        encodedImagePath,
        StandardCharsets.UTF_8.toString()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // ── Top Bar ───────────────────────────────────
        ShilpaKalaTopBar(
            title = LanguageManager.text(
                "Your Branded Photo 🎉",
                "ನಿಮ್ಮ ಬ್ರಾಂಡೆಡ್ ಫೋಟೋ 🎉"
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Branded Photo ─────────────────────────
            AsyncImage(
                model = Uri.parse(imagePath),
                contentDescription = "Branded product photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // ── Success Message ───────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftParchment)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✦ Handmade in Karnataka ✦",
                    style = MaterialTheme.typography.labelLarge,
                    color = HeritagGold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LanguageManager.text(
                        "Your photo is ready to share!",
                        "ನಿಮ್ಮ ಫೋಟೋ ಹಂಚಿಕೊಳ್ಳಲು ಸಿದ್ಧವಾಗಿದೆ!"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepBrown,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ── Share Button ──────────────────────────
            Button(
                onClick = { viewModel.shareImage(imagePath) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta
                )
            ) {
                Text(
                    text = LanguageManager.text(
                        "📤 Share",
                        "📤 ಹಂಚಿಕೊಳ್ಳಿ"
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmCream
                )
            }

            // ── Gallery + New Photo Buttons ───────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.Gallery.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Terracotta
                    )
                ) {
                    Text(
                        text = LanguageManager.text(
                            "🖼️ Gallery",
                            "🖼️ ಗ್ಯಾಲರಿ"
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = Terracotta
                    )
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.Camera.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Terracotta
                    )
                ) {
                    Text(
                        text = LanguageManager.text(
                            "📸 New Photo",
                            "📸 ಹೊಸ ಫೋಟೋ"
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = Terracotta
                    )
                }
            }

            // ── Back to Home ──────────────────────────
            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, LightSand
                )
            ) {
                Text(
                    text = LanguageManager.text(
                        "🏠 Back to Home",
                        "🏠 ಮುಖಪುಟಕ್ಕೆ ಹಿಂತಿರುಗಿ"
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = DeepBrown
                )
            }
        }
    }
}