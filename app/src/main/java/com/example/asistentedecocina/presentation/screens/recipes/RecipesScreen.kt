package com.example.asistentedecocina.presentation.screens.recipes

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asistentedecocina.data.local.entity.RecipeCategory
import com.example.asistentedecocina.data.local.entity.RecipeDifficulty
import com.example.asistentedecocina.data.local.entity.RecipeEntity
import com.example.asistentedecocina.presentation.screens.recipes.detail.RecipeDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    onAddRecipe: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipesViewModel = viewModel()
) {
    var selectedRecipe by remember { mutableStateOf<RecipeEntity?>(null) }
    val recipes by viewModel.recipes.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    if (selectedRecipe != null) {
        RecipeDetailScreen(
            recipe = selectedRecipe!!,
            onBackClick = { selectedRecipe = null },
            onEditClick = { /* TODO: Implementar edición */ },
            onFavoriteClick = { 
                viewModel.toggleFavorite(selectedRecipe!!.id, !selectedRecipe!!.isFavorite)
                selectedRecipe = selectedRecipe!!.copy(isFavorite = !selectedRecipe!!.isFavorite)
            }
        )
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = { 
                    searchQuery = it
                    viewModel.setSearchQuery(it)
                },
                onSearch = { viewModel.setSearchQuery(it) },
                active = false,
                onActiveChange = {},
                placeholder = { Text("Buscar recetas...") },
                leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
                trailingIcon = {
                    Row {
                        IconButton(onClick = { showFilters = !showFilters }) {
                            Icon(Icons.Default.FilterList, "Filtros")
                        }
                        IconButton(onClick = { viewModel.toggleFavorites() }) {
                            Icon(Icons.Default.Favorite, "Favoritos")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {}

            // Filtros
            if (showFilters) {
                FilterSection(
                    onCategorySelected = { viewModel.setCategory(it) },
                    onDifficultySelected = { viewModel.setDifficulty(it) }
                )
            }

            // Grid de recetas
            if (recipes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay recetas disponibles",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { selectedRecipe = recipe },
                            onFavoriteClick = { viewModel.toggleFavorite(recipe.id, !recipe.isFavorite) }
                        )
                    }
                }
            }

            // Botón flotante para agregar receta
            FloatingActionButton(
                onClick = onAddRecipe,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, "Agregar receta")
            }
        }
    }
}

@Composable
private fun FilterSection(
    onCategorySelected: (RecipeCategory?) -> Unit,
    onDifficultySelected: (RecipeDifficulty?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(100.dp)
        ) {
            items(RecipeCategory.values()) { category ->
                FilterChip(
                    selected = false,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.name) }
                )
            }
        }

        Text(
            text = "Dificultad",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RecipeDifficulty.values().forEach { difficulty ->
                FilterChip(
                    selected = false,
                    onClick = { onDifficultySelected(difficulty) },
                    label = { Text(difficulty.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeCard(
    recipe: RecipeEntity,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            // TODO: Implementar carga de imágenes
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${recipe.preparationTime} min",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (recipe.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
} 