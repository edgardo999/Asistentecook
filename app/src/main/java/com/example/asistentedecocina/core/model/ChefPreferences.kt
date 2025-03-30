package com.example.asistentedecocina.core.model

import androidx.compose.ui.graphics.Color

data class ChefPreferences(
    val style: ChefStyle = ChefStyle.CLASSIC,
    val animationSpeed: Float = 1.0f,
    val primaryColor: Color = Color(0xFF6200EE),
    val secondaryColor: Color = Color(0xFF03DAC5),
    val hatColor: Color = Color.White,
    val apronColor: Color = Color.White,
    val expressiveness: Float = 1.0f, // Factor de intensidad de las expresiones
    val size: Float = 1.0f // Factor de escala del chef
)

enum class ChefStyle {
    CLASSIC,      // Estilo clásico tradicional
    MODERN,       // Estilo moderno y minimalista
    CARTOON,      // Estilo caricaturesco
    PIXEL,        // Estilo pixel art
    WATERCOLOR    // Estilo acuarela
}

// Paletas de colores predefinidas
object ChefColorPalettes {
    val Classic = ChefPreferences(
        primaryColor = Color(0xFF6200EE),
        secondaryColor = Color(0xFF03DAC5),
        hatColor = Color.White,
        apronColor = Color.White
    )

    val Modern = ChefPreferences(
        primaryColor = Color(0xFF1A73E8),
        secondaryColor = Color(0xFF34A853),
        hatColor = Color(0xFFF1F3F4),
        apronColor = Color(0xFFF1F3F4)
    )

    val Playful = ChefPreferences(
        primaryColor = Color(0xFFFF4081),
        secondaryColor = Color(0xFF00BCD4),
        hatColor = Color(0xFFFFEB3B),
        apronColor = Color(0xFFE1BEE7)
    )

    val Natural = ChefPreferences(
        primaryColor = Color(0xFF795548),
        secondaryColor = Color(0xFF8BC34A),
        hatColor = Color(0xFFEFEBE9),
        apronColor = Color(0xFFD7CCC8)
    )

    val Dark = ChefPreferences(
        primaryColor = Color(0xFF121212),
        secondaryColor = Color(0xFF2196F3),
        hatColor = Color(0xFF424242),
        apronColor = Color(0xFF212121)
    )
} 