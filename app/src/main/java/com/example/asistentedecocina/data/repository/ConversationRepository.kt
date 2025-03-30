package com.example.asistentedecocina.data.repository

import com.example.asistentedecocina.core.connectivity.ConnectivityManager
import com.example.asistentedecocina.data.local.dao.ConversationDao
import com.example.asistentedecocina.data.local.entity.ConversationEntity
import com.example.asistentedecocina.core.model.ChatMessage
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val connectivityManager: ConnectivityManager
) {
    val conversations = conversationDao.getAllConversations()
    val isOnline = connectivityManager.isNetworkAvailable

    suspend fun saveConversation(messages: List<ChatMessage>) {
        val conversation = ConversationEntity(
            messages = messages,
            isSynced = isOnline.first()
        )
        conversationDao.insertConversation(conversation)
    }

    suspend fun getConversation(id: Long): ConversationEntity? {
        return conversationDao.getConversationById(id)
    }

    suspend fun syncPendingConversations() {
        if (isOnline.first()) {
            val unsyncedConversations = conversationDao.getUnsyncedConversations()
            unsyncedConversations.forEach { conversation ->
                // TODO: Implementar sincronización con el servidor
                val syncedConversation = conversation.copy(isSynced = true)
                conversationDao.updateConversation(syncedConversation)
            }
        }
    }

    suspend fun cleanOldConversations(maxAgeInMillis: Long) {
        val cutoffTimestamp = System.currentTimeMillis() - maxAgeInMillis
        conversationDao.deleteOldConversations(cutoffTimestamp)
    }
} 