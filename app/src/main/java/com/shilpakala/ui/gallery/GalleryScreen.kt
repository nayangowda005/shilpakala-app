package com.shilpakala.ui.gallery

import com.shilpakala.ui.components.ShilpaKalaTopBar
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.shilpakala.data.local.entity.PhotoEntity
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.DeepBrown
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.MediumBrown
import com.shilpakala.ui.theme.SoftParchment
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun GalleryScreen(
    navController: NavHostController,
    viewModel: GalleryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // ── Observe language changes ──────────────────────
    val currentLanguage by LanguageManager.currentLanguageFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // ── Header ────────────────────────────────────
        ShilpaKalaTopBar(
            title = LanguageManager.text(
                "My Gallery 🖼️",
                "ನನ್ನ ಗ್ಯಾಲರಿ 🖼️"
            )
        )

        // ── Content ───────────────────────────────────
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Terracotta)
                }
            }

            uiState.photos.isEmpty() -> {
                // ── Empty State ───────────────────────
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "🪵",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = LanguageManager.text(
                                "No photos yet!",
                                "ಇನ್ನೂ ಯಾವುದೇ ಫೋಟೋಗಳಿಲ್ಲ!"
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            color = Terracotta,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = LanguageManager.text(
                                "Take your first product photo\nto get started.",
                                "ಪ್ರಾರಂಭಿಸಲು ನಿಮ್ಮ ಮೊದಲ\nಉತ್ಪನ್ನದ ಫೋಟೋ ತೆಗೆಯಿರಿ."
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MediumBrown,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                // ── Photo Grid ────────────────────────
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.photos) { photo ->
                        PhotoGridItem(
                            photo = photo,
                            onClick = {
                                val encoded = URLEncoder.encode(
                                    photo.imagePath,
                                    StandardCharsets.UTF_8.toString()
                                )
                                navController.navigate(
                                    Screen.Preview.createRoute(encoded)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// ── Photo Grid Item ───────────────────────────────────
@Composable
fun PhotoGridItem(
    photo: PhotoEntity,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SoftParchment)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = Uri.parse(photo.imagePath),
            contentDescription = photo.productName,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                ),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = photo.productName,
                style = MaterialTheme.typography.labelLarge,
                color = DeepBrown,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "₹ ${photo.price}",
                style = MaterialTheme.typography.bodySmall,
                color = Terracotta
            )
        }
    }
}