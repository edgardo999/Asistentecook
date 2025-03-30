package com.example.asistentedecocina.core.notification

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notification_preferences")

class NotificationPreferencesRepository(private val context: Context) {
    private object PreferencesKeys {
        val DAILY_TIPS_ENABLED = booleanPreferencesKey("daily_tips_enabled")
        val RECIPE_REMINDERS_ENABLED = booleanPreferencesKey("recipe_reminders_enabled")
        val TIMER_NOTIFICATIONS_ENABLED = booleanPreferencesKey("timer_notifications_enabled")
        val NOTIFICATION_TIME = stringPreferencesKey("notification_time")
        val LAST_TIP_DATE = longPreferencesKey("last_tip_date")
    }

    val dailyTipsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DAILY_TIPS_ENABLED] ?: true
        }

    val recipeRemindersEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.RECIPE_REMINDERS_ENABLED] ?: true
        }

    val timerNotificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.TIMER_NOTIFICATIONS_ENABLED] ?: true
        }

    val notificationTime: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_TIME] ?: "09:00"
        }

    val lastTipDate: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LAST_TIP_DATE] ?: 0L
        }

    suspend fun setDailyTipsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_TIPS_ENABLED] = enabled
        }
    }

    suspend fun setRecipeRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.RECIPE_REMINDERS_ENABLED] = enabled
        }
    }

    suspend fun setTimerNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setNotificationTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_TIME] = time
        }
    }

    suspend fun updateLastTipDate() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_TIP_DATE] = System.currentTimeMillis()
        }
    }
} 