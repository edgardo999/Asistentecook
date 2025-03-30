package com.example.asistentedecocina.data.repository

import com.example.asistentedecocina.data.local.dao.MealPlanDao
import com.example.asistentedecocina.data.local.entity.*
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
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MealPlanRepositoryTest {

    @Mock
    private lateinit var mealPlanDao: MealPlanDao

    private lateinit var repository: MealPlanRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = MealPlanRepository(mealPlanDao)
    }

    @Test
    fun `getMealPlan returns plan when exists`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()
        val mealPlanEntity = createTestMealPlanEntity(weekStart)
        whenever(mealPlanDao.getMealPlan(weekStart)).thenReturn(mealPlanEntity)

        // Act
        val result = repository.getMealPlan(weekStart)

        // Assert
        assert(result != null)
        assertEquals(weekStart, result?.weekStart)
    }

    @Test
    fun `getMealPlan returns null when not exists`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()
        whenever(mealPlanDao.getMealPlan(weekStart)).thenReturn(null)

        // Act
        val result = repository.getMealPlan(weekStart)

        // Assert
        assertNull(result)
    }

    @Test
    fun `saveMealPlan saves plan and its relations`() = runTest {
        // Arrange
        val mealPlan = createTestMealPlan(LocalDate.now())

        // Act
        repository.saveMealPlan(mealPlan)

        // Assert
        verify(mealPlanDao).insertMealPlan(any())
        verify(mealPlanDao).insertPlannedMeal(any())
    }

    @Test
    fun `updateMealPlan updates plan and its relations`() = runTest {
        // Arrange
        val mealPlan = createTestMealPlan(LocalDate.now())

        // Act
        repository.updateMealPlan(mealPlan)

        // Assert
        verify(mealPlanDao).updateMealPlan(any())
        verify(mealPlanDao).deletePlannedMeals(any())
        verify(mealPlanDao).insertPlannedMeal(any())
    }

    @Test
    fun `deleteMealPlan deletes plan`() = runTest {
        // Arrange
        val weekStart = LocalDate.now()

        // Act
        repository.deleteMealPlan(weekStart)

        // Assert
        verify(mealPlanDao).deleteMealPlan(weekStart)
    }

    @Test
    fun `getPreferences returns preferences`() = runTest {
        // Arrange
        val preferences = MealPlanPreferences()
        whenever(mealPlanDao.getPreferences()).thenReturn(flowOf(preferences))

        // Act
        val result = repository.getPreferences().single()

        // Assert
        assertEquals(preferences, result)
    }

    @Test
    fun `updatePreferences updates preferences`() = runTest {
        // Arrange
        val preferences = MealPlanPreferences()

        // Act
        repository.updatePreferences(preferences)

        // Assert
        verify(mealPlanDao).updatePreferences(preferences)
    }

    @Test
    fun `getMealPlansForDateRange returns plans in range`() = runTest {
        // Arrange
        val startDate = LocalDate.now()
        val endDate = startDate.plusWeeks(2)
        val mealPlans = listOf(
            createTestMealPlanEntity(startDate),
            createTestMealPlanEntity(startDate.plusWeeks(1))
        )
        whenever(mealPlanDao.getMealPlansForDateRange(startDate, endDate)).thenReturn(mealPlans)

        // Act
        val result = repository.getMealPlansForDateRange(startDate, endDate)

        // Assert
        assertEquals(2, result.size)
        assertEquals(startDate, result[0].weekStart)
        assertEquals(startDate.plusWeeks(1), result[1].weekStart)
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

    private fun createTestMealPlanEntity(weekStart: LocalDate): MealPlanEntity {
        return MealPlanEntity(
            weekStart = weekStart,
            notes = "Notas del plan"
        )
    }
} 