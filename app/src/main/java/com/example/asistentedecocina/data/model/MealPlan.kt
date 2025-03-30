package com.example.asistentedecocina.data.model

import java.time.DayOfWeek
import java.time.LocalDate

data class MealPlan(
    val id: String = "",
    val weekStartDate: LocalDate,
    val meals: Map<DayOfWeek, List<PlannedMeal>> = emptyMap(),
    val notes: String = ""
)

data class PlannedMeal(
    val id: String = "",
    val recipeId: String,
    val recipe: Recipe,
    val mealType: MealType,
    val servings: Int,
    val notes: String = ""
)

enum class MealType {
    DESAYUNO,
    ALMUERZO,
    CENA,
    MERIENDA
}

data class MealPlanPreferences(
    val defaultServings: Int = 4,
    val mealTypesPerDay: Map<DayOfWeek, Set<MealType>> = DayOfWeek.values().associateWith { 
        setOf(MealType.DESAYUNO, MealType.ALMUERZO, MealType.CENA) 
    },
    val excludedRecipes: Set<String> = emptySet(),
    val preferredCategories: Set<String> = emptySet(),
    val dietaryRestrictions: Set<DietaryRestriction> = emptySet()
)

enum class DietaryRestriction {
    VEGETARIANO,
    VEGANO,
    SIN_GLUTEN,
    SIN_LACTOSA,
    BAJO_EN_CALORIAS,
    ALTO_EN_PROTEINAS
} 