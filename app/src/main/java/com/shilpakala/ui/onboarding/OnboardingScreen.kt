package com.shilpakala.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.DeepBrown
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.LightSand
import com.shilpakala.ui.theme.MediumBrown
import com.shilpakala.ui.theme.SoftParchment
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream

@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // ── Observe language changes ──────────────────────
    // Removed unused currentLanguage variable
    val keyboardController = LocalSoftwareKeyboardController.current

    // ── Navigate when saved ───────────────────────────
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Onboarding.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ── Header ───────────────────────────────────
        Text(
            text = "🪵",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to Shilpa-Kala",
            style = MaterialTheme.typography.headlineMedium,
            color = Terracotta,
            textAlign = TextAlign.Center
        )

        Text(
            text = "ಶಿಲ್ಪ ಕಲೆಗೆ ಸ್ವಾಗತ",
            style = MaterialTheme.typography.titleMedium,
            color = HeritagGold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // ── Name Input ───────────────────────────────
        Text(
            text = "What is your name? / ನಿಮ್ಮ ಹೆಸರೇನು?",
            style = MaterialTheme.typography.titleMedium,
            color = DeepBrown,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.artisanName,
            onValueChange = { viewModel.onNameChange(it) },
            placeholder = {
                Text(
                    text = "Enter your name / ಹೆಸರು ನಮೂದಿಸಿ",
                    color = MediumBrown.copy(alpha = 0.6f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Terracotta,
                unfocusedBorderColor = LightSand,
                focusedTextColor = DeepBrown,
                unfocusedTextColor = DeepBrown,
                cursorColor = Terracotta
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ── Language Selector ────────────────────────
        Text(
            text = "Choose Language / ಭಾಷೆ ಆಯ್ಕೆ ಮಾಡಿ",
            style = MaterialTheme.typography.titleMedium,
            color = DeepBrown,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LanguageChip(
                label = "English",
                isSelected = uiState.selectedLanguage == "en",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.onLanguageChange("en") }
            )
            LanguageChip(
                label = "ಕನ್ನಡ",
                isSelected = uiState.selectedLanguage == "kn",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.onLanguageChange("kn") }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // ── Continue Button ──────────────────────────
        Button(
            onClick = {
                keyboardController?.hide()
                viewModel.saveAndContinue()
            },
            enabled = uiState.artisanName.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Terracotta,
                disabledContainerColor = LightSand
            )
        ) {
            Text(
                text = "Let's Begin / ಪ್ರಾರಂಭಿಸೋಣ",
                style = MaterialTheme.typography.labelLarge,
                color = WarmCream
            )
        }
    }
}

// ── Language Chip Component ───────────────────────────
@Composable
fun LanguageChip(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Terracotta else SoftParchment
    val textColor = if (isSelected) WarmCream else DeepBrown
    val borderColor = if (isSelected) Terracotta else LightSand

    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    )
}
