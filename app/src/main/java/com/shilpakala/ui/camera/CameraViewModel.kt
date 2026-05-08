package com.shilpakala.ui.camera

import android.app.Application
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class CameraUiState(
    val isCapturing: Boolean = false,
    val capturedImagePath: String? = null,
    val error: String? = null
)

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    var imageCapture: ImageCapture? = null

    // ── Take Photo ────────────────────────────────────
    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        _uiState.value = _uiState.value.copy(isCapturing = true)

        val context = getApplication<Application>()
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "ShilpaKala_$name")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/ShilpaKala"
                )
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri?.toString() ?: ""
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(
                            isCapturing = false,
                            capturedImagePath = savedUri
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(
                            isCapturing = false,
                            error = exception.message
                        )
                    }
                }
            }
        )
    }

    fun clearCapturedImage() {
        _uiState.value = _uiState.value.copy(capturedImagePath = null)
    }
}