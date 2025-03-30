package com.example.asistentedecocina.data.local.dao

import androidx.room.*
import com.example.asistentedecocina.data.local.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY timestamp DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: Long): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity): Long

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Delete
    suspend fun deleteConversation(conversation: ConversationEntity)

    @Query("DELETE FROM conversations WHERE timestamp < :timestamp")
    suspend fun deleteOldConversations(timestamp: Long)

    @Query("SELECT * FROM conversations WHERE isSynced = 0")
    suspend fun getUnsyncedConversations(): List<ConversationEntity>
} 