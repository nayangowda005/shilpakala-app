package com.shilpakala.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Camera : Screen("camera")
    object LabelEditor : Screen("label_editor/{imagePath}") {
        fun createRoute(imagePath: String) =
            "label_editor/${imagePath}"
    }
    object Preview : Screen("preview/{imagePath}") {
        fun createRoute(imagePath: String) =
            "preview/${imagePath}"
    }
    object Gallery : Screen("gallery")
}