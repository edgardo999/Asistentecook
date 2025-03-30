package com.example.asistentedecocina.data.repository

import com.example.asistentedecocina.data.model.MealPlan
import com.example.asistentedecocina.data.model.MealPlanPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class MealPlanRepository {
    private val mealPlans = MutableStateFlow<Map<LocalDate, MealPlan>>(emptyMap())
    private val preferences = MutableStateFlow(MealPlanPreferences())

    fun getMealPlan(weekStart: LocalDate): Flow<MealPlan?> {
        return mealPlans.map { plans ->
            plans[weekStart]
        }
    }

    suspend fun saveMealPlan(mealPlan: MealPlan) {
        val currentPlans = mealPlans.value.toMutableMap()
        currentPlans[mealPlan.weekStart] = mealPlan
        mealPlans.value = currentPlans
    }

    suspend fun deleteMealPlan(weekStart: LocalDate) {
        val currentPlans = mealPlans.value.toMutableMap()
        currentPlans.remove(weekStart)
        mealPlans.value = currentPlans
    }

    fun getPreferences(): Flow<MealPlanPreferences> {
        return preferences
    }

    suspend fun updatePreferences(newPreferences: MealPlanPreferences) {
        preferences.value = newPreferences
    }

    suspend fun getMealPlansForDateRange(startDate: LocalDate, endDate: LocalDate): List<MealPlan> {
        return mealPlans.value.values.filter { plan ->
            plan.weekStart in startDate..endDate
        }
    }
} 