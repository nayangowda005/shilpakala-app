package com.shilpakala.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.data.preferences.UserPreferences
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShilpaKalaTopBar(
    title: String,
    onLanguageChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    // ── Observe global language state ────────────────────
    val currentLang by LanguageManager.currentLanguageFlow.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Terracotta)
            .padding(
                top = 48.dp,
                bottom = 16.dp,
                start = 20.dp,
                end = 20.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ── Screen Title ──────────────────────────────
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = WarmCream
        )

        // ── Language Toggle ───────────────────────────
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    WarmCream.copy(alpha = 0.5f),
                    RoundedCornerShape(20.dp)
                )
                .background(Terracotta),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // English Option
            LanguageChipItem(
                label = "EN",
                isSelected = currentLang == "en",
                onClick = {
                    if (currentLang != "en") {
                        LanguageManager.currentLanguage = "en"

                        scope.launch {
                            prefs.saveLanguage("en")
                        }

                        onLanguageChanged()
                    }
                }
            )

            Spacer(modifier = Modifier.width(2.dp))

            // Kannada Option
            LanguageChipItem(
                label = "ಕನ್ನಡ",
                isSelected = currentLang == "kn",
                onClick = {
                    if (currentLang != "kn") {
                        LanguageManager.currentLanguage = "kn"

                        scope.launch {
                            prefs.saveLanguage("kn")
                        }

                        onLanguageChanged()
                    }
                }
            )
        }
    }
}

// ── Language Chip Item ────────────────────────────────
@Composable
fun LanguageChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = if (isSelected) Terracotta else WarmCream,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) HeritagGold
                else Terracotta
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
    )
}