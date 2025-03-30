package com.example.asistentedecocina.core.model

data class RecipeStep(
    val id: String,
    val instruction: String,
    val duration: Int = 0, // Duración en minutos
    val ingredients: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
    val imageUrl: String? = null,
    val isOptional: Boolean = false,
    val requiresAttention: Boolean = false
) 