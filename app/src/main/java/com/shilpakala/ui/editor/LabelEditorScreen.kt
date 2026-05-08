package com.shilpakala.ui.editor

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
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
import com.shilpakala.ui.theme.MediumBrown
import com.shilpakala.ui.theme.SoftParchment
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun LabelEditorScreen(
    navController: NavHostController,
    encodedImagePath: String,
    viewModel: LabelEditorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ── Observe language changes ──────────────────────
    val currentLanguage by LanguageManager.currentLanguageFlow.collectAsState()
    val imagePath = URLDecoder.decode(
        encodedImagePath,
        StandardCharsets.UTF_8.toString()
    )

    LaunchedEffect(uiState.savedImagePath) {
        uiState.savedImagePath?.let { path ->
            val encoded = URLEncoder.encode(
                path,
                StandardCharsets.UTF_8.toString()
            )
            navController.navigate(Screen.Preview.createRoute(encoded))
            viewModel.clearSavedImage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // ── Top Bar ───────────────────────────────────
        ShilpaKalaTopBar(
            title = LanguageManager.text(
                "Add Product Label",
                "ಉತ್ಪನ್ನದ ಲೇಬಲ್ ಸೇರಿಸಿ"
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Photo Preview ─────────────────────────
            AsyncImage(
                model = Uri.parse(imagePath),
                contentDescription = "Captured product photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── Section Title ─────────────────────────
            Text(
                text = LanguageManager.text(
                    "Product Details",
                    "ಉತ್ಪನ್ನದ ವಿವರಗಳು"
                ),
                style = MaterialTheme.typography.titleMedium,
                color = DeepBrown
            )

            // ── Product Name ──────────────────────────
            LabelTextField(
                value = uiState.productName,
                onValueChange = { viewModel.onProductNameChange(it) },
                label = LanguageManager.text(
                    "Product Name",
                    "ಉತ್ಪನ್ನದ ಹೆಸರು"
                ),
                placeholder = LanguageManager.text(
                    "e.g. Gombe Doll",
                    "ಉದಾ: ಗೊಂಬೆ"
                )
            )

            // ── Wood Type ─────────────────────────────
            LabelTextField(
                value = uiState.woodType,
                onValueChange = { viewModel.onWoodTypeChange(it) },
                label = LanguageManager.text(
                    "Wood Type",
                    "ಮರದ ವಿಧ"
                ),
                placeholder = LanguageManager.text(
                    "e.g. Rosewood",
                    "ಉದಾ: ರೋಸ್‌ವುಡ್"
                )
            )

            // ── Price ─────────────────────────────────
            LabelTextField(
                value = uiState.price,
                onValueChange = { viewModel.onPriceChange(it) },
                label = LanguageManager.text(
                    "Price (₹)",
                    "ಬೆಲೆ (₹)"
                ),
                placeholder = LanguageManager.text(
                    "e.g. 500",
                    "ಉದಾ: ೫೦೦"
                ),
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ── Label Preview Box ─────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftParchment)
                    .padding(16.dp)
            ) {
                Text(
                    text = LanguageManager.text(
                        "Label Preview",
                        "ಲೇಬಲ್ ಪೂರ್ವವೀಕ್ಷಣೆ"
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = Terracotta
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✦ Handmade in Karnataka ✦",
                    style = MaterialTheme.typography.bodySmall,
                    color = HeritagGold
                )
                Text(
                    text = uiState.productName.ifEmpty {
                        LanguageManager.text(
                            "Product Name",
                            "ಉತ್ಪನ್ನದ ಹೆಸರು"
                        )
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepBrown
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "🪵 ${uiState.woodType.ifEmpty {
                            LanguageManager.text("Wood Type", "ಮರದ ವಿಧ")
                        }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MediumBrown
                    )
                    Text(
                        text = "₹ ${uiState.price.ifEmpty { "--" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MediumBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Apply Button ──────────────────────────
            Button(
                onClick = { viewModel.applyOverlayAndSave(imagePath) },
                enabled = !uiState.isProcessing &&
                        uiState.productName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    disabledContainerColor = LightSand
                )
            ) {
                if (uiState.isProcessing) {
                    CircularProgressIndicator(
                        color = WarmCream,
                        strokeWidth = 2.dp,
                        modifier = Modifier.height(20.dp)
                    )
                } else {
                    Text(
                        text = LanguageManager.text(
                            "Apply & Preview",
                            "ಅನ್ವಯಿಸಿ ಮತ್ತು ಪೂರ್ವವೀಕ್ಷಿಸಿ"
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmCream
                    )
                }
            }
        }
    }
}

// ── Reusable Text Field ───────────────────────────────
@Composable
fun LabelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = MediumBrown) },
        placeholder = {
            Text(
                text = placeholder,
                color = MediumBrown.copy(alpha = 0.5f)
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
            keyboardType = keyboardType
        ),
        singleLine = true
    )
}