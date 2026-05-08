package com.shilpakala.ui.guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shilpakala.data.preferences.LanguageManager
import com.shilpakala.ui.components.ShilpaKalaTopBar
import com.shilpakala.ui.navigation.Screen
import com.shilpakala.ui.theme.DeepBrown
import com.shilpakala.ui.theme.HeritagGold
import com.shilpakala.ui.theme.LightSand
import com.shilpakala.ui.theme.MediumBrown
import com.shilpakala.ui.theme.SoftParchment
import com.shilpakala.ui.theme.SuccessGreen
import com.shilpakala.ui.theme.Terracotta
import com.shilpakala.ui.theme.WarmCream
import kotlinx.coroutines.delay

data class GuideStep(
    val number: Int,
    val emoji: String,
    val titleEn: String,
    val titleKn: String,
    val instructionEn: String,
    val instructionKn: String,
    val whyEn: String,
    val whyKn: String,
    val hintEn: String,
    val hintKn: String
)

@Composable
fun CameraGuideScreen(navController: NavHostController) {
    var currentStep by remember { mutableStateOf(1) }

    val steps = listOf(
        GuideStep(
            number = 1,
            emoji = "🧹",
            titleEn = "Clean Your Product",
            titleKn = "ನಿಮ್ಮ ಉತ್ಪನ್ನವನ್ನು ಶುಚ್ಛ ಮಾಡಿ",
            instructionEn = "Clean your product and place it on a plain white or neutral cloth",
            instructionKn = "ನಿಮ್ಮ ಉತ್ಪನ್ನವನ್ನು ಸ್ವಚ್ಛ ಮಾಡಿ ಮತ್ತು ಬಿಳುಪುಟ್ಟ ಮೇಲ್ಮೈಯಲ್ಲಿ ಇರಿಸಿ",
            whyEn = "Dust and dirt make your work look cheap. A clean product = luxury perception",
            whyKn = "ಧೂಳು ಮತ್ತು ಕೆಸರು ನಿಮ್ಮ ಕೆಲಸವನ್ನು ಸಿಸ್ತಾಗಿ ಕಾಣುವಂತೆ ಮಾಡುತ್ತದೆ",
            hintEn = "Use a white cloth or plain surface for best results",
            hintKn = "ಉತ್ತಮ ಫಲಿತಾಂಶಕ್ಕಾಗಿ ಬಿಳುಪುಟ್ಟ ಬಟ್ಟೆ ಬಳಸಿ"
        ),
        GuideStep(
            number = 2,
            emoji = "📱",
            titleEn = "Hold Phone Straight",
            titleKn = "ಫೋನ್ ಸರಳವಾಗಿ ಹಿಡಿದುಕೊಳ್ಳಿ",
            instructionEn = "Hold your phone parallel to the product. Not tilted up or down. Keep it steady!",
            instructionKn = "ನಿಮ್ಮ ಫೋನ್ ಅನ್ನು ಸಮಾನಾಂತರವಾಗಿ ಹಿಡಿದುಕೊಳ್ಳಿ. ಮೇಲೆ ಅಥವಾ ಕೆಳಗೆ ತಿರುವಿಸಬೇಡಿ",
            whyEn = "Tilted angles make products look distorted and unprofessional",
            whyKn = "ಕೋನದ ಫೋಟೋ ಉತ್ಪನ್ನವನ್ನು ವಿಚಿತ್ರವಾಗಿ ಕಾಣುವಂತೆ ಮಾಡುತ್ತದೆ",
            hintEn = "Eye-level photography = Professional look",
            hintKn = "ಕಣ್ಣದ ಮಟ್ಟದ ಫೋಟೋ = ವೃತ್ತಿಪರ ಮೌಲ್ಯ"
        ),
        GuideStep(
            number = 3,
            emoji = "💡",
            titleEn = "Check Your Lighting",
            titleKn = "ನಿಮ್ಮ ಬೆಳಕನ್ನು ಪರಿಶೀಲಿಸಿ",
            instructionEn = "Use natural light from a window. Avoid harsh shadows on your product",
            instructionKn = "ಕಿಟಕಿಯಿಂದ ನೈಸರ್ಗಿಕ ಬೆಳಕನ್ನು ಬಳಸಿ. ತೀವ್ರ ನೆರಳುಗಳನ್ನು ತಪ್ಪಿಸಿ",
            whyEn = "Bad lighting kills luxury perception. Professional photos are bright and clear",
            whyKn = "ಕೆಟ್ಟ ಬೆಳಕು ಪ್ರಿಮಿಯಮ್ ಮೌಲ್ಯವನ್ನು ನಾಶ ಮಾಡುತ್ತದೆ",
            hintEn = "Shoot near a window during daytime for perfect lighting",
            hintKn = "ದಿನದ ವೇಳೆ ಕಿಟಕಿಯ ಬಳಿ ಚಿತ್ರ ತೆಗೆಯಿರಿ"
        ),
        GuideStep(
            number = 4,
            emoji = "📐",
            titleEn = "Center In The Frame",
            titleKn = "ಚೌಕಟ್ಟಿನ ಮಧ್ಯದಲ್ಲಿ ಇರಿಸಿ",
            instructionEn = "Position your product in the center of the frame. Leave equal space on all sides",
            instructionKn = "ಉತ್ಪನ್ನವನ್ನು ಚೌಕಟ್ಟಿನ ಮಧ್ಯದಲ್ಲಿ ಇರಿಸಿ. ಎಲ್ಲೆಡೆ ಸಮಾನ ಜಾಗ ಬಿಡಿ",
            whyEn = "Centered composition looks professional. Balanced frame = high-value perception",
            whyKn = "ಮಧ್ಯದಲ್ಲಿ ಇರುವ ಸಂಯೋಜನೆ ವೃತ್ತಿಪರವಾಗಿ ಕಾಣುತ್ತದೆ",
            hintEn = "Use the camera grid to align your product perfectly",
            hintKn = "ನಿಖುಂತವಾಗಿ ಜೋಡಿಸಲು ಕ್ಯಾಮೆರಾ ಗ್ರಿಡ್ ಬಳಸಿ"
        ),
        GuideStep(
            number = 5,
            emoji = "✅",
            titleEn = "Ready To Capture!",
            titleKn = "ಸೆರೆಹಿಡಿಯಲು ಸಿದ್ಧವಾಗಿದೆ!",
            instructionEn = "You're all set! You've learned professional photography tips. Ready to capture?",
            instructionKn = "ನೀವು ಸಿದ್ಧವಾಗಿದ್ದೀರಿ! ವೃತ್ತಿಪರ ಫೋಟೋಗ್ರಫಿ ಕಲಿತಿದ್ದೀರಿ.",
            whyEn = "You now understand professional product photography!",
            whyKn = "ನೀವು ವೃತ್ತಿಪರ ಉತ್ಪನ್ನ ಫೋಟೋಗ್ರಫಿ ಅರ್ಥ ಮಾಡಿಕೊಂಡಿದ್ದೀರಿ!",
            hintEn = "Tap the button below to open your camera",
            hintKn = "ನಿಮ್ಮ ಕ್ಯಾಮೆರಾ ತೆರೆಯಲು ಕೆಳಗೆ ಬಟನ್ ಒತ್ತಿರಿ"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // ── Top Bar ───────────────────────────────────
        ShilpaKalaTopBar(
            title = LanguageManager.text(
                "Camera Guide 📸",
                "ಕ್ಯಾಮೆರಾ ಮಾರ್ಗದರ್ಶನ 📸"
            )
        )

        val step = steps[currentStep - 1]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Progress Bar ──────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LanguageManager.text(
                            "Step $currentStep of 5",
                            "ಹಂತ $currentStep / 5"
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MediumBrown
                    )
                    Text(
                        text = "${(currentStep * 20)}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = Terracotta
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { currentStep / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Terracotta,
                    trackColor = LightSand
                )
            }

            // ── Step Content ──────────────────────────
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally() + fadeIn(),
                exit = slideOutHorizontally() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SoftParchment)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Emoji
                    Text(
                        text = step.emoji,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.size(64.dp)
                    )

                    // Title
                    Text(
                        text = LanguageManager.text(step.titleEn, step.titleKn),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Terracotta,
                        textAlign = TextAlign.Center
                    )

                    // Instruction
                    Text(
                        text = LanguageManager.text(
                            step.instructionEn,
                            step.instructionKn
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepBrown,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Animation Area ─────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .border(2.dp, LightSand, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (currentStep) {
                    1 -> AnimationStep1()
                    2 -> AnimationStep2()
                    3 -> AnimationStep3()
                    4 -> AnimationStep4()
                    5 -> AnimationStep5()
                }
            }

            // ── Why & Hint Box ─────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Yellow.copy(alpha = 0.1f))
                    .border(1.dp, HeritagGold, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡 ${LanguageManager.text("Why?", "ಏಕೆ?")}",
                    style = MaterialTheme.typography.labelMedium,
                    color = HeritagGold
                )
                Text(
                    text = LanguageManager.text(step.whyEn, step.whyKn),
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepBrown
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✨ ${LanguageManager.text("Tip", "ಸಲಹೆ")}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Terracotta
                )
                Text(
                    text = LanguageManager.text(step.hintEn, step.hintKn),
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepBrown
                )
            }

            // ── Navigation Buttons ─────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Back Button
                OutlinedButton(
                    onClick = {
                        if (currentStep > 1) currentStep--
                    },
                    enabled = currentStep > 1,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (currentStep > 1) Terracotta else LightSand
                    )
                ) {
                    Text(
                        text = LanguageManager.text("← Back", "← ಹಿಂದಕ್ಕೆ"),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (currentStep > 1) Terracotta else LightSand
                    )
                }

                // Next/Open Camera Button
                if (currentStep < 5) {
                    Button(
                        onClick = { currentStep++ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Terracotta
                        )
                    ) {
                        Text(
                            text = LanguageManager.text("Next →", "ಮುಂದೆ →"),
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmCream
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            navController.navigate(Screen.Camera.route) {
                                popUpTo(Screen.Home.route)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessGreen
                        )
                    ) {
                        Text(
                            text = LanguageManager.text(
                                "📸 Open Camera",
                                "📸 ಕ್ಯಾಮೆರಾ ತೆರೆಯಿರಿ"
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmCream
                        )
                    }
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
// ANIMATION COMPOSABLES
// ════════════════════════════════════════════════════════════

@Composable
fun AnimationStep1() {
    var showClean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showClean = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dirty Product
        AnimatedVisibility(
            visible = !showClean,
            exit = slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🪵💨",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = LanguageManager.text("Dirty", "ಕೆಸರಿನ"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MediumBrown
                )
            }
        }

        // Clean Product
        AnimatedVisibility(
            visible = showClean,
            enter = slideInHorizontally(initialOffsetX = { 300 }) + fadeIn()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🪵✨",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = LanguageManager.text("Clean!", "ಶುಚ್ಛ!"),
                    style = MaterialTheme.typography.labelMedium,
                    color = SuccessGreen
                )
            }
        }
    }
}

@Composable
fun AnimationStep2() {
    var tiltAngle by remember { mutableStateOf(0f) }
    var isWrong by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            // Show wrong angles
            tiltAngle = 30f
            delay(800)
            tiltAngle = -30f
            delay(800)
            tiltAngle = 0f
            isWrong = false
            delay(1000)
            isWrong = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Phone Animation
        Box(
            modifier = Modifier
                .size(60.dp, 100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DeepBrown)
                .border(2.dp, HeritagGold, RoundedCornerShape(8.dp))
                .rotate(tiltAngle)
        )

        // Status
        Text(
            text = if (isWrong) {
                LanguageManager.text("❌ Tilted", "❌ ಕೋನದಲ್ಲಿ")
            } else {
                LanguageManager.text("✓ Straight!", "✓ ಸರಳ!")
            },
            style = MaterialTheme.typography.labelMedium,
            color = if (isWrong) Color.Red else SuccessGreen
        )
    }
}

@Composable
fun AnimationStep3() {
    var lightIntensity by remember { mutableStateOf(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            lightIntensity = 0.3f // dark
            delay(1000)
            lightIntensity = 1f // bright
            delay(1500)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Product with changing brightness
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Color.Yellow.copy(alpha = lightIntensity)
                )
        )

        Text(
            text = "💡",
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            text = if (lightIntensity > 0.6f) {
                LanguageManager.text("Good light!", "ಸುಲಭವಾದ ಬೆಳಕು!")
            } else {
                LanguageManager.text("Too dark", "ತುಂಬಾ ಕತ್ತಲೆ")
            },
            style = MaterialTheme.typography.labelMedium,
            color = if (lightIntensity > 0.6f) SuccessGreen else Color.DarkGray
        )
    }
}

@Composable
fun AnimationStep4() {
    var frameOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            frameOffset = 300f
            delay(500)
            frameOffset = 0f
            delay(1500)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Dotted Frame
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(
                    width = 3.dp,
                    color = HeritagGold,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = (frameOffset / 30).dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = LanguageManager.text("Centered!", "ಮಧ್ಯದಲ್ಲಿ!"),
            style = MaterialTheme.typography.labelMedium,
            color = SuccessGreen
        )
    }
}

@Composable
fun AnimationStep5() {
    var pulseScale by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            pulseScale = 1f
            delay(500)
            pulseScale = 1.1f
            delay(500)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Checkmark
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.2f))
                .border(3.dp, SuccessGreen, CircleShape)
        ) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.displayLarge,
                color = SuccessGreen,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            text = LanguageManager.text(
                "Perfect! You're ready!",
                "ನಿಖುಂತ! ನೀವು ಸಿದ್ಧವಾಗಿದ್ದೀರಿ!"
            ),
            style = MaterialTheme.typography.titleMedium,
            color = SuccessGreen,
            textAlign = TextAlign.Center
        )
    }
}