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
class ChefAnimationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test chef welcome animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null,
                isChefSpeaking = true
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Chef hablando")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test chef listening animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null,
                isListening = true
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Chef escuchando")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test chef thinking animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = true,
                error = null,
                isChefThinking = true
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Chef pensando")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test chef response animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = listOf(createTestRecipe()),
                isLoading = false,
                error = null,
                chefResponse = "¡Encontré una receta perfecta para ti!"
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithText("¡Encontré una receta perfecta para ti!")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test chef error animation`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = "No pude entender tu comando",
                isChefSpeaking = true
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Assert
        composeTestRule.onNodeWithText("No pude entender tu comando")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test chef voice command integration`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        val voiceCommand = "Buscar receta de pasta"
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null,
                isListening = true
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Simular comando de voz
        viewModel.handleVoiceCommand(arrayOf(voiceCommand))

        // Assert
        composeTestRule.onNodeWithContentDescription("Chef procesando comando")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `test chef animation transitions`() {
        // Arrange
        val viewModel = mock(RecipeViewModel::class.java)
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null,
                isChefSpeaking = true
            )
        )

        // Act
        composeTestRule.setContent {
            RecipeScreen(viewModel = viewModel)
        }

        // Simular transiciones de estado
        viewModel.updateChefState(isListening = true)
        composeTestRule.onNodeWithContentDescription("Chef escuchando")
            .assertExists()
            .assertIsDisplayed()

        viewModel.updateChefState(isChefThinking = true)
        composeTestRule.onNodeWithContentDescription("Chef pensando")
            .assertExists()
            .assertIsDisplayed()

        viewModel.updateChefState(isChefSpeaking = true)
        composeTestRule.onNodeWithContentDescription("Chef hablando")
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