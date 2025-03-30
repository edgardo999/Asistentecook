package com.example.asistentedecocina.presentation.screens.tips

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.core.model.CulinaryTip
import com.example.asistentedecocina.core.model.TipCategory
import com.example.asistentedecocina.presentation.components.CulinaryTipCard
import com.example.asistentedecocina.presentation.components.SearchBar
import com.example.asistentedecocina.presentation.components.CategoryFilterChips

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulinaryTipsScreen(
    tips: List<CulinaryTip>,
    onTipClick: (CulinaryTip) -> Unit,
    onSearch: (String) -> Unit,
    onFilterChange: (TipCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TipCategory.GENERAL) }
    var showRandomTip by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consejos Culinarios") },
                actions = {
                    IconButton(onClick = { showRandomTip = true }) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Consejo aleatorio")
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

            // Filtros de categoría
            CategoryFilterChips(
                selectedFilter = selectedCategory.name,
                onFilterSelected = { 
                    selectedCategory = TipCategory.valueOf(it)
                    onFilterChange(selectedCategory)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Lista de consejos
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tips) { tip ->
                    CulinaryTipCard(
                        tip = tip,
                        onClick = { onTipClick(tip) }
                    )
                }
            }
        }

        // Diálogo de consejo aleatorio
        if (showRandomTip) {
            RandomTipDialog(
                tip = tips.random(),
                onDismiss = { showRandomTip = false }
            )
        }
    }
}

@Composable
private fun RandomTipDialog(
    tip: CulinaryTip,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(tip.title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(tip.content)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { },
                        label = { Text(tip.category.name) },
                        leadingIcon = {
                            Icon(
                                imageVector = when (tip.category) {
                                    TipCategory.TECNICAS_BASICAS -> Icons.Default.Restaurant
                                    TipCategory.INGREDIENTES -> Icons.Default.LocalGroceryStore
                                    TipCategory.EQUIPAMIENTO -> Icons.Default.Kitchen
                                    TipCategory.SEGURIDAD -> Icons.Default.Security
                                    TipCategory.CONSERVACION -> Icons.Default.Refresh
                                    TipCategory.PRESENTACION -> Icons.Default.Palette
                                    TipCategory.NUTRICION -> Icons.Default.Favorite
                                    TipCategory.GENERAL -> Icons.Default.Info
                                },
                                contentDescription = null
                            )
                        }
                    )
                    
                    AssistChip(
                        onClick = { },
                        label = { Text(tip.difficulty.name) },
                        leadingIcon = {
                            Icon(
                                imageVector = when (tip.difficulty) {
                                    TipDifficulty.PRINCIPIANTE -> Icons.Default.Star
                                    TipDifficulty.INTERMEDIO -> Icons.Default.Star
                                    TipDifficulty.AVANZADO -> Icons.Default.Star
                                },
                                contentDescription = null
                            )
                        }
                    )
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