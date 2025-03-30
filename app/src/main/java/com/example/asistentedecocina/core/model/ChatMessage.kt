package com.example.asistentedecocina.core.model

import java.util.*

enum class MessageType {
    USER,
    ASSISTANT
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val type: MessageType,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
) 