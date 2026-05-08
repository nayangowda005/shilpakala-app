package com.shilpakala.ui.preview

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PreviewUiState(
    val isSharing: Boolean = false
)

class PreviewViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState: StateFlow<PreviewUiState> = _uiState

    // ── Share Image ───────────────────────────────────
    fun shareImage(imageUri: String) {
        val context = getApplication<Application>()
        val uri = Uri.parse(imageUri)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(
                Intent.EXTRA_TEXT,
                "✦ Handmade in Karnataka ✦\n" +
                        "Check out this beautiful handicraft!\n" +
                        "Made with love by skilled artisans.\n" +
                        "#ShilpaKala #MadeInIndia #HandmadeKarnataka"
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(
            Intent.createChooser(shareIntent, "Share via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}