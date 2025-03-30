package com.example.asistentedecocina.data.local.converter

import androidx.room.TypeConverter
import com.example.asistentedecocina.core.model.ChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatMessageConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): List<ChatMessage> {
        val listType = object : TypeToken<List<ChatMessage>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(messages: List<ChatMessage>): String {
        return gson.toJson(messages)
    }
} 