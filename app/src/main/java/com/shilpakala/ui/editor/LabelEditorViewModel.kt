package com.shilpakala.ui.editor

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.io.IOException

data class LabelEditorUiState(
    val productName: String = "",
    val woodType: String = "",
    val price: String = "",
    val isProcessing: Boolean = false,
    val savedImagePath: String? = null,
    val error: String? = null
)

class LabelEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LabelEditorUiState())
    val uiState: StateFlow<LabelEditorUiState> = _uiState

    fun onProductNameChange(name: String) {
        _uiState.value = _uiState.value.copy(productName = name)
    }

    fun onWoodTypeChange(type: String) {
        _uiState.value = _uiState.value.copy(woodType = type)
    }

    fun onPriceChange(price: String) {
        _uiState.value = _uiState.value.copy(price = price)
    }

    fun clearSavedImage() {
        _uiState.value = _uiState.value.copy(savedImagePath = null)
    }

    // ── Apply Overlay and Save ────────────────────────
    fun applyOverlayAndSave(imageUri: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true)
            try {
                val resultPath = withContext(Dispatchers.IO) {
                    processImage(imageUri)
                }
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    savedImagePath = resultPath
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    error = e.message
                )
            }
        }
    }

    // ── Core Image Processing ─────────────────────────
    private suspend fun processImage(imageUri: String): String {
        val context = getApplication<Application>()

        // Load original bitmap
        val inputStream = context.contentResolver.openInputStream(Uri.parse(imageUri))
        val original = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // Create mutable copy
        val bitmap = original.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()

        // ── Brand Label Background ────────────────────
        val labelHeight = height * 0.22f
        val labelTop = height - labelHeight
        val labelPaint = Paint().apply {
            color = Color.argb(210, 192, 82, 42) // Terracotta semi-transparent
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, labelTop, width, height, labelPaint)

        // ── "Handmade in Karnataka" Text ──────────────
        val brandPaint = Paint().apply {
            color = Color.rgb(212, 160, 23) // Heritage Gold
            textSize = width * 0.045f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        val brandText = "✦ Handmade in Karnataka ✦"
        val brandX = (width - brandPaint.measureText(brandText)) / 2
        canvas.drawText(brandText, brandX, labelTop + labelHeight * 0.28f, brandPaint)

        // ── Product Name ──────────────────────────────
        val productPaint = Paint().apply {
            color = Color.WHITE
            textSize = width * 0.055f
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            isAntiAlias = true
        }
        val productText = _uiState.value.productName.ifEmpty { "Handicraft Product" }
        val productX = (width - productPaint.measureText(productText)) / 2
        canvas.drawText(productText, productX, labelTop + labelHeight * 0.52f, productPaint)

        // ── Wood Type + Price Row ─────────────────────
        val detailPaint = Paint().apply {
            color = Color.rgb(255, 248, 240) // Warm Cream
            textSize = width * 0.038f
            isAntiAlias = true
        }

        val woodText = "🪵 ${_uiState.value.woodType.ifEmpty { "Natural Wood" }}"
        canvas.drawText(woodText, width * 0.06f, labelTop + labelHeight * 0.76f, detailPaint)

        val priceText = "₹ ${_uiState.value.price.ifEmpty { "--" }}"
        val priceX = width - detailPaint.measureText(priceText) - width * 0.06f
        canvas.drawText(priceText, priceX, labelTop + labelHeight * 0.76f, detailPaint)

        // ── Divider Line ──────────────────────────────
        val linePaint = Paint().apply {
            color = Color.rgb(212, 160, 23)
            strokeWidth = 2f
            alpha = 150
        }
        canvas.drawLine(
            width * 0.06f,
            labelTop + labelHeight * 0.62f,
            width * 0.94f,
            labelTop + labelHeight * 0.62f,
            linePaint
        )

        // ── Save Result ───────────────────────────────
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "SK_Branded_$name")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/ShilpaKala"
                )
            }
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: throw IOException("Failed to create image file URI")

        context.contentResolver.openOutputStream(uri)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        // Save to Room DB
        val db = com.shilpakala.data.local.ShilpaKalaDatabase.getDatabase(context)
        val photo = com.shilpakala.data.local.entity.PhotoEntity(
            imagePath = uri.toString(),
            productName = _uiState.value.productName,
            woodType = _uiState.value.woodType,
            price = _uiState.value.price
        )
        db.photoDao().insertPhoto(photo)

        return uri.toString()
    }
}