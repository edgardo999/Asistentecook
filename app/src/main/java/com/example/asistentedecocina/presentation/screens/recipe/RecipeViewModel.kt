package com.example.asistentedecocina.presentation.screens.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentedecocina.data.model.Recipe
import com.example.asistentedecocina.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes.asStateFlow()

    init {
        loadRecipes()
        loadFavoriteRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            recipeRepository.getAllRecipes().collect { recipes ->
                _recipes.value = recipes
            }
        }
    }

    private fun loadFavoriteRecipes() {
        viewModelScope.launch {
            recipeRepository.getFavoriteRecipes().collect { recipes ->
                _favoriteRecipes.value = recipes
            }
        }
    }

    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.saveRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.updateRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipe)
        }
    }

    fun toggleFavorite(recipeId: String) {
        viewModelScope.launch {
            recipeRepository.toggleFavorite(recipeId)
        }
    }

    fun removeFavorite(recipeId: String) {
        viewModelScope.launch {
            recipeRepository.removeFavorite(recipeId)
        }
    }
} 