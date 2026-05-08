package com.shilpakala.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.ui.components.ShilpaKalaTopBar
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    // ── Observe language changes ──────────────────────
    val currentLanguage by LanguageManager.currentLanguageFlow.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState.capturedImagePath) {
        uiState.capturedImagePath?.let { path ->
            val encoded = URLEncoder.encode(path, StandardCharsets.UTF_8.toString())
            navController.navigate(Screen.LabelEditor.createRoute(encoded))
            viewModel.clearCapturedImage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {

            // ── Camera Preview ────────────────────────
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onImageCaptureReady = { capture -> viewModel.imageCapture = capture },
                lifecycleOwner = lifecycleOwner
            )

            // ── Product Outline Overlay ───────────────
            ProductOverlay()

            // ── Top Bar ───────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                ShilpaKalaTopBar(
                    title = LanguageManager.text("Camera 📸", "ಕ್ಯಾಮೆರಾ 📸")
                )
                Text(
                    text = LanguageManager.text(
                        "📸 Place your product inside the frame",
                        "📸 ಉತ್ಪನ್ನವನ್ನು ಚೌಕಟ್ಟಿನೊಳಗೆ ಇರಿಸಿ"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmCream,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(8.dp)
                )
            }

            // ── Bottom Controls ───────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { viewModel.takePhoto() },
                    enabled = !uiState.isCapturing,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(if (uiState.isCapturing) Color.Gray else Terracotta)
                        .border(3.dp, WarmCream, CircleShape)
                ) {
                    Text(
                        text = if (uiState.isCapturing) "⏳" else "📷",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Text(
                    text = LanguageManager.text(
                        if (uiState.isCapturing) "Capturing..." else "Tap to capture",
                        if (uiState.isCapturing) "ಸೆರೆಹಿಡಿಯುತ್ತಿದೆ..." else "ಸೆರೆಹಿಡಿಯಲು ಒತ್ತಿರಿ"
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = WarmCream
                )
            }

        } else {
            // ── No Permission UI ──────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmCream)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "📷", style = MaterialTheme.typography.displayLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = LanguageManager.text(
                        "Camera permission is required",
                        "ಕ್ಯಾಮೆರಾ ಅನುಮತಿ ಅಗತ್ಯವಿದೆ"
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = Terracotta,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = LanguageManager.text(
                        "Please allow camera access to take product photos.",
                        "ಉತ್ಪನ್ನದ ಫೋಟೋ ತೆಗೆಯಲು ಕ್ಯಾಮೆರಾ ಪ್ರವೇಶವನ್ನು ಅನುಮತಿಸಿ."
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ── Camera Preview ────────────────────────────────────
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onImageCaptureReady: (ImageCapture) -> Unit,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()
                onImageCaptureReady(imageCapture)
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) { e.printStackTrace() }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = modifier
    )
}

// ── Product Outline Overlay ───────────────────────────
@Composable
fun ProductOverlay() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(280.dp, 280.dp)
                .border(
                    width = 2.dp,
                    color = HeritagGold.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}