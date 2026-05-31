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
import com.shilpakala.data.local.ShilpaKalaDatabase
import com.shilpakala.data.local.entity.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

data class LabelEditorUiState(
    val productName: String = "",
    val woodType: String = "",
    val price: String = "",
    val isProcessing: Boolean = false,
    val savedImagePath: String? = null,
    val error: String? = null
)

class LabelEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ShilpaKalaDatabase.getDatabase(application)
    private val photoDao = db.photoDao()

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
    private fun processImage(imageUri: String): String {
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

        // ── Handmade in Karnataka Badge ──────────────
        drawHandmadeBadge(
            canvas = canvas,
            x = width * 0.15f,
            y = labelTop + labelHeight * 0.28f,
            size = width * 0.12f
        )

        // ── Product Name ──────────────────────────────
        val productPaint = Paint().apply {
            color = Color.WHITE
            textSize = width * 0.055f
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            isAntiAlias = true
        }
        val productText = _uiState.value.productName.ifEmpty { "Handicraft Product" }
        val productX = width * 0.35f
        val productY = labelTop + labelHeight * 0.52f
        canvas.drawText(productText, productX, productY, productPaint)

        // ── Wood Type + Price Row ─────────────────────
        val detailPaint = Paint().apply {
            color = Color.rgb(255, 248, 240) // Warm Cream
            textSize = width * 0.038f
            isAntiAlias = true
        }

        val woodText = "🪵 ${_uiState.value.woodType.ifEmpty { "Natural Wood" }}"
        canvas.drawText(woodText, width * 0.35f, labelTop + labelHeight * 0.76f, detailPaint)

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
            width * 0.35f,
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
        )

        context.contentResolver.openOutputStream(uri!!)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        // ── Save to Room DB ───────────────────────────
        viewModelScope.launch {
            photoDao.insertPhoto(
                PhotoEntity(
                    imagePath = uri.toString(),
                    productName = _uiState.value.productName,
                    woodType = _uiState.value.woodType,
                    price = _uiState.value.price
                )
            )
        }

        return uri.toString()
    }

    // ── Draw Handmade Badge ────────────────────────
    private fun drawHandmadeBadge(
        canvas: Canvas,
        x: Float,
        y: Float,
        size: Float
    ) {
        // Outer circle (Terracotta)
        val outerPaint = Paint().apply {
            color = Color.rgb(192, 82, 42) // Terracotta
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(x, y, size, outerPaint)

        // Inner circle (Heritage Gold)
        val innerPaint = Paint().apply {
            color = Color.rgb(212, 160, 23) // Heritage Gold
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(x, y, size * 0.85f, innerPaint)

        // White border (decorative)
        val borderPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = size * 0.08f
            isAntiAlias = true
        }
        canvas.drawCircle(x, y, size * 0.88f, borderPaint)

        // "Handmade in Karnataka" text
        val textPaint = Paint().apply {
            color = Color.rgb(192, 82, 42) // Terracotta
            textSize = size * 0.25f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }

        // Top text: "Handmade"
        val topText = "Handmade"
        val topX = x - (textPaint.measureText(topText) / 2)
        val topY = y - (size * 0.2f)
        canvas.drawText(topText, topX, topY, textPaint)

        // Bottom text: "in Karnataka"
        val bottomText = "in Karnataka"
        val bottomX = x - (textPaint.measureText(bottomText) / 2)
        val bottomY = y + (size * 0.5f)
        canvas.drawText(bottomText, bottomX, bottomY, textPaint)

        // Center star ✦
        val starPaint = Paint().apply {
            color = Color.WHITE
            textSize = size * 0.4f
            isAntiAlias = true
        }
        val star = "✦"
        val starX = x - (starPaint.measureText(star) / 2)
        val starY = y + (size * 0.12f)
        canvas.drawText(star, starX, starY, starPaint)
    }
}