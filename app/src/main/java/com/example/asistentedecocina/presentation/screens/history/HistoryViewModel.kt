package com.example.asistentedecocina.presentation.screens.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentedecocina.data.local.AppDatabase
import com.example.asistentedecocina.data.local.entity.ConversationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val conversationDao = database.conversationDao()

    val conversations: StateFlow<List<ConversationHistory>> = conversationDao
        .getAllConversations()
        .map { entities ->
            entities.map { entity ->
                ConversationHistory(
                    id = entity.id,
                    title = entity.title,
                    lastMessage = entity.lastMessage,
                    timestamp = entity.timestamp,
                    messages = entity.messages
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addConversation(title: String, messages: List<String>) {
        viewModelScope.launch {
            val conversation = ConversationEntity(
                title = title,
                lastMessage = messages.last(),
                messages = messages
            )
            conversationDao.insertConversation(conversation)
        }
    }

    fun deleteConversation(conversation: ConversationHistory) {
        viewModelScope.launch {
            val entity = ConversationEntity(
                id = conversation.id,
                title = conversation.title,
                lastMessage = conversation.lastMessage,
                timestamp = conversation.timestamp,
                messages = conversation.messages
            )
            conversationDao.deleteConversation(entity)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            conversationDao.deleteAllConversations()
        }
    }
} 