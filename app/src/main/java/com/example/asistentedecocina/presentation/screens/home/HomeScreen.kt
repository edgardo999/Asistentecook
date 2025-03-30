package com.example.asistentedecocina.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.asistentedecocina.core.model.AvatarEmotion
import com.example.asistentedecocina.presentation.components.Avatar
import com.example.asistentedecocina.presentation.viewmodel.AvatarViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AvatarViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Chef Asistente",
            style = MaterialTheme.typography.headlineMedium
        )

        Avatar(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )

        // Controles de estado básico
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.startListening() }) {
                Text("Escuchar")
            }
            Button(onClick = { viewModel.startSpeaking() }) {
                Text("Hablar")
            }
            Button(onClick = { viewModel.startThinking() }) {
                Text("Pensar")
            }
        }

        // Controles de emociones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.setEmotion(AvatarEmotion.HAPPY) }) {
                Text("Feliz")
            }
            Button(onClick = { viewModel.setEmotion(AvatarEmotion.CONFUSED) }) {
                Text("Confundido")
            }
            Button(onClick = { viewModel.setEmotion(AvatarEmotion.NEUTRAL) }) {
                Text("Neutral")
            }
        }

        // Controles de acciones de cocina
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.startCooking() }) {
                Text("Cocinar")
            }
            Button(onClick = { viewModel.startChopping() }) {
                Text("Cortar")
            }
            Button(onClick = { viewModel.startMixing() }) {
                Text("Mezclar")
            }
        }

        Button(
            onClick = { viewModel.presentDish() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Presentar Plato")
        }
    }
} 