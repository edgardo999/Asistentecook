package com.example.asistentedecocina.presentation.screens.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentedecocina.data.local.AppDatabase
import com.example.asistentedecocina.data.local.entity.RecipeCategory
import com.example.asistentedecocina.data.local.entity.RecipeDifficulty
import com.example.asistentedecocina.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val recipeDao = database.recipeDao()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<RecipeCategory?>(null)
    private val _selectedDifficulty = MutableStateFlow<RecipeDifficulty?>(null)
    private val _showOnlyFavorites = MutableStateFlow(false)

    val recipes: StateFlow<List<RecipeEntity>> = combine(
        _searchQuery,
        _selectedCategory,
        _selectedDifficulty,
        _showOnlyFavorites
    ) { query, category, difficulty, onlyFavorites ->
        when {
            onlyFavorites -> recipeDao.getFavoriteRecipes()
            query.isNotEmpty() -> recipeDao.searchRecipes(query)
            category != null -> recipeDao.getRecipesByCategory(category)
            difficulty != null -> recipeDao.getRecipesByDifficulty(difficulty)
            else -> recipeDao.getAllRecipes()
        }
    }.flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: RecipeCategory?) {
        _selectedCategory.value = category
    }

    fun setDifficulty(difficulty: RecipeDifficulty?) {
        _selectedDifficulty.value = difficulty
    }

    fun toggleFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun toggleFavorite(recipeId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            recipeDao.updateFavoriteStatus(recipeId, isFavorite)
        }
    }

    fun addRecipe(
        title: String,
        description: String,
        preparationTime: Int,
        servings: Int,
        difficulty: RecipeDifficulty,
        imageUrl: String?,
        ingredients: List<String>,
        instructions: List<String>,
        category: RecipeCategory
    ) {
        viewModelScope.launch {
            val recipe = RecipeEntity(
                title = title,
                description = description,
                preparationTime = preparationTime,
                servings = servings,
                difficulty = difficulty,
                imageUrl = imageUrl,
                ingredients = ingredients,
                instructions = instructions,
                category = category
            )
            recipeDao.insertRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.deleteRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeDao.updateRecipe(recipe)
        }
    }
} 