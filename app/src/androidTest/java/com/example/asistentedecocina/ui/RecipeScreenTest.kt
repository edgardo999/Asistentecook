package com.example.asistentedecocina.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asistentedecocina.MainActivity
import com.example.asistentedecocina.data.model.*
import com.example.asistentedecocina.presentation.screens.recipe.RecipeScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RecipeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `recipe screen displays recipe list`() {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1"),
            createTestRecipe("2"),
            createTestRecipe("3")
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = recipes,
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        recipes.forEach { recipe ->
            composeTestRule.onNodeWithText(recipe.name).assertIsDisplayed()
        }
    }

    @Test
    fun `recipe screen displays empty state when no recipes`() {
        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = emptyList(),
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("No hay recetas").assertIsDisplayed()
    }

    @Test
    fun `recipe screen displays search bar`() {
        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = emptyList(),
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Buscar recetas").assertIsDisplayed()
    }

    @Test
    fun `recipe screen displays filter buttons`() {
        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = emptyList(),
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Filtrar por cocina").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Filtrar por dificultad").assertIsDisplayed()
    }

    @Test
    fun `recipe screen displays add recipe button`() {
        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = emptyList(),
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Agregar receta").assertIsDisplayed()
    }

    @Test
    fun `recipe screen filters recipes when searching`() {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1", name = "Pasta"),
            createTestRecipe("2", name = "Arroz"),
            createTestRecipe("3", name = "Pizza")
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = recipes,
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Buscar recetas")
            .performTextInput("Pasta")

        composeTestRule.onNodeWithText("Pasta").assertIsDisplayed()
        composeTestRule.onNodeWithText("Arroz").assertDoesNotExist()
        composeTestRule.onNodeWithText("Pizza").assertDoesNotExist()
    }

    @Test
    fun `recipe screen displays recipe details when clicked`() {
        // Arrange
        val recipe = createTestRecipe("1")

        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = listOf(recipe),
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText(recipe.name).performClick()
        composeTestRule.onNodeWithText(recipe.description).assertIsDisplayed()
        recipe.ingredients.forEach { ingredient ->
            composeTestRule.onNodeWithText(ingredient.name).assertIsDisplayed()
        }
        recipe.instructions.forEach { instruction ->
            composeTestRule.onNodeWithText(instruction).assertIsDisplayed()
        }
    }

    @Test
    fun `recipe screen displays loading state`() {
        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = emptyList(),
                isLoading = true,
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Cargando").assertIsDisplayed()
    }

    @Test
    fun `recipe screen displays error state`() {
        // Act
        composeTestRule.setContent {
            RecipeScreen(
                recipes = emptyList(),
                error = "Error al cargar las recetas",
                onRecipeClick = {},
                onAddRecipeClick = {},
                onSearchQueryChange = {},
                onFilterByCuisine = {},
                onFilterByDifficulty = {},
                onClearFilters = {}
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Error al cargar las recetas").assertIsDisplayed()
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