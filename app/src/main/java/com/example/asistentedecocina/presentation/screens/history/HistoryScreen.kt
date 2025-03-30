package com.example.asistentedecocina.presentation.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class ConversationHistory(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val lastMessage: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messages: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    conversations: List<ConversationHistory>,
    onDeleteConversation: (ConversationHistory) -> Unit,
    onConversationClick: (ConversationHistory) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (conversations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay conversaciones guardadas",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(conversations) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onDelete = { onDeleteConversation(conversation) },
                        onClick = { onConversationClick(conversation) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationItem(
    conversation: ConversationHistory,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(Date(conversation.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar conversación",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun HistoryScreenPreview() {
    val sampleConversations = listOf(
        ConversationHistory(
            title = "Receta de Paella",
            lastMessage = "Aquí tienes los ingredientes principales para la paella...",
            messages = listOf(
                "¿Cómo hago una paella?",
                "Aquí tienes los ingredientes principales para la paella..."
            )
        ),
        ConversationHistory(
            title = "Pasta Carbonara",
            lastMessage = "Para hacer una auténtica carbonara italiana...",
            messages = listOf(
                "¿Cuál es la receta original de la carbonara?",
                "Para hacer una auténtica carbonara italiana..."
            )
        )
    )

    MaterialTheme {
        HistoryScreen(
            conversations = sampleConversations,
            onDeleteConversation = {},
            onConversationClick = {}
        )
    }
} 