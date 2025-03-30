package com.example.asistentedecocina.integration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asistentedecocina.data.model.*
import com.example.asistentedecocina.data.repository.MealPlanRepository
import com.example.asistentedecocina.data.repository.RecipeRepository
import com.example.asistentedecocina.presentation.screens.mealplan.MealPlanViewModel
import com.example.asistentedecocina.presentation.screens.recipe.RecipeViewModel
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ViewModelIntegrationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var mealPlanRepository: MealPlanRepository

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var mealPlanViewModel: MealPlanViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        recipeViewModel = RecipeViewModel(recipeRepository)
        mealPlanViewModel = MealPlanViewModel(mealPlanRepository)
    }

    @Test
    fun `adding recipe to meal plan updates both viewmodels`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        val weekStart = LocalDate.now()
        val plannedMeal = PlannedMeal(
            recipeId = recipe.id,
            recipeName = recipe.name,
            mealType = MealType.ALMUERZO,
            servings = 2,
            notes = "Notas"
        )

        // Act
        recipeViewModel.saveRecipe(recipe)
        mealPlanViewModel.addMealToPlan(DayOfWeek.MONDAY, plannedMeal)

        // Assert
        val savedRecipe = recipeRepository.getRecipeById(recipe.id)
        assertNotNull(savedRecipe)
        assertEquals(recipe.id, savedRecipe.id)

        val mealPlan = mealPlanRepository.getMealPlan(weekStart)
        assertNotNull(mealPlan)
        assertTrue(mealPlan.meals[DayOfWeek.MONDAY]?.contains(plannedMeal) == true)
    }

    @Test
    fun `updating recipe updates meal plan`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        val weekStart = LocalDate.now()
        val plannedMeal = PlannedMeal(
            recipeId = recipe.id,
            recipeName = recipe.name,
            mealType = MealType.ALMUERZO,
            servings = 2,
            notes = "Notas"
        )

        // Act
        recipeViewModel.saveRecipe(recipe)
        mealPlanViewModel.addMealToPlan(DayOfWeek.MONDAY, plannedMeal)
        
        val updatedRecipe = recipe.copy(name = "Receta Actualizada")
        recipeViewModel.updateRecipe(updatedRecipe)

        // Assert
        val mealPlan = mealPlanRepository.getMealPlan(weekStart)
        assertNotNull(mealPlan)
        val updatedPlannedMeal = mealPlan.meals[DayOfWeek.MONDAY]?.find { it.recipeId == recipe.id }
        assertNotNull(updatedPlannedMeal)
        assertEquals(updatedRecipe.name, updatedPlannedMeal.recipeName)
    }

    @Test
    fun `deleting recipe removes it from meal plan`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        val weekStart = LocalDate.now()
        val plannedMeal = PlannedMeal(
            recipeId = recipe.id,
            recipeName = recipe.name,
            mealType = MealType.ALMUERZO,
            servings = 2,
            notes = "Notas"
        )

        // Act
        recipeViewModel.saveRecipe(recipe)
        mealPlanViewModel.addMealToPlan(DayOfWeek.MONDAY, plannedMeal)
        recipeViewModel.deleteRecipe(recipe.id)

        // Assert
        val deletedRecipe = recipeRepository.getRecipeById(recipe.id)
        assertNull(deletedRecipe)

        val mealPlan = mealPlanRepository.getMealPlan(weekStart)
        assertNotNull(mealPlan)
        assertTrue(mealPlan.meals[DayOfWeek.MONDAY]?.isEmpty() == true)
    }

    @Test
    fun `favorite recipe appears in favorites and meal plan`() = runTest {
        // Arrange
        val recipe = createTestRecipe("1")
        val weekStart = LocalDate.now()
        val plannedMeal = PlannedMeal(
            recipeId = recipe.id,
            recipeName = recipe.name,
            mealType = MealType.ALMUERZO,
            servings = 2,
            notes = "Notas"
        )

        // Act
        recipeViewModel.saveRecipe(recipe)
        recipeViewModel.toggleFavorite(recipe.id)
        mealPlanViewModel.addMealToPlan(DayOfWeek.MONDAY, plannedMeal)

        // Assert
        val favoriteRecipes = recipeRepository.getFavoriteRecipes()
        assertTrue(favoriteRecipes.contains(recipe))

        val mealPlan = mealPlanRepository.getMealPlan(weekStart)
        assertNotNull(mealPlan)
        assertTrue(mealPlan.meals[DayOfWeek.MONDAY]?.contains(plannedMeal) == true)
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