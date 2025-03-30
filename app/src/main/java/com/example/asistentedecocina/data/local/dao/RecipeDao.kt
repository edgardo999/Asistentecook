package com.example.asistentedecocina.data.local.dao

import androidx.room.*
import com.example.asistentedecocina.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    // Recetas
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: String): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    // Categorías
    @Query("SELECT * FROM recipe_categories")
    fun getAllCategories(): Flow<List<RecipeCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: RecipeCategoryEntity)

    @Update
    suspend fun updateCategory(category: RecipeCategoryEntity)

    @Delete
    suspend fun deleteCategory(category: RecipeCategoryEntity)

    // Ingredientes
    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun getRecipeIngredients(recipeId: String): List<RecipeIngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: RecipeIngredientEntity)

    @Update
    suspend fun updateIngredient(ingredient: RecipeIngredientEntity)

    @Delete
    suspend fun deleteIngredient(ingredient: RecipeIngredientEntity)

    // Relaciones receta-categoría
    @Query("SELECT categoryId FROM recipe_category_relations WHERE recipeId = :recipeId")
    suspend fun getRecipeCategoryIds(recipeId: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeCategoryRelation(relation: RecipeCategoryRelation)

    @Delete
    suspend fun deleteRecipeCategoryRelation(relation: RecipeCategoryRelation)

    // Recetas favoritas
    @Query("SELECT * FROM favorite_recipes")
    fun getFavoriteRecipes(): Flow<List<FavoriteRecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favorite: FavoriteRecipeEntity)

    @Delete
    suspend fun deleteFavoriteRecipe(favorite: FavoriteRecipeEntity)

    // Búsqueda
    @Query("""
        SELECT r.* FROM recipes r
        LEFT JOIN recipe_category_relations rcr ON r.id = rcr.recipeId
        WHERE r.name LIKE '%' || :query || '%'
        OR r.description LIKE '%' || :query || '%'
        OR rcr.categoryId IN (:categoryIds)
    """)
    fun searchRecipes(query: String, categoryIds: List<String>): Flow<List<RecipeEntity>>

    // Filtros
    @Query("""
        SELECT r.* FROM recipes r
        WHERE (:isVegetarian = 0 OR r.isVegetarian = 1)
        AND (:isVegan = 0 OR r.isVegan = 1)
        AND (:isGlutenFree = 0 OR r.isGlutenFree = 1)
        AND (:isDairyFree = 0 OR r.isDairyFree = 1)
        AND (:isNutFree = 0 OR r.isNutFree = 1)
        AND r.difficulty IN (:difficulties)
        AND r.preparationTime + r.cookingTime BETWEEN :minTime AND :maxTime
        AND r.servings BETWEEN :minServings AND :maxServings
    """)
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
    ): Flow<List<RecipeEntity>>
} 