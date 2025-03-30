package com.example.asistentedecocina.presentation.screens.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.asistentedecocina.core.constants.AnimationConstants
import com.example.asistentedecocina.presentation.components.AnimationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationTestScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Estados", "Emociones", "Acciones")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Prueba de Animaciones") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> StatesTab()
            1 -> EmotionsTab()
            2 -> ActionsTab()
        }
    }
}

@Composable
private fun StatesTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            listOf(
                "idle" to "Estado Normal",
                "listening" to "Escuchando",
                "speaking" to "Hablando",
                "thinking" to "Pensando"
            )
        ) { (animation, title) ->
            AnimationPreview(
                animationRes = "animations/states/chef_$animation.json",
                title = title,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
        }
    }
}

@Composable
private fun EmotionsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            listOf(
                "happy" to "Feliz",
                "sad" to "Triste",
                "surprised" to "Sorprendido",
                "confused" to "Confundido",
                "neutral" to "Neutral"
            )
        ) { (animation, title) ->
            AnimationPreview(
                animationRes = "animations/emotions/chef_$animation.json",
                title = title,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
        }
    }
}

@Composable
private fun ActionsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            listOf(
                "cooking" to "Cocinando",
                "chopping" to "Cortando",
                "mixing" to "Mezclando",
                "presenting" to "Presentando"
            )
        ) { (animation, title) ->
            AnimationPreview(
                animationRes = "animations/actions/chef_$animation.json",
                title = title,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
        }
    }
} 