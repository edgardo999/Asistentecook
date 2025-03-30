package com.example.asistentedecocina.presentation.screens.mealplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentedecocina.data.model.MealPlan
import com.example.asistentedecocina.data.model.MealPlanPreferences
import com.example.asistentedecocina.data.model.PlannedMeal
import com.example.asistentedecocina.data.repository.MealPlanRepository
import com.example.asistentedecocina.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class MealPlanViewModel(
    private val mealPlanRepository: MealPlanRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _currentMealPlan = MutableStateFlow<MealPlan?>(null)
    val currentMealPlan: StateFlow<MealPlan?> = _currentMealPlan.asStateFlow()

    private val _preferences = MutableStateFlow(MealPlanPreferences())
    val preferences: StateFlow<MealPlanPreferences> = _preferences.asStateFlow()

    private val _selectedWeek = MutableStateFlow(LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong() - 1))
    val selectedWeek: StateFlow<LocalDate> = _selectedWeek.asStateFlow()

    init {
        loadCurrentMealPlan()
        loadPreferences()
    }

    private fun loadCurrentMealPlan() {
        viewModelScope.launch {
            mealPlanRepository.getMealPlan(_selectedWeek.value).collect { plan ->
                _currentMealPlan.value = plan
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            mealPlanRepository.getPreferences().collect { prefs ->
                _preferences.value = prefs
            }
        }
    }

    fun updateSelectedWeek(weekStart: LocalDate) {
        _selectedWeek.value = weekStart
    }

    suspend fun addMeal(dayOfWeek: DayOfWeek, meal: PlannedMeal) {
        val currentPlan = _currentMealPlan.value ?: createEmptyMealPlan()
        val updatedMeals = currentPlan.meals.toMutableMap()
        val dayMeals = updatedMeals[dayOfWeek]?.toMutableList() ?: mutableListOf()
        dayMeals.add(meal)
        updatedMeals[dayOfWeek] = dayMeals

        val updatedPlan = currentPlan.copy(meals = updatedMeals)
        mealPlanRepository.saveMealPlan(updatedPlan)
    }

    suspend fun removeMeal(dayOfWeek: DayOfWeek, meal: PlannedMeal) {
        val currentPlan = _currentMealPlan.value ?: return
        val updatedMeals = currentPlan.meals.toMutableMap()
        val dayMeals = updatedMeals[dayOfWeek]?.toMutableList() ?: return
        dayMeals.remove(meal)
        updatedMeals[dayOfWeek] = dayMeals

        val updatedPlan = currentPlan.copy(meals = updatedMeals)
        mealPlanRepository.saveMealPlan(updatedPlan)
    }

    suspend fun updatePreferences(newPreferences: MealPlanPreferences) {
        mealPlanRepository.updatePreferences(newPreferences)
    }

    suspend fun generateMealPlan() {
        val currentPlan = _currentMealPlan.value ?: createEmptyMealPlan()
        val updatedMeals = currentPlan.meals.toMutableMap()
        val prefs = _preferences.value

        for (day in DayOfWeek.values()) {
            val dayMeals = mutableListOf<PlannedMeal>()
            val mealTypes = prefs.mealTypesPerDay[day] ?: emptyList()

            for (mealType in mealTypes) {
                val suggestedRecipes = getSuggestedRecipes(prefs)
                if (suggestedRecipes.isNotEmpty()) {
                    val recipe = suggestedRecipes.random()
                    dayMeals.add(
                        PlannedMeal(
                            recipeId = recipe.id,
                            recipeName = recipe.name,
                            mealType = mealType,
                            servings = prefs.defaultServings,
                            notes = ""
                        )
                    )
                }
            }

            updatedMeals[day] = dayMeals
        }

        val updatedPlan = currentPlan.copy(meals = updatedMeals)
        mealPlanRepository.saveMealPlan(updatedPlan)
    }

    private fun createEmptyMealPlan(): MealPlan {
        return MealPlan(
            weekStart = _selectedWeek.value,
            meals = emptyMap(),
            notes = ""
        )
    }

    private suspend fun getSuggestedRecipes(preferences: MealPlanPreferences): List<Recipe> {
        val allRecipes = recipeRepository.getAllRecipes()
        return allRecipes.filter { recipe ->
            // Filtrar por restricciones dietéticas
            val meetsDietaryRestrictions = preferences.dietaryRestrictions.all { restriction ->
                when (restriction) {
                    DietaryRestriction.VEGETARIAN -> recipe.isVegetarian
                    DietaryRestriction.VEGAN -> recipe.isVegan
                    DietaryRestriction.GLUTEN_FREE -> recipe.isGlutenFree
                    DietaryRestriction.DAIRY_FREE -> recipe.isDairyFree
                    DietaryRestriction.NUT_FREE -> recipe.isNutFree
                }
            }

            // Filtrar por categorías preferidas
            val meetsPreferredCategories = preferences.preferredCategories.isEmpty() ||
                    recipe.categories.any { it in preferences.preferredCategories }

            // Excluir recetas no deseadas
            val notExcluded = recipe.id !in preferences.excludedRecipes

            meetsDietaryRestrictions && meetsPreferredCategories && notExcluded
        }
    }
} 