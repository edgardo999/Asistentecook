package com.example.asistentedecocina.presentation.screens.categories

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.core.model.RecipeCategory
import com.example.asistentedecocina.presentation.components.CategoryCard
import com.example.asistentedecocina.presentation.components.SearchBar
import com.example.asistentedecocina.presentation.components.CategoryFilterChips

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    categories: List<RecipeCategory>,
    onCategoryClick: (RecipeCategory) -> Unit,
    onSearch: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddCategory by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todas") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías") },
                actions = {
                    IconButton(onClick = { showAddCategory = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir categoría")
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
                onQueryChange = { 
                    searchQuery = it
                    onSearch(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Filtros
            CategoryFilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { 
                    selectedFilter = it
                    onFilterChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Grid de categorías
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onCategoryClick(category) }
                    )
                }
            }
        }

        // Diálogo para añadir categoría
        if (showAddCategory) {
            AddCategoryDialog(
                onDismiss = { showAddCategory = false },
                onConfirm = { name, description, icon, color ->
                    // TODO: Implementar la lógica para añadir categoría
                    showAddCategory = false
                }
            )
        }
    }
}

@Composable
private fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("restaurant") }
    var selectedColor by remember { mutableStateOf(Color(0xFF2196F3).value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Categoría") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                // TODO: Implementar selector de iconos
                
                // TODO: Implementar selector de colores
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(name, description, selectedIcon, selectedColor)
                },
                enabled = name.isNotBlank()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
} 