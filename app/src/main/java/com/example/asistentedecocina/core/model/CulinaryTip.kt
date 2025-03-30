package com.example.asistentedecocina.core.model

data class CulinaryTip(
    val id: String,
    val title: String,
    val content: String,
    val category: TipCategory,
    val difficulty: TipDifficulty,
    val tags: List<String> = emptyList(),
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val lastShown: Long = 0
)

enum class TipCategory {
    TECNICAS_BASICAS,
    INGREDIENTES,
    EQUIPAMIENTO,
    SEGURIDAD,
    CONSERVACION,
    PRESENTACION,
    NUTRICION,
    GENERAL
}

enum class TipDifficulty {
    PRINCIPIANTE,
    INTERMEDIO,
    AVANZADO
} 