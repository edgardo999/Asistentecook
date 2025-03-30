package com.example.asistentedecocina.data.repository

import com.example.asistentedecocina.data.local.dao.RecipeDao
import com.example.asistentedecocina.data.local.entity.*
import com.example.asistentedecocina.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao
) {
    fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes().map { entities ->
            entities.map { it.toRecipe() }
        }
    }

    fun getFavoriteRecipes(): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipes().map { favorites ->
            favorites.mapNotNull { favorite ->
                recipeDao.getRecipeById(favorite.recipeId)?.toRecipe()
            }
        }
    }

    suspend fun getRecipeById(id: String): Recipe? {
        return recipeDao.getRecipeById(id)?.toRecipe()
    }

    suspend fun saveRecipe(recipe: Recipe) {
        val entity = recipe.toEntity()
        recipeDao.insertRecipe(entity)

        // Guardar ingredientes
        recipe.ingredients.forEach { ingredient ->
            recipeDao.insertIngredient(ingredient.toEntity(recipe.id))
        }

        // Guardar categorías
        recipe.categories.forEach { category ->
            recipeDao.insertRecipeCategoryRelation(
                RecipeCategoryRelation(
                    recipeId = recipe.id,
                    categoryId = category.id
                )
            )
        }
    }

    suspend fun updateRecipe(recipe: Recipe) {
        val entity = recipe.toEntity()
        recipeDao.updateRecipe(entity)

        // Actualizar ingredientes
        recipeDao.getRecipeIngredients(recipe.id).forEach {
            recipeDao.deleteIngredient(it)
        }
        recipe.ingredients.forEach { ingredient ->
            recipeDao.insertIngredient(ingredient.toEntity(recipe.id))
        }

        // Actualizar categorías
        recipeDao.getRecipeCategoryIds(recipe.id).forEach { categoryId ->
            recipeDao.deleteRecipeCategoryRelation(
                RecipeCategoryRelation(
                    recipeId = recipe.id,
                    categoryId = categoryId
                )
            )
        }
        recipe.categories.forEach { category ->
            recipeDao.insertRecipeCategoryRelation(
                RecipeCategoryRelation(
                    recipeId = recipe.id,
                    categoryId = category.id
                )
            )
        }
    }

    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.toEntity())
    }

    suspend fun toggleFavorite(recipeId: String) {
        val favorite = FavoriteRecipeEntity(recipeId = recipeId, addedDate = System.currentTimeMillis())
        recipeDao.insertFavoriteRecipe(favorite)
    }

    suspend fun removeFavorite(recipeId: String) {
        recipeDao.deleteFavoriteRecipe(FavoriteRecipeEntity(recipeId = recipeId, addedDate = 0))
    }

    fun searchRecipes(query: String, categoryIds: List<String>): Flow<List<Recipe>> {
        return recipeDao.searchRecipes(query, categoryIds).map { entities ->
            entities.map { it.toRecipe() }
        }
    }

    fun filterRecipes(
        isVegetarian: Boolean,
        isVegan: Boolean,
        isGlutenFree: Boolean,
        isDairyFree: Boolean,
        isNutFree: Boolean,
        difficulties: List<RecipeDifficulty>,
        minTime: Int,
        maxTime: Int,
        minServings: Int,
        maxServings: Int
    ): Flow<List<Recipe>> {
        return recipeDao.filterRecipes(
            isVegetarian = isVegetarian,
            isVegan = isVegan,
            isGlutenFree = isGlutenFree,
            isDairyFree = isDairyFree,
            isNutFree = isNutFree,
            difficulties = difficulties,
            minTime = minTime,
            maxTime = maxTime,
            minServings = minServings,
            maxServings = maxServings
        ).map { entities ->
            entities.map { it.toRecipe() }
        }
    }

    private suspend fun RecipeEntity.toRecipe(): Recipe {
        val ingredients = recipeDao.getRecipeIngredients(id)
        val categoryIds = recipeDao.getRecipeCategoryIds(id)
        
        return Recipe(
            id = id,
            name = name,
            description = description,
            instructions = instructions,
            preparationTime = preparationTime,
            cookingTime = cookingTime,
            servings = servings,
            difficulty = difficulty,
            imageUrl = imageUrl,
            isVegetarian = isVegetarian,
            isVegan = isVegan,
            isGlutenFree = isGlutenFree,
            isDairyFree = isDairyFree,
            isNutFree = isNutFree,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            notes = notes,
            ingredients = ingredients.map { it.toIngredient() },
            categories = categoryIds.map { RecipeCategory(it, "", null) } // TODO: Obtener nombres de categorías
        )
    }

    private fun Recipe.toEntity(): RecipeEntity {
        return RecipeEntity(
            id = id,
            name = name,
            description = description,
            instructions = instructions,
            preparationTime = preparationTime,
            cookingTime = cookingTime,
            servings = servings,
            difficulty = difficulty,
            imageUrl = imageUrl,
            isVegetarian = isVegetarian,
            isVegan = isVegan,
            isGlutenFree = isGlutenFree,
            isDairyFree = isDairyFree,
            isNutFree = isNutFree,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            notes = notes
        )
    }

    private fun RecipeIngredientEntity.toIngredient(): RecipeIngredient {
        return RecipeIngredient(
            name = name,
            amount = amount,
            unit = unit,
            notes = notes
        )
    }

    private fun RecipeIngredient.toEntity(recipeId: String): RecipeIngredientEntity {
        return RecipeIngredientEntity(
            recipeId = recipeId,
            name = name,
            amount = amount,
            unit = unit,
            notes = notes
        )
    }
} 