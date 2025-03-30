package com.example.asistentedecocina.presentation.components.filters

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategorySelector(
    selectedCategories: Set<String>,
    onCategoriesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "Entrantes",
        "Platos principales",
        "Postres",
        "Bebidas",
        "Sopas",
        "Ensaladas",
        "Aperitivos",
        "Panadería",
        "Salsas",
        "Otros"
    )

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = category in selectedCategories,
                onClick = {
                    val newSelection = if (category in selectedCategories) {
                        selectedCategories - category
                    } else {
                        selectedCategories + category
                    }
                    onCategoriesChange(newSelection)
                },
                label = { Text(category) }
            )
        }
    }
}

@Composable
fun DifficultySelector(
    selectedDifficulties: Set<String>,
    onDifficultiesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val difficulties = listOf("Fácil", "Medio", "Difícil")

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        difficulties.forEach { difficulty ->
            FilterChip(
                selected = difficulty in selectedDifficulties,
                onClick = {
                    val newSelection = if (difficulty in selectedDifficulties) {
                        selectedDifficulties - difficulty
                    } else {
                        selectedDifficulties + difficulty
                    }
                    onDifficultiesChange(newSelection)
                },
                label = { Text(difficulty) }
            )
        }
    }
}

@Composable
fun RangeSelector(
    value: IntRange,
    onValueChange: (IntRange) -> Unit,
    min: Int,
    max: Int,
    step: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value.first.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { newInt ->
                        if (newInt in min..value.last) {
                            onValueChange(newInt..value.last)
                        }
                    }
                },
                label = { Text("Mín") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Text("-")
            OutlinedTextField(
                value = value.last.toString(),
                onValueChange = { newValue ->
                    newValue.toIntOrNull()?.let { newInt ->
                        if (newInt in value.first..max) {
                            onValueChange(value.first..newInt)
                        }
                    }
                },
                label = { Text("Máx") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
    }
}

@Composable
fun IngredientSelector(
    selectedIngredients: Set<String>,
    onIngredientsChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var newIngredient by remember { mutableStateOf("") }
    val commonIngredients = listOf(
        "Arroz", "Pasta", "Pollo", "Carne", "Pescado", "Huevos",
        "Leche", "Queso", "Tomate", "Cebolla", "Ajo", "Aceite",
        "Sal", "Pimienta", "Azúcar", "Harina", "Mantequilla"
    )

    Column(modifier = modifier) {
        // Campo de entrada para nuevos ingredientes
        OutlinedTextField(
            value = newIngredient,
            onValueChange = { newIngredient = it },
            label = { Text("Agregar ingrediente") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (newIngredient.isNotBlank()) {
                            onIngredientsChange(selectedIngredients + newIngredient)
                            newIngredient = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ingredientes seleccionados
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedIngredients.forEach { ingredient ->
                AssistChip(
                    onClick = {
                        onIngredientsChange(selectedIngredients - ingredient)
                    },
                    label = { Text(ingredient) },
                    trailingIcon = {
                        Icon(Icons.Default.Close, contentDescription = "Eliminar")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Ingredientes comunes sugeridos
        Text(
            text = "Ingredientes comunes:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            commonIngredients.forEach { ingredient ->
                if (ingredient !in selectedIngredients) {
                    FilterChip(
                        selected = false,
                        onClick = {
                            onIngredientsChange(selectedIngredients + ingredient)
                        },
                        label = { Text(ingredient) }
                    )
                }
            }
        }
    }
} 