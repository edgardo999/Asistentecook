package com.example.asistentedecocina.presentation.screens.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.asistentedecocina.core.animation.AnimationManager
import com.example.asistentedecocina.core.animation.ChefState
import com.example.asistentedecocina.core.model.ChatMessage
import com.example.asistentedecocina.core.model.MenuItem
import com.example.asistentedecocina.core.model.MenuItems
import com.example.asistentedecocina.core.model.MessageType
import com.example.asistentedecocina.presentation.components.ChatMessageItem
import com.example.asistentedecocina.presentation.components.ChefAnimation
import com.example.asistentedecocina.presentation.components.ChefAnimationWithControls
import com.example.asistentedecocina.presentation.components.DrawerContent
import com.example.asistentedecocina.presentation.components.SpeechRecognitionStatus
import com.example.asistentedecocina.presentation.screens.history.HistoryScreen
import com.example.asistentedecocina.presentation.screens.history.HistoryViewModel
import com.example.asistentedecocina.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToRecipes: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf<MenuItem?>(MenuItems.items.first()) }
    var isListening by remember { mutableStateOf(false) }
    var userInput by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()
    val conversations by viewModel.conversations.collectAsState()

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = { menuItem ->
                    selectedItem = menuItem
                    scope.launch {
                        drawerState.close()
                    }
                },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                selectedItem = selectedItem
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Asistente de Cocina") },
                    actions = {
                        IconButton(onClick = onNavigateToRecipes) {
                            Icon(Icons.Default.MenuBook, "Ver recetas")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Área del chef animado (70% de la pantalla)
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ChefAnimation(
                        isListening = uiState.isListening,
                        isThinking = uiState.speechState is com.example.asistentedecocina.core.speech.SpeechState.ProcessingInput
                    )
                }

                // Área de interacción (30% de la pantalla)
                Surface(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth(),
                    tonalElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Estado del reconocimiento de voz
                        SpeechRecognitionStatus(
                            state = uiState.speechState,
                            onAlternativeSelected = { text ->
                                viewModel.processVoiceInput(text)
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Botones de control
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            // Botón de modo continuo
                            OutlinedIconToggleButton(
                                checked = uiState.isListening,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        viewModel.startVoiceRecognition(continuous = true)
                                    } else {
                                        viewModel.stopVoiceRecognition()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (uiState.isListening) Icons.Default.Hearing else Icons.Default.HearingDisabled,
                                    contentDescription = if (uiState.isListening) "Detener escucha continua" else "Iniciar escucha continua"
                                )
                            }

                            // Botón principal de voz
                            FloatingActionButton(
                                onClick = {
                                    if (!uiState.isListening) {
                                        viewModel.startVoiceRecognition(continuous = false)
                                    } else {
                                        viewModel.stopVoiceRecognition()
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(
                                    imageVector = if (uiState.isListening) Icons.Default.MicOff else Icons.Default.Mic,
                                    contentDescription = if (uiState.isListening) "Detener" else "Hablar"
                                )
                            }

                            // Botón de ayuda
                            OutlinedIconButton(
                                onClick = { viewModel.processVoiceInput("ayuda") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Help,
                                    contentDescription = "Ayuda"
                                )
                            }
                        }

                        // Indicador de estado de conexión
                        if (!uiState.isOnline) {
                            Text(
                                text = "Modo sin conexión",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen({})
    }
} 