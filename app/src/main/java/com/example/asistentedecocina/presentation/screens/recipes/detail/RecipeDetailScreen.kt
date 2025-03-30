package com.example.asistentedecocina.presentation.screens.recipes.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.data.local.entity.RecipeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: RecipeEntity,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onStartCooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showStartCookingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (recipe.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, "Editar receta")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showStartCookingDialog = true },
                icon = { Icon(Icons.Default.PlayArrow, "Iniciar cocina") },
                text = { Text("Empezar a cocinar") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Imagen de la receta
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Implementar carga de imágenes
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Información general
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoItem(
                            icon = Icons.Default.Timer,
                            label = "Tiempo",
                            value = "${recipe.preparationTime} min"
                        )
                        InfoItem(
                            icon = Icons.Default.Person,
                            label = "Porciones",
                            value = "${recipe.servings}"
                        )
                        InfoItem(
                            icon = Icons.Default.Assignment,
                            label = "Dificultad",
                            value = recipe.difficulty.name
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                }
            }

            // Ingredientes
            item {
                SectionTitle(text = "Ingredientes")
            }

            items(recipe.ingredients) { ingredient ->
                ListItem(
                    leadingContent = {
                        Icon(Icons.Default.FiberManualRecord, null, Modifier.size(8.dp))
                    },
                    headlineContent = { Text(ingredient) }
                )
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                SectionTitle(text = "Instrucciones")
            }

            items(recipe.instructions.withIndex().toList()) { (index, instruction) ->
                ListItem(
                    leadingContent = {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    headlineContent = { Text(instruction) }
                )
            }

            // Espacio al final
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showStartCookingDialog) {
        AlertDialog(
            onDismissRequest = { showStartCookingDialog = false },
            title = { Text("¿Listo para empezar?") },
            text = { 
                Text(
                    "Te guiaré paso a paso en la preparación de ${recipe.title}. " +
                    "Asegúrate de tener todos los ingredientes listos."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showStartCookingDialog = false
                        onStartCooking()
                    }
                ) {
                    Text("Empezar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartCookingDialog = false }) {
                    Text("Cancelar")
                }
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
} 