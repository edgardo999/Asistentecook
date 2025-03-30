package com.example.asistentedecocina.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.asistentedecocina.data.local.converter.ChatMessageConverter
import com.example.asistentedecocina.core.model.ChatMessage

@Entity(tableName = "conversations")
@TypeConverters(ChatMessageConverter::class)
data class ConversationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val messages: List<ChatMessage>,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val summary: String? = null
) 