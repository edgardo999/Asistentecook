package com.example.asistentedecocina.presentation.components

import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulinaryTipCard(
    tip: CulinaryTip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(tip.isFavorite) }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado con título y botón de favorito
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tip.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(
                    onClick = { isFavorite = !isFavorite }
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

            Spacer(modifier = Modifier.height(8.dp))

            // Contenido del consejo
            Text(
                text = tip.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Etiquetas y categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Categoría
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

                // Dificultad
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

                // Tags
                tip.tags.forEach { tag ->
                    AssistChip(
                        onClick = { },
                        label = { Text(tag) }
                    )
                }
            }
        }
    }
} 