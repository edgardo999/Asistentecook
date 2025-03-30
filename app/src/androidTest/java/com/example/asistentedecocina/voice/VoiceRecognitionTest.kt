package com.example.asistentedecocina.voice

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asistentedecocina.presentation.screens.recipe.RecipeViewModel
import com.example.asistentedecocina.presentation.screens.recipe.RecipeUiState
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class VoiceRecognitionTest {

    private lateinit var context: Context
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        viewModel = mock(RecipeViewModel::class.java)
    }

    @Test
    fun `test speech recognizer initialization`() {
        assertTrue(speechRecognizer != null)
    }

    @Test
    fun `test speech recognition intent`() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        assertTrue(intent.hasExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL))
        assertEquals("es-ES", intent.getStringExtra(RecognizerIntent.EXTRA_LANGUAGE))
    }

    @Test
    fun `test speech recognition listener`() {
        val listener = mock(RecognitionListener::class.java)
        speechRecognizer.setRecognitionListener(listener)

        // Simular eventos de reconocimiento
        speechRecognizer.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
        verify(listener).onReadyForSpeech(any())

        // Simular resultados
        val results = arrayOf("Buscar receta de pasta")
        speechRecognizer.stopListening()
        verify(listener).onResults(any())
    }

    @Test
    fun `test voice search command`() {
        val searchQuery = "Buscar receta de pasta"
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null
            )
        )

        // Simular reconocimiento de voz
        val results = arrayOf(searchQuery)
        viewModel.handleVoiceCommand(results)

        verify(viewModel).searchRecipes(searchQuery)
    }

    @Test
    fun `test voice filter command`() {
        val filterCommand = "Filtrar por cocina mexicana"
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null
            )
        )

        // Simular reconocimiento de voz
        val results = arrayOf(filterCommand)
        viewModel.handleVoiceCommand(results)

        verify(viewModel).filterRecipes(cuisine = Cuisine.MEXICANA)
    }

    @Test
    fun `test voice navigation command`() {
        val navigationCommand = "Ir a la siguiente receta"
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = listOf(createTestRecipe()),
                isLoading = false,
                error = null
            )
        )

        // Simular reconocimiento de voz
        val results = arrayOf(navigationCommand)
        viewModel.handleVoiceCommand(results)

        verify(viewModel).navigateToNextRecipe()
    }

    @Test
    fun `test voice error handling`() {
        val errorCommand = "Comando no reconocido"
        `when`(viewModel.uiState).thenReturn(
            RecipeUiState(
                recipes = emptyList(),
                isLoading = false,
                error = null
            )
        )

        // Simular reconocimiento de voz con error
        val results = arrayOf(errorCommand)
        viewModel.handleVoiceCommand(results)

        verify(viewModel, never()).searchRecipes(any())
        verify(viewModel, never()).filterRecipes(any())
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