package com.example.asistentedecocina.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.core.model.RecipeCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: RecipeCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(category.isFavorite) }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = Color(category.color).copy(alpha = 0.1f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icono
                Icon(
                    imageVector = Icons.Default.Restaurant, // TODO: Implementar selección dinámica de iconos
                    contentDescription = null,
                    tint = Color(category.color),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nombre
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Descripción
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                // Contador de recetas
                Text(
                    text = "${category.recipeCount} recetas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Botón de favorito
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) 
                            Icons.Default.Favorite 
                        else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) 
                            "Quitar de favoritos" 
                        else "Añadir a favoritos",
                        tint = if (isFavorite) 
                            MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 