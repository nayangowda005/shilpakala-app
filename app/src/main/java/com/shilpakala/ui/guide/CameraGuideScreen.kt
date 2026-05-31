package com.shilpakala.ui.guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
    val currentStep = remember { mutableIntStateOf(1) }

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
        ShilpaKalaTopBar(
            title = LanguageManager.text(
                "Camera Guide 📸",
                "ಕ್ಯಾಮೆರಾ ಮಾರ್ಗದರ್ಶನ 📸"
            )
        )

        val step = steps[currentStep.value - 1]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LanguageManager.text(
                            "Step ${currentStep.value} of 5",
                            "ಹಂತ ${currentStep.value} / 5"
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MediumBrown
                    )
                    Text(
                        text = "${currentStep.value * 20}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = Terracotta
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { currentStep.value / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Terracotta,
                    trackColor = LightSand
                )
            }

            // Step Content
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
                    Text(
                        text = step.emoji,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = LanguageManager.text(step.titleEn, step.titleKn),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Terracotta,
                        textAlign = TextAlign.Center
                    )
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

            // Animation Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .border(2.dp, LightSand, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (currentStep.value) {
                    1 -> AnimationStep1()
                    2 -> AnimationStep2()
                    3 -> AnimationStep3()
                    4 -> AnimationStep4()
                    5 -> AnimationStep5()
                }
            }

            // Why & Hint Box
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

            // Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { if (currentStep.value > 1) currentStep.value-- },
                    enabled = currentStep.value > 1,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = LanguageManager.text("← Back", "← ಹಿಂದಕ್ಕೆ"),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (currentStep.value > 1) Terracotta else LightSand
                    )
                }

                if (currentStep.value < 5) {
                    Button(
                        onClick = { currentStep.value++ },
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

@Composable
fun AnimationStep1() {
    val showClean = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showClean.value = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = !showClean.value,
            exit = slideOutHorizontally(targetOffsetX = { -300 }) + fadeOut()
        ) {
            Text(text = "🪵💨", style = MaterialTheme.typography.displayLarge)
        }

        AnimatedVisibility(
            visible = showClean.value,
            enter = slideInHorizontally(initialOffsetX = { 300 }) + fadeIn()
        ) {
            Text(text = "🪵✨", style = MaterialTheme.typography.displayLarge)
        }
    }
}

@Composable
fun AnimationStep2() {
    val tiltAngle = remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            tiltAngle.value = 30f
            delay(800)
            tiltAngle.value = -30f
            delay(800)
            tiltAngle.value = 0f
            delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp, 100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DeepBrown)
                .border(2.dp, HeritagGold, RoundedCornerShape(8.dp))
                .rotate(tiltAngle.value)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "📱", style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
fun AnimationStep3() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Yellow.copy(alpha = 0.7f))
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "💡", style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
fun AnimationStep4() {
    val frameOffsetX = remember { mutableStateOf(300f) }

    LaunchedEffect(Unit) {
        while (true) {
            frameOffsetX.value = 300f
            delay(500)
            frameOffsetX.value = 0f
            delay(1500)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .border(
                    width = 3.dp,
                    color = HeritagGold,
                    shape = RoundedCornerShape(8.dp)
                )
                .offset(x = (frameOffsetX.value / 30).dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "📐", style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
fun AnimationStep5() {
    val pulseScale = remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            pulseScale.value = 1f
            delay(500)
            pulseScale.value = 1.1f
            delay(500)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.2f))
                .border(3.dp, SuccessGreen, CircleShape)
                .scale(pulseScale.value)
        ) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.displayLarge,
                color = SuccessGreen,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "✅", style = MaterialTheme.typography.displaySmall)
    }
}