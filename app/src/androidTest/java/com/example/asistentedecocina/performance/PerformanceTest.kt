package com.example.asistentedecocina.performance

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asistentedecocina.data.model.*
import com.example.asistentedecocina.data.repository.RecipeRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PerformanceTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `load large recipe list performance`() = runTest {
        // Arrange
        val largeRecipeList = (1..1000).map { createTestRecipe(it.toString()) }

        // Act & Assert
        benchmarkRule.measureRepeated {
            recipeRepository.saveRecipes(largeRecipeList)
            val loadedRecipes = recipeRepository.getAllRecipes()
            assertEquals(1000, loadedRecipes.size)
        }
    }

    @Test
    fun `search recipes performance`() = runTest {
        // Arrange
        val recipes = (1..1000).map { createTestRecipe(it.toString()) }
        recipeRepository.saveRecipes(recipes)

        // Act & Assert
        benchmarkRule.measureRepeated {
            val searchResults = recipeRepository.searchRecipes("Pasta")
            assertTrue(searchResults.isNotEmpty())
        }
    }

    @Test
    fun `filter recipes performance`() = runTest {
        // Arrange
        val recipes = (1..1000).map { createTestRecipe(it.toString()) }
        recipeRepository.saveRecipes(recipes)

        // Act & Assert
        benchmarkRule.measureRepeated {
            val filteredRecipes = recipeRepository.filterRecipes(
                cuisine = Cuisine.MEXICANA,
                difficulty = Difficulty.MEDIO
            )
            assertTrue(filteredRecipes.isNotEmpty())
        }
    }

    @Test
    fun `update recipe performance`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        recipeRepository.saveRecipe(recipe)

        // Act & Assert
        benchmarkRule.measureRepeated {
            val updatedRecipe = recipe.copy(name = "Receta Actualizada")
            recipeRepository.updateRecipe(updatedRecipe)
            val loadedRecipe = recipeRepository.getRecipeById("1")
            assertEquals("Receta Actualizada", loadedRecipe?.name)
        }
    }

    @Test
    fun `delete recipe performance`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        recipeRepository.saveRecipe(recipe)

        // Act & Assert
        benchmarkRule.measureRepeated {
            recipeRepository.deleteRecipe("1")
            val deletedRecipe = recipeRepository.getRecipeById("1")
            assertTrue(deletedRecipe == null)
        }
    }

    @Test
    fun `toggle favorite performance`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        recipeRepository.saveRecipe(recipe)

        // Act & Assert
        benchmarkRule.measureRepeated {
            recipeRepository.toggleFavorite("1")
            val updatedRecipe = recipeRepository.getRecipeById("1")
            assertTrue(updatedRecipe?.isFavorite == true)
        }
    }

    @Test
    fun `load favorite recipes performance`() = runTest {
        // Arrange
        val recipes = (1..1000).map { createTestRecipe(it.toString()) }
        recipeRepository.saveRecipes(recipes)
        recipes.forEach { recipe ->
            recipeRepository.toggleFavorite(recipe.id)
        }

        // Act & Assert
        benchmarkRule.measureRepeated {
            val favoriteRecipes = recipeRepository.getFavoriteRecipes()
            assertEquals(1000, favoriteRecipes.size)
        }
    }

    @Test
    fun `concurrent operations performance`() = runTest {
        // Arrange
        val recipes = (1..1000).map { createTestRecipe(it.toString()) }

        // Act & Assert
        benchmarkRule.measureRepeated {
            // Simular operaciones concurrentes
            recipeRepository.saveRecipes(recipes)
            val searchResults = recipeRepository.searchRecipes("Pasta")
            val filteredRecipes = recipeRepository.filterRecipes(
                cuisine = Cuisine.MEXICANA,
                difficulty = Difficulty.MEDIO
            )
            recipeRepository.toggleFavorite("1")
            val favoriteRecipes = recipeRepository.getFavoriteRecipes()

            assertTrue(searchResults.isNotEmpty())
            assertTrue(filteredRecipes.isNotEmpty())
            assertTrue(favoriteRecipes.isNotEmpty())
        }
    }

    private fun createTestRecipe(id: String): Recipe {
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
            isFavorite = false
        )
    }
} 