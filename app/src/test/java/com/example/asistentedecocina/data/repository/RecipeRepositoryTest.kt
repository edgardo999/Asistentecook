package com.example.asistentedecocina.data.repository

import com.example.asistentedecocina.data.local.dao.RecipeDao
import com.example.asistentedecocina.data.local.entity.RecipeEntity
import com.example.asistentedecocina.data.model.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.DayOfWeek
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RecipeRepositoryTest {

    @Mock
    private lateinit var recipeDao: RecipeDao

    private lateinit var repository: RecipeRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RecipeRepository(recipeDao)
    }

    @Test
    fun `getAllRecipes returns list of recipes`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipeEntity("1"),
            createTestRecipeEntity("2")
        )
        whenever(recipeDao.getAllRecipes()).thenReturn(recipes)

        // Act
        val result = repository.getAllRecipes()

        // Assert
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("2", result[1].id)
    }

    @Test
    fun `getRecipeById returns recipe when exists`() = runTest {
        // Arrange
        val recipeId = "1"
        val recipeEntity = createTestRecipeEntity(recipeId)
        whenever(recipeDao.getRecipeById(recipeId)).thenReturn(recipeEntity)

        // Act
        val result = repository.getRecipeById(recipeId)

        // Assert
        assert(result != null)
        assertEquals(recipeId, result?.id)
    }

    @Test
    fun `getRecipeById returns null when not exists`() = runTest {
        // Arrange
        val recipeId = "1"
        whenever(recipeDao.getRecipeById(recipeId)).thenReturn(null)

        // Act
        val result = repository.getRecipeById(recipeId)

        // Assert
        assertNull(result)
    }

    @Test
    fun `saveRecipe saves recipe`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")

        // Act
        repository.saveRecipe(recipe)

        // Assert
        verify(recipeDao).insertRecipe(any())
    }

    @Test
    fun `updateRecipe updates recipe`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")

        // Act
        repository.updateRecipe(recipe)

        // Assert
        verify(recipeDao).updateRecipe(any())
    }

    @Test
    fun `deleteRecipe deletes recipe`() = runTest {
        // Arrange
        val recipeId = "1"

        // Act
        repository.deleteRecipe(recipeId)

        // Assert
        verify(recipeDao).deleteRecipe(recipeId)
    }

    @Test
    fun `getFavoriteRecipes returns favorite recipes`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipeEntity("1", true),
            createTestRecipeEntity("2", true)
        )
        whenever(recipeDao.getFavoriteRecipes()).thenReturn(recipes)

        // Act
        val result = repository.getFavoriteRecipes()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result.all { it.isFavorite })
    }

    @Test
    fun `toggleFavorite updates recipe favorite status`() = runTest {
        // Arrange
        val recipeId = "1"
        val recipe = createTestRecipe(recipeId, false)
        whenever(recipeDao.getRecipeById(recipeId)).thenReturn(createTestRecipeEntity(recipeId, false))

        // Act
        repository.toggleFavorite(recipeId)

        // Assert
        verify(recipeDao).updateRecipe(argThat { isFavorite })
    }

    private fun createTestRecipe(id: String, isFavorite: Boolean = false): Recipe {
        return Recipe(
            id = id,
            name = "Receta $id",
            description = "Descripción de la receta $id",
            ingredients = listOf(
                Ingredient(
                    name = "Ingrediente 1",
                    amount = "100g",
                    unit = "gramos"
                )
            ),
            instructions = listOf("Paso 1", "Paso 2"),
            prepTime = 30,
            cookTime = 45,
            servings = 4,
            difficulty = Difficulty.MEDIO,
            cuisine = Cuisine.MEXICANA,
            mealType = MealType.ALMUERZO,
            imageUrl = "https://example.com/image.jpg",
            isFavorite = isFavorite
        )
    }

    private fun createTestRecipeEntity(id: String, isFavorite: Boolean = false): RecipeEntity {
        return RecipeEntity(
            id = id,
            name = "Receta $id",
            description = "Descripción de la receta $id",
            ingredients = listOf(
                Ingredient(
                    name = "Ingrediente 1",
                    amount = "100g",
                    unit = "gramos"
                )
            ),
            instructions = listOf("Paso 1", "Paso 2"),
            prepTime = 30,
            cookTime = 45,
            servings = 4,
            difficulty = Difficulty.MEDIO,
            cuisine = Cuisine.MEXICANA,
            mealType = MealType.ALMUERZO,
            imageUrl = "https://example.com/image.jpg",
            isFavorite = isFavorite
        )
    }
} 