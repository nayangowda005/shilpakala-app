package com.shilpakala.ui.navigation

import com.shilpakala.ui.gallery.GalleryScreen
import com.shilpakala.ui.preview.PreviewScreen
import com.shilpakala.ui.editor.LabelEditorScreen
import com.shilpakala.ui.camera.CameraScreen
import com.shilpakala.ui.home.HomeScreen
import com.shilpakala.ui.onboarding.OnboardingScreen
import com.shilpakala.ui.splash.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ── Splash ──────────────────────────────────
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // ── Onboarding ───────────────────────────────
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        // ── Home ─────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // ── Camera ───────────────────────────────────
        composable(Screen.Camera.route) {
            CameraScreen(navController = navController)
        }

        // ── Label Editor ─────────────────────────────
        composable(
            route = Screen.LabelEditor.route,
            arguments = listOf(
                navArgument("imagePath") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val imagePath = backStackEntry.arguments?.getString("imagePath") ?: ""
            LabelEditorScreen(
                navController = navController,
                encodedImagePath = imagePath
            )
        }

        // ── Preview ──────────────────────────────────
        composable(
            route = Screen.Preview.route,
            arguments = listOf(
                navArgument("imagePath") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val imagePath = backStackEntry.arguments?.getString("imagePath") ?: ""
            PreviewScreen(
                navController = navController,
                encodedImagePath = imagePath
            )
        }

        // ── Gallery ──────────────────────────────────
        composable(Screen.Gallery.route) {
            GalleryScreen(navController = navController)
        }
    }
}