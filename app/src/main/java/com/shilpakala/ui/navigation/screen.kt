package com.shilpakala.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Gallery : Screen("gallery")

    data class LabelEditor(val imagePath: String) : Screen("label_editor/{imagePath}") {
        companion object {
            const val route = "label_editor/{imagePath}"
            fun createRoute(imagePath: String) = "label_editor/${imagePath}"
        }
    }

    data class Preview(val imagePath: String) : Screen("preview/{imagePath}") {
        companion object {
            const val route = "preview/{imagePath}"
            fun createRoute(imagePath: String) = "preview/${imagePath}"
        }
    }

    object CameraGuide : Screen("camera_guide")
}