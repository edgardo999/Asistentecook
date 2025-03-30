package com.example.asistentedecocina.core.model

data class RecipeCategory(
    val id: String,
    val name: String,
    val description: String,
    val icon: String, // Nombre del icono de Material Design
    val color: Long, // Color en formato ARGB
    val parentId: String? = null, // Para categorías anidadas
    val isFavorite: Boolean = false,
    val recipeCount: Int = 0
) 