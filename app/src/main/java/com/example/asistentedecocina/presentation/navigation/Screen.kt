package com.example.asistentedecocina.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AnimationTest : Screen("animation_test")
    // Aquí agregaremos más rutas según las necesidades de la aplicación
} 