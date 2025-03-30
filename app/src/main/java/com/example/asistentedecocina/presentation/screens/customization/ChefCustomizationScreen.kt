package com.example.asistentedecocina.presentation.screens.customization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.asistentedecocina.core.model.ChefColorPalettes
import com.example.asistentedecocina.core.model.ChefStyle
import com.example.asistentedecocina.presentation.components.ChefAnimation
import com.example.asistentedecocina.presentation.viewmodel.ChefCustomizationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefCustomizationScreen(
    onNavigateBack: () -> Unit,
    viewModel: ChefCustomizationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showColorPicker by remember { mutableStateOf(false) }
    var currentColorTarget by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personalizar Chef") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetToDefaults() }) {
                        Icon(Icons.Default.RestartAlt, "Restablecer")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Vista previa del chef
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                ChefAnimation(
                    isListening = false,
                    isThinking = false,
                    preferences = uiState.preferences
                )
            }

            // Sección de estilos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Estilo",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ChefStyle.values().forEach { style ->
                            FilterChip(
                                selected = uiState.preferences.style == style,
                                onClick = { viewModel.updateStyle(style) },
                                label = { Text(style.name.lowercase().capitalize()) }
                            )
                        }
                    }
                }
            }

            // Sección de colores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Colores",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    // Paletas predefinidas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ColorPaletteButton(
                            palette = ChefColorPalettes.Classic,
                            onClick = { viewModel.applyPalette(ChefColorPalettes.Classic) }
                        )
                        ColorPaletteButton(
                            palette = ChefColorPalettes.Modern,
                            onClick = { viewModel.applyPalette(ChefColorPalettes.Modern) }
                        )
                        ColorPaletteButton(
                            palette = ChefColorPalettes.Playful,
                            onClick = { viewModel.applyPalette(ChefColorPalettes.Playful) }
                        )
                        ColorPaletteButton(
                            palette = ChefColorPalettes.Natural,
                            onClick = { viewModel.applyPalette(ChefColorPalettes.Natural) }
                        )
                        ColorPaletteButton(
                            palette = ChefColorPalettes.Dark,
                            onClick = { viewModel.applyPalette(ChefColorPalettes.Dark) }
                        )
                    }

                    // Colores individuales
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ColorRow(
                            label = "Color principal",
                            color = uiState.preferences.primaryColor,
                            onColorClick = {
                                currentColorTarget = "primary"
                                showColorPicker = true
                            }
                        )
                        ColorRow(
                            label = "Color secundario",
                            color = uiState.preferences.secondaryColor,
                            onColorClick = {
                                currentColorTarget = "secondary"
                                showColorPicker = true
                            }
                        )
                        ColorRow(
                            label = "Color del gorro",
                            color = uiState.preferences.hatColor,
                            onColorClick = {
                                currentColorTarget = "hat"
                                showColorPicker = true
                            }
                        )
                        ColorRow(
                            label = "Color del delantal",
                            color = uiState.preferences.apronColor,
                            onColorClick = {
                                currentColorTarget = "apron"
                                showColorPicker = true
                            }
                        )
                    }
                }
            }

            // Sección de animación
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Animación",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    // Velocidad de animación
                    Column {
                        Text(
                            text = "Velocidad: ${uiState.preferences.animationSpeed}x",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = uiState.preferences.animationSpeed,
                            onValueChange = { viewModel.updateAnimationSpeed(it) },
                            valueRange = 0.5f..2.0f,
                            steps = 5
                        )
                    }

                    // Expresividad
                    Column {
                        Text(
                            text = "Expresividad: ${uiState.preferences.expressiveness}x",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = uiState.preferences.expressiveness,
                            onValueChange = { viewModel.updateExpressiveness(it) },
                            valueRange = 0.5f..2.0f,
                            steps = 5
                        )
                    }

                    // Tamaño
                    Column {
                        Text(
                            text = "Tamaño: ${uiState.preferences.size}x",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = uiState.preferences.size,
                            onValueChange = { viewModel.updateSize(it) },
                            valueRange = 0.5f..2.0f,
                            steps = 5
                        )
                    }
                }
            }
        }
    }

    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Seleccionar color") },
            text = {
                // TODO: Implementar selector de color personalizado
                Text("Selector de color pendiente de implementación")
            },
            confirmButton = {
                TextButton(onClick = { showColorPicker = false }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showColorPicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ColorPaletteButton(
    palette: com.example.asistentedecocina.core.model.ChefPreferences,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(palette.primaryColor)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(palette.secondaryColor)
            )
        }
    }
}

@Composable
private fun ColorRow(
    label: String,
    color: Color,
    onColorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
                .clickable(onClick = onColorClick)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )
    }
} 