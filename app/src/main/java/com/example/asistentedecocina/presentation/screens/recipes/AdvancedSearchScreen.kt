package com.example.asistentedecocina.presentation.screens.recipes

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
import com.example.asistentedecocina.core.search.RecipeSearchManager
import com.example.asistentedecocina.core.search.SearchFilters
import com.example.asistentedecocina.core.search.SortOption
import com.example.asistentedecocina.data.model.Recipe
import com.example.asistentedecocina.presentation.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSearchScreen(
    searchManager: RecipeSearchManager,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilters by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    
    val searchQuery by searchManager.searchQuery.collectAsState()
    val filters by searchManager.filters.collectAsState()
    val searchResults by searchManager.searchResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Búsqueda Avanzada") },
                actions = {
                    IconButton(onClick = { showSortDialog = true }) {
                        Icon(Icons.Default.Sort, contentDescription = "Ordenar")
                    }
                    IconButton(onClick = { showFilters = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchManager.updateSearchQuery(it) },
                onSearch = { },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Buscar recetas...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchManager.clearSearch() }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) { }

            // Resultados de búsqueda
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe) }
                    )
                }
            }
        }

        // Diálogo de filtros
        if (showFilters) {
            FilterDialog(
                currentFilters = filters,
                onDismiss = { showFilters = false },
                onApplyFilters = { newFilters ->
                    searchManager.updateFilters(newFilters)
                    showFilters = false
                },
                onClearFilters = {
                    searchManager.clearFilters()
                    showFilters = false
                }
            )
        }

        // Diálogo de ordenamiento
        if (showSortDialog) {
            SortDialog(
                currentSort = filters.sortBy,
                onDismiss = { showSortDialog = false },
                onSortSelected = { sortOption ->
                    searchManager.updateFilters(filters.copy(sortBy = sortOption))
                    showSortDialog = false
                }
            )
        }
    }
}

@Composable
private fun FilterDialog(
    currentFilters: SearchFilters,
    onDismiss: () -> Unit,
    onApplyFilters: (SearchFilters) -> Unit,
    onClearFilters: () -> Unit
) {
    var categories by remember { mutableStateOf(currentFilters.categories) }
    var difficulty by remember { mutableStateOf(currentFilters.difficulty) }
    var cookingTimeRange by remember { mutableStateOf(currentFilters.cookingTime) }
    var servingsRange by remember { mutableStateOf(currentFilters.servings) }
    var ingredients by remember { mutableStateOf(currentFilters.ingredients) }
    var excludeIngredients by remember { mutableStateOf(currentFilters.excludeIngredients) }
    var isVegetarian by remember { mutableStateOf(currentFilters.isVegetarian) }
    var isVegan by remember { mutableStateOf(currentFilters.isVegan) }
    var isGlutenFree by remember { mutableStateOf(currentFilters.isGlutenFree) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtros de búsqueda") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Categorías
                    FilterSection(
                        title = "Categorías",
                        content = {
                            CategorySelector(
                                selectedCategories = categories,
                                onCategoriesChange = { categories = it }
                            )
                        }
                    )
                }

                item {
                    // Dificultad
                    FilterSection(
                        title = "Dificultad",
                        content = {
                            DifficultySelector(
                                selectedDifficulties = difficulty,
                                onDifficultiesChange = { difficulty = it }
                            )
                        }
                    )
                }

                item {
                    // Tiempo de cocción
                    FilterSection(
                        title = "Tiempo de cocción (minutos)",
                        content = {
                            RangeSelector(
                                value = cookingTimeRange,
                                onValueChange = { cookingTimeRange = it },
                                min = 0,
                                max = 480,
                                step = 5,
                                label = "Rango de tiempo"
                            )
                        }
                    )
                }

                item {
                    // Porciones
                    FilterSection(
                        title = "Porciones",
                        content = {
                            RangeSelector(
                                value = servingsRange,
                                onValueChange = { servingsRange = it },
                                min = 1,
                                max = 50,
                                step = 1,
                                label = "Rango de porciones"
                            )
                        }
                    )
                }

                item {
                    // Ingredientes incluidos
                    FilterSection(
                        title = "Ingredientes incluidos",
                        content = {
                            IngredientSelector(
                                selectedIngredients = ingredients,
                                onIngredientsChange = { ingredients = it }
                            )
                        }
                    )
                }

                item {
                    // Ingredientes excluidos
                    FilterSection(
                        title = "Ingredientes excluidos",
                        content = {
                            IngredientSelector(
                                selectedIngredients = excludeIngredients,
                                onIngredientsChange = { excludeIngredients = it }
                            )
                        }
                    )
                }

                item {
                    // Opciones dietéticas
                    FilterSection(
                        title = "Opciones dietéticas",
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                FilterChip(
                                    selected = isVegetarian == true,
                                    onClick = { isVegetarian = if (isVegetarian == true) null else true },
                                    label = { Text("Vegetariano") }
                                )
                                FilterChip(
                                    selected = isVegan == true,
                                    onClick = { isVegan = if (isVegan == true) null else true },
                                    label = { Text("Vegano") }
                                )
                                FilterChip(
                                    selected = isGlutenFree == true,
                                    onClick = { isGlutenFree = if (isGlutenFree == true) null else true },
                                    label = { Text("Sin gluten") }
                                )
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApplyFilters(
                        SearchFilters(
                            categories = categories,
                            difficulty = difficulty,
                            cookingTime = cookingTimeRange,
                            servings = servingsRange,
                            ingredients = ingredients,
                            excludeIngredients = excludeIngredients,
                            isVegetarian = isVegetarian,
                            isVegan = isVegan,
                            isGlutenFree = isGlutenFree,
                            sortBy = currentFilters.sortBy
                        )
                    )
                }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onClearFilters) {
                    Text("Limpiar")
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

@Composable
private fun SortDialog(
    currentSort: SortOption,
    onDismiss: () -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ordenar por") },
        text = {
            Column {
                SortOption.values().forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortSelected(option) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSort == option,
                            onClick = { onSortSelected(option) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (option) {
                                SortOption.NAME -> "Nombre"
                                SortOption.COOKING_TIME -> "Tiempo de cocción"
                                SortOption.DIFFICULTY -> "Dificultad"
                                SortOption.RATING -> "Calificación"
                                SortOption.DATE_ADDED -> "Fecha de agregado"
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
private fun FilterSection(
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