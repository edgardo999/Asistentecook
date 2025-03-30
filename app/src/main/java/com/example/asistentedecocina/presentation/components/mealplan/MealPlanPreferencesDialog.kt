package com.example.asistentedecocina.presentation.components.mealplan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.data.model.DietaryRestriction
import com.example.asistentedecocina.data.model.MealPlanPreferences
import com.example.asistentedecocina.data.model.MealType
import java.time.DayOfWeek

@Composable
fun MealPlanPreferencesDialog(
    currentPreferences: MealPlanPreferences,
    onDismiss: () -> Unit,
    onPreferencesChange: (MealPlanPreferences) -> Unit
) {
    var defaultServings by remember { mutableStateOf(currentPreferences.defaultServings) }
    var mealTypesPerDay by remember { mutableStateOf(currentPreferences.mealTypesPerDay) }
    var excludedRecipes by remember { mutableStateOf(currentPreferences.excludedRecipes) }
    var preferredCategories by remember { mutableStateOf(currentPreferences.preferredCategories) }
    var dietaryRestrictions by remember { mutableStateOf(currentPreferences.dietaryRestrictions) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Preferencias del plan") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Porciones por defecto
                    PreferenceSection(
                        title = "Porciones por defecto",
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Número de porciones")
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { if (defaultServings > 1) defaultServings-- }
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Reducir")
                                    }
                                    Text(
                                        text = defaultServings.toString(),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    IconButton(
                                        onClick = { defaultServings++ }
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                    }
                                }
                            }
                        }
                    )
                }

                item {
                    // Tipos de comida por día
                    PreferenceSection(
                        title = "Tipos de comida por día",
                        content = {
                            DayOfWeek.values().forEach { day ->
                                DayMealTypesSelector(
                                    day = day,
                                    selectedTypes = mealTypesPerDay[day] ?: emptySet(),
                                    onTypesChange = { types ->
                                        mealTypesPerDay = mealTypesPerDay + (day to types)
                                    }
                                )
                            }
                        }
                    )
                }

                item {
                    // Restricciones dietéticas
                    PreferenceSection(
                        title = "Restricciones dietéticas",
                        content = {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DietaryRestriction.values().forEach { restriction ->
                                    FilterChip(
                                        selected = restriction in dietaryRestrictions,
                                        onClick = {
                                            dietaryRestrictions = if (restriction in dietaryRestrictions) {
                                                dietaryRestrictions - restriction
                                            } else {
                                                dietaryRestrictions + restriction
                                            }
                                        },
                                        label = {
                                            Text(
                                                when (restriction) {
                                                    DietaryRestriction.VEGETARIANO -> "Vegetariano"
                                                    DietaryRestriction.VEGANO -> "Vegano"
                                                    DietaryRestriction.SIN_GLUTEN -> "Sin gluten"
                                                    DietaryRestriction.SIN_LACTOSA -> "Sin lactosa"
                                                    DietaryRestriction.BAJO_EN_CALORIAS -> "Bajo en calorías"
                                                    DietaryRestriction.ALTO_EN_PROTEINAS -> "Alto en proteínas"
                                                }
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    )
                }

                item {
                    // Categorías preferidas
                    PreferenceSection(
                        title = "Categorías preferidas",
                        content = {
                            val categories = listOf(
                                "Entrantes",
                                "Platos principales",
                                "Postres",
                                "Bebidas",
                                "Sopas",
                                "Ensaladas",
                                "Aperitivos",
                                "Panadería",
                                "Salsas"
                            )
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                categories.forEach { category ->
                                    FilterChip(
                                        selected = category in preferredCategories,
                                        onClick = {
                                            preferredCategories = if (category in preferredCategories) {
                                                preferredCategories - category
                                            } else {
                                                preferredCategories + category
                                            }
                                        },
                                        label = { Text(category) }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPreferencesChange(
                        MealPlanPreferences(
                            defaultServings = defaultServings,
                            mealTypesPerDay = mealTypesPerDay,
                            excludedRecipes = excludedRecipes,
                            preferredCategories = preferredCategories,
                            dietaryRestrictions = dietaryRestrictions
                        )
                    )
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun PreferenceSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun DayMealTypesSelector(
    day: DayOfWeek,
    selectedTypes: Set<MealType>,
    onTypesChange: (Set<MealType>) -> Unit
) {
    Column {
        Text(
            text = day.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MealType.values().forEach { mealType ->
                FilterChip(
                    selected = mealType in selectedTypes,
                    onClick = {
                        val newTypes = if (mealType in selectedTypes) {
                            selectedTypes - mealType
                        } else {
                            selectedTypes + mealType
                        }
                        onTypesChange(newTypes)
                    },
                    label = {
                        Text(
                            when (mealType) {
                                MealType.DESAYUNO -> "Desayuno"
                                MealType.ALMUERZO -> "Almuerzo"
                                MealType.CENA -> "Cena"
                                MealType.MERIENDA -> "Merienda"
                            }
                        )
                    }
                )
            }
        }
    }
} 