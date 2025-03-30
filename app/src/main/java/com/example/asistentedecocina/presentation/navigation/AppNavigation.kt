package com.example.asistentedecocina.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.asistentedecocina.presentation.screens.home.HomeScreen
import com.example.asistentedecocina.presentation.screens.test.AnimationTestScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.AnimationTest.route // Temporalmente cambiamos la ruta inicial
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.AnimationTest.route) {
            AnimationTestScreen()
        }
        // Aquí agregaremos más rutas según las necesidades de la aplicación
    }
} 