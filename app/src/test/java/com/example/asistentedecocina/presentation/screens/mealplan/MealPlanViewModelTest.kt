package com.example.asistentedecocina.presentation.screens.mealplan

import app.cash.turbine.test
import com.example.asistentedecocina.data.model.*
import com.example.asistentedecocina.data.repository.MealPlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MealPlanViewModelTest {

    @Mock
    private lateinit var repository: MealPlanRepository

    private lateinit var viewModel: MealPlanViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MealPlanViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMealPlan updates currentPlan state`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()
        val mealPlan = createTestMealPlan(weekStart)
        whenever(repository.getMealPlan(weekStart)).thenReturn(mealPlan)

        // Act & Assert
        viewModel.currentPlan.test {
            assertEquals(null, awaitItem())
            assertEquals(mealPlan, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `loadPreferences updates preferences state`() = runTest {
        // Arrange
        val preferences = MealPlanPreferences()
        whenever(repository.getPreferences()).thenReturn(flowOf(preferences))

        // Act & Assert
        viewModel.preferences.test {
            assertEquals(MealPlanPreferences(), awaitItem())
            assertEquals(preferences, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `saveMealPlan calls repository saveMealPlan`() = runTest {
        // Arrange
        val mealPlan = createTestMealPlan(LocalDate.now())

        // Act
        viewModel.saveMealPlan(mealPlan)

        // Assert
        verify(repository).saveMealPlan(mealPlan)
    }

    @Test
    fun `updateMealPlan calls repository updateMealPlan`() = runTest {
        // Arrange
        val mealPlan = createTestMealPlan(LocalDate.now())

        // Act
        viewModel.updateMealPlan(mealPlan)

        // Assert
        verify(repository).updateMealPlan(mealPlan)
    }

    @Test
    fun `deleteMealPlan calls repository deleteMealPlan`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()

        // Act
        viewModel.deleteMealPlan(weekStart)

        // Assert
        verify(repository).deleteMealPlan(weekStart)
    }

    @Test
    fun `updatePreferences calls repository updatePreferences`() = runTest {
        // Arrange
        val preferences = MealPlanPreferences()

        // Act
        viewModel.updatePreferences(preferences)

        // Assert
        verify(repository).updatePreferences(preferences)
    }

    @Test
    fun `addMealToPlan updates currentPlan state`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()
        val mealPlan = createTestMealPlan(weekStart)
        whenever(repository.getMealPlan(weekStart)).thenReturn(mealPlan)

        val newMeal = PlannedMeal(
            recipeId = "2",
            recipeName = "Receta 2",
            mealType = MealType.ALMUERZO,
            servings = 2,
            notes = "Notas"
        )

        // Act
        viewModel.addMealToPlan(DayOfWeek.MONDAY, newMeal)

        // Assert
        viewModel.currentPlan.test {
            assertEquals(null, awaitItem())
            assertTrue(awaitItem()?.meals?.get(DayOfWeek.MONDAY)?.contains(newMeal) == true)
            awaitComplete()
        }
    }

    @Test
    fun `removeMealFromPlan updates currentPlan state`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()
        val mealPlan = createTestMealPlan(weekStart)
        whenever(repository.getMealPlan(weekStart)).thenReturn(mealPlan)

        // Act
        viewModel.removeMealFromPlan(DayOfWeek.MONDAY, "1")

        // Assert
        viewModel.currentPlan.test {
            assertEquals(null, awaitItem())
            assertTrue(awaitItem()?.meals?.get(DayOfWeek.MONDAY)?.isEmpty() == true)
            awaitComplete()
        }
    }

    private fun createTestMealPlan(weekStart: LocalDate): MealPlan {
        return MealPlan(
            weekStart = weekStart,
            meals = mapOf(
                DayOfWeek.MONDAY to listOf(
                    PlannedMeal(
                        recipeId = "1",
                        recipeName = "Receta 1",
                        mealType = MealType.DESAYUNO,
                        servings = 2,
                        notes = "Notas"
                    )
                )
            ),
            notes = "Notas del plan"
        )
    }
} 