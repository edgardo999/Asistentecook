package com.example.asistentedecocina.data.local.entity

import androidx.room.*
import com.example.asistentedecocina.data.model.Recipe
import com.example.asistentedecocina.data.model.RecipeCategory
import com.example.asistentedecocina.data.model.RecipeDifficulty
import com.example.asistentedecocina.data.model.RecipeIngredient

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val instructions: String,
    val preparationTime: Int,
    val cookingTime: Int,
    val servings: Int,
    val difficulty: RecipeDifficulty,
    val imageUrl: String?,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val isGlutenFree: Boolean,
    val isDairyFree: Boolean,
    val isNutFree: Boolean,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val notes: String?
)

@Entity(tableName = "recipe_categories")
data class RecipeCategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?
)

@Entity(tableName = "recipe_ingredients")
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: String,
    val name: String,
    val amount: Double,
    val unit: String,
    val notes: String?
)

@Entity(tableName = "recipe_category_relations")
data class RecipeCategoryRelation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: String,
    val categoryId: String
)

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey
    val recipeId: String,
    val addedDate: Long
) 