package com.example.asistentedecocina.presentation.screens.mealplan

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
import com.example.asistentedecocina.data.model.MealPlan
import com.example.asistentedecocina.data.model.MealType
import com.example.asistentedecocina.data.model.PlannedMeal
import com.example.asistentedecocina.data.model.Recipe
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    currentPlan: MealPlan?,
    onPlanChange: (MealPlan) -> Unit,
    onRecipeSelect: (DayOfWeek, MealType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedWeek by remember { mutableStateOf(LocalDate.now()) }
    var showWeekPicker by remember { mutableStateOf(false) }
    var showPreferences by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan de Menú Semanal") },
                actions = {
                    IconButton(onClick = { showPreferences = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Preferencias")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showWeekPicker = true }
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Cambiar semana")
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Selector de semana
            WeekSelector(
                selectedWeek = selectedWeek,
                onWeekChange = { selectedWeek = it }
            )

            // Lista de días
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(DayOfWeek.values()) { day ->
                    DayMealPlan(
                        day = day,
                        meals = currentPlan?.meals?.get(day) ?: emptyList(),
                        onMealClick = { mealType ->
                            onRecipeSelect(day, mealType)
                        }
                    )
                }
            }
        }

        // Diálogo de selección de semana
        if (showWeekPicker) {
            WeekPickerDialog(
                currentWeek = selectedWeek,
                onDismiss = { showWeekPicker = false },
                onWeekSelected = { week ->
                    selectedWeek = week
                    showWeekPicker = false
                }
            )
        }

        // Diálogo de preferencias
        if (showPreferences) {
            MealPlanPreferencesDialog(
                currentPreferences = MealPlanPreferences(),
                onDismiss = { showPreferences = false },
                onPreferencesChange = { /* TODO: Implementar cambios de preferencias */ }
            )
        }
    }
}

@Composable
private fun WeekSelector(
    selectedWeek: LocalDate,
    onWeekChange: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onWeekChange(selectedWeek.minusWeeks(1)) }
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Semana anterior")
        }

        Text(
            text = "Semana del ${selectedWeek.format(java.time.format.DateTimeFormatter.ofPattern("d MMM"))}",
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(
            onClick = { onWeekChange(selectedWeek.plusWeeks(1)) }
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Siguiente semana")
        }
    }
}

@Composable
private fun DayMealPlan(
    day: DayOfWeek,
    meals: List<PlannedMeal>,
    onMealClick: (MealType) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = day.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            MealType.values().forEach { mealType ->
                val meal = meals.find { it.mealType == mealType }
                MealSlot(
                    mealType = mealType,
                    plannedMeal = meal,
                    onClick = { onMealClick(mealType) }
                )
            }
        }
    }
}

@Composable
private fun MealSlot(
    mealType: MealType,
    plannedMeal: PlannedMeal?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (mealType) {
                MealType.DESAYUNO -> "Desayuno"
                MealType.ALMUERZO -> "Almuerzo"
                MealType.CENA -> "Cena"
                MealType.MERIENDA -> "Merienda"
            },
            style = MaterialTheme.typography.bodyLarge
        )

        if (plannedMeal != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = plannedMeal.recipe.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "(${plannedMeal.servings} porciones)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            Text(
                text = "Agregar receta",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun WeekPickerDialog(
    currentWeek: LocalDate,
    onDismiss: () -> Unit,
    onWeekSelected: (LocalDate) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar semana") },
        text = {
            DatePicker(
                selectedDate = currentWeek,
                onDateSelected = { date ->
                    // Ajustar al inicio de la semana
                    val weekStart = date.minusDays(date.dayOfWeek.value.toLong() - 1)
                    onWeekSelected(weekStart)
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun MealPlanPreferencesDialog(
    currentPreferences: MealPlanPreferences,
    onDismiss: () -> Unit,
    onPreferencesChange: (MealPlanPreferences) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Preferencias del plan") },
        text = {
            Column {
                // TODO: Implementar preferencias
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
} 