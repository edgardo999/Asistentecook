package com.example.asistentedecocina.core.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: String
)

object MenuItems {
    val items = listOf(
        MenuItem(
            id = "home",
            title = "Inicio",
            icon = Icons.Default.Home,
            route = "home"
        ),
        MenuItem(
            id = "favorites",
            title = "Recetas Favoritas",
            icon = Icons.Default.Favorite,
            route = "favorites"
        ),
        MenuItem(
            id = "history",
            title = "Historial",
            icon = Icons.Default.History,
            route = "history"
        ),
        MenuItem(
            id = "settings",
            title = "Configuración",
            icon = Icons.Default.Settings,
            route = "settings"
        ),
        MenuItem(
            id = "about",
            title = "Acerca de",
            icon = Icons.Default.Info,
            route = "about"
        )
    )
} 