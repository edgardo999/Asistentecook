package com.example.asistentedecocina.core.search

import com.example.asistentedecocina.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

data class SearchFilters(
    val categories: Set<String> = emptySet(),
    val difficulty: Set<String> = emptySet(),
    val cookingTime: IntRange = 0..Int.MAX_VALUE,
    val servings: IntRange = 1..Int.MAX_VALUE,
    val ingredients: Set<String> = emptySet(),
    val excludeIngredients: Set<String> = emptySet(),
    val isVegetarian: Boolean? = null,
    val isVegan: Boolean? = null,
    val isGlutenFree: Boolean? = null,
    val sortBy: SortOption = SortOption.NAME
)

enum class SortOption {
    NAME,
    COOKING_TIME,
    DIFFICULTY,
    RATING,
    DATE_ADDED
}

class RecipeSearchManager {
    private val _searchQuery = MutableStateFlow("")
    private val _filters = MutableStateFlow(SearchFilters())
    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())

    val searchQuery: Flow<String> = _searchQuery.asStateFlow()
    val filters: Flow<SearchFilters> = _filters.asStateFlow()
    val searchResults: Flow<List<Recipe>> = _searchResults.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        performSearch()
    }

    fun updateFilters(newFilters: SearchFilters) {
        _filters.value = newFilters
        performSearch()
    }

    private fun performSearch() {
        val query = _searchQuery.value.lowercase()
        val currentFilters = _filters.value

        // Aquí implementaremos la lógica de búsqueda real cuando tengamos acceso a la base de datos
        // Por ahora, simulamos la búsqueda
        val filteredRecipes = emptyList<Recipe>() // TODO: Implementar búsqueda real
            .filter { recipe ->
                // Filtro por texto de búsqueda
                val matchesQuery = query.isEmpty() || 
                    recipe.name.lowercase().contains(query) ||
                    recipe.description.lowercase().contains(query) ||
                    recipe.ingredients.any { it.lowercase().contains(query) }

                // Filtro por categorías
                val matchesCategories = currentFilters.categories.isEmpty() ||
                    recipe.categories.any { it in currentFilters.categories }

                // Filtro por dificultad
                val matchesDifficulty = currentFilters.difficulty.isEmpty() ||
                    recipe.difficulty in currentFilters.difficulty

                // Filtro por tiempo de cocción
                val matchesCookingTime = recipe.cookingTime in currentFilters.cookingTime

                // Filtro por porciones
                val matchesServings = recipe.servings in currentFilters.servings

                // Filtro por ingredientes
                val matchesIngredients = currentFilters.ingredients.isEmpty() ||
                    currentFilters.ingredients.all { ingredient ->
                        recipe.ingredients.any { it.lowercase().contains(ingredient.lowercase()) }
                    }

                // Filtro por ingredientes excluidos
                val matchesExcludeIngredients = currentFilters.excludeIngredients.isEmpty() ||
                    currentFilters.excludeIngredients.none { ingredient ->
                        recipe.ingredients.any { it.lowercase().contains(ingredient.lowercase()) }
                    }

                // Filtros dietéticos
                val matchesDietary = when {
                    currentFilters.isVegetarian != null -> recipe.isVegetarian == currentFilters.isVegetarian
                    currentFilters.isVegan != null -> recipe.isVegan == currentFilters.isVegan
                    currentFilters.isGlutenFree != null -> recipe.isGlutenFree == currentFilters.isGlutenFree
                    else -> true
                }

                matchesQuery && matchesCategories && matchesDifficulty && 
                matchesCookingTime && matchesServings && matchesIngredients && 
                matchesExcludeIngredients && matchesDietary
            }
            .sortedWith(when (currentFilters.sortBy) {
                SortOption.NAME -> compareBy { it.name }
                SortOption.COOKING_TIME -> compareBy { it.cookingTime }
                SortOption.DIFFICULTY -> compareBy { it.difficulty }
                SortOption.RATING -> compareByDescending { it.rating }
                SortOption.DATE_ADDED -> compareByDescending { it.dateAdded }
            })

        _searchResults.value = filteredRecipes
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _filters.value = SearchFilters()
        performSearch()
    }

    fun clearFilters() {
        _filters.value = SearchFilters()
        performSearch()
    }
} 