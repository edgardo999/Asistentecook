package com.example.asistentedecocina.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asistentedecocina.presentation.screens.recipe.RecipeScreen
import com.example.asistentedecocina.presentation.screens.recipe.RecipeViewModel
import com.example.asistentedecocina.presentation.screens.recipe.RecipeUiState
import com.example.asistentedecocina.data.model.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class AnimationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test recipe card animation on click`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        val recipe = createTestRecipe()
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = listOf(recipe),
                isLoading = false,
                error = null
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithText(recipe.name)
            .assertExists()
            .performClick()
            .assertIsDisplayed()
    }

    @Test
    fun `test search bar animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Buscar recetas")
            .assertExists()
            .performClick()
            .assertIsDisplayed()
    }

    @Test
    fun `test filter chips animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithText("Mexicana")
            .assertExists()
            .performClick()
            .assertIsDisplayed()
    }

    @Test
    fun `test loading animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = true,
                error = null
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Cargando")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test error animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = "Error de carga"
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithText("Error de carga")
            .assertExists()
            .assertIsDisplayed()
    }

    private fun createTestRecipe(): Recipe {
        return Recipe(
            id = "1",
            name = "Test Recipe",
            description = "Test Description",
            ingredients = listOf(
                Ingredient(
                    name = "Test Ingredient",
                    amount = "100g",
                    unit = "gramos"
                )
            ),
            instructions = listOf("Test Step 1", "Test Step 2"),
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