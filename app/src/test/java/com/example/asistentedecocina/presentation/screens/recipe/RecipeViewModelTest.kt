package com.example.asistentedecocina.presentation.screens.recipe

import app.cash.turbine.test
import com.example.asistentedecocina.data.model.*
import com.example.asistentedecocina.data.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {

    @Mock
    private lateinit var repository: RecipeRepository

    private lateinit var viewModel: RecipeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadRecipes updates recipes state`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1"),
            createTestRecipe("2")
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()

        // Assert
        viewModel.recipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(recipes, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `loadFavoriteRecipes updates favoriteRecipes state`() = runTest {
        // Arrange
        val favoriteRecipes = listOf(
            createTestRecipe("1", isFavorite = true),
            createTestRecipe("2", isFavorite = true)
        )
        whenever(repository.getFavoriteRecipes()).thenReturn(favoriteRecipes)

        // Act
        viewModel.loadFavoriteRecipes()

        // Assert
        viewModel.favoriteRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(favoriteRecipes, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `saveRecipe calls repository saveRecipe and updates UI`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        whenever(repository.getRecipeById("1")).thenReturn(recipe)

        // Act
        viewModel.saveRecipe(recipe)

        // Assert
        verify(repository).saveRecipe(recipe)
        viewModel.selectedRecipe.test {
            assertNull(awaitItem())
            assertEquals(recipe, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `updateRecipe calls repository updateRecipe and updates UI`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        whenever(repository.getRecipeById("1")).thenReturn(recipe)

        // Act
        viewModel.updateRecipe(recipe)

        // Assert
        verify(repository).updateRecipe(recipe)
        viewModel.selectedRecipe.test {
            assertNull(awaitItem())
            assertEquals(recipe, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `deleteRecipe calls repository deleteRecipe and clears selected recipe`() = runTest {
        // Arrange
        val recipeId = "1"
        val recipe = createTestRecipe(recipeId)
        whenever(repository.getRecipeById(recipeId)).thenReturn(recipe)

        // Act
        viewModel.getRecipeById(recipeId)
        viewModel.deleteRecipe(recipeId)

        // Assert
        verify(repository).deleteRecipe(recipeId)
        viewModel.selectedRecipe.test {
            assertNull(awaitItem())
            assertEquals(recipe, awaitItem())
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `toggleFavorite calls repository toggleFavorite and updates UI`() = runTest {
        // Arrange
        val recipeId = "1"
        val recipe = createTestRecipe(recipeId, isFavorite = false)
        whenever(repository.getRecipeById(recipeId)).thenReturn(recipe)
        whenever(repository.getFavoriteRecipes()).thenReturn(listOf(recipe.copy(isFavorite = true)))

        // Act
        viewModel.toggleFavorite(recipeId)

        // Assert
        verify(repository).toggleFavorite(recipeId)
        viewModel.favoriteRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipe.copy(isFavorite = true)), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `searchRecipes filters recipes by name`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1", name = "Pasta"),
            createTestRecipe("2", name = "Arroz"),
            createTestRecipe("3", name = "Pizza")
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.searchRecipes("Pasta")

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipes[0]), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `filterByCuisine filters recipes by cuisine`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1", cuisine = Cuisine.MEXICANA),
            createTestRecipe("2", cuisine = Cuisine.ITALIANA),
            createTestRecipe("3", cuisine = Cuisine.MEXICANA)
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.filterByCuisine(Cuisine.MEXICANA)

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipes[0], recipes[2]), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `filterByDifficulty filters recipes by difficulty`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1", difficulty = Difficulty.FACIL),
            createTestRecipe("2", difficulty = Difficulty.MEDIO),
            createTestRecipe("3", difficulty = Difficulty.FACIL)
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.filterByDifficulty(Difficulty.FACIL)

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipes[0], recipes[2]), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `loadRecipes handles empty list`() = runTest {
        // Arrange
        whenever(repository.getAllRecipes()).thenReturn(emptyList())

        // Act
        viewModel.loadRecipes()

        // Assert
        viewModel.recipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(emptyList(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `loadRecipes handles error`() = runTest {
        // Arrange
        whenever(repository.getAllRecipes()).thenThrow(RuntimeException("Error de carga"))

        // Act
        viewModel.loadRecipes()

        // Assert
        viewModel.error.test {
            assertEquals(null, awaitItem())
            assertEquals("Error al cargar las recetas", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `searchRecipes with empty query returns all recipes`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1"),
            createTestRecipe("2"),
            createTestRecipe("3")
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.searchRecipes("")

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(recipes, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `searchRecipes is case insensitive`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1", name = "Pasta"),
            createTestRecipe("2", name = "Arroz"),
            createTestRecipe("3", name = "Pizza")
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.searchRecipes("PASTA")

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipes[0]), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `filterByCuisine and searchRecipes work together`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1", name = "Pasta", cuisine = Cuisine.ITALIANA),
            createTestRecipe("2", name = "Pasta", cuisine = Cuisine.MEXICANA),
            createTestRecipe("3", name = "Arroz", cuisine = Cuisine.ITALIANA)
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.filterByCuisine(Cuisine.ITALIANA)
        viewModel.searchRecipes("Pasta")

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipes[0]), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `clearFilters resets filtered recipes`() = runTest {
        // Arrange
        val recipes = listOf(
            createTestRecipe("1"),
            createTestRecipe("2"),
            createTestRecipe("3")
        )
        whenever(repository.getAllRecipes()).thenReturn(recipes)

        // Act
        viewModel.loadRecipes()
        viewModel.filterByCuisine(Cuisine.MEXICANA)
        viewModel.clearFilters()

        // Assert
        viewModel.filteredRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(recipes, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getRecipeById returns correct recipe`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        whenever(repository.getRecipeById("1")).thenReturn(recipe)

        // Act
        viewModel.getRecipeById("1")

        // Assert
        viewModel.selectedRecipe.test {
            assertNull(awaitItem())
            assertEquals(recipe, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getRecipeById returns null for non-existent recipe`() = runTest {
        // Arrange
        whenever(repository.getRecipeById("1")).thenReturn(null)

        // Act
        viewModel.getRecipeById("1")

        // Assert
        viewModel.selectedRecipe.test {
            assertNull(awaitItem())
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `toggleFavorite updates recipe favorite status in UI`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1", isFavorite = false)
        whenever(repository.getRecipeById("1")).thenReturn(recipe)
        whenever(repository.getFavoriteRecipes()).thenReturn(listOf(recipe.copy(isFavorite = true)))

        // Act
        viewModel.toggleFavorite("1")

        // Assert
        viewModel.favoriteRecipes.test {
            assertEquals(emptyList(), awaitItem())
            assertEquals(listOf(recipe.copy(isFavorite = true)), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `updateRecipe updates recipe in UI`() = runTest {
        // Arrange
        val originalRecipe = createTestRecipe("1")
        val updatedRecipe = originalRecipe.copy(name = "Receta Actualizada")
        whenever(repository.getRecipeById("1")).thenReturn(updatedRecipe)

        // Act
        viewModel.updateRecipe(updatedRecipe)

        // Assert
        viewModel.selectedRecipe.test {
            assertNull(awaitItem())
            assertEquals(updatedRecipe, awaitItem())
            awaitComplete()
        }
    }

    private fun createTestRecipe(
        id: String,
        name: String = "Receta $id",
        cuisine: Cuisine = Cuisine.MEXICANA,
        difficulty: Difficulty = Difficulty.MEDIO,
        isFavorite: Boolean = false
    ): Recipe {
        return Recipe(
            id = id,
            name = name,
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
            difficulty = difficulty,
            cuisine = cuisine,
            mealType = MealType.ALMUERZO,
            imageUrl = "https://example.com/image.jpg",
            isFavorite = isFavorite
        )
    }
} 