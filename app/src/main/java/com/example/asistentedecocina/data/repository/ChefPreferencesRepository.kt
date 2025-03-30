package com.example.asistentedecocina.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.asistentedecocina.core.model.ChefPreferences
import com.example.asistentedecocina.core.model.ChefStyle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "chef_preferences")

@Singleton
class ChefPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val STYLE = stringPreferencesKey("chef_style")
        val ANIMATION_SPEED = floatPreferencesKey("animation_speed")
        val PRIMARY_COLOR = longPreferencesKey("primary_color")
        val SECONDARY_COLOR = longPreferencesKey("secondary_color")
        val HAT_COLOR = longPreferencesKey("hat_color")
        val APRON_COLOR = longPreferencesKey("apron_color")
        val EXPRESSIVENESS = floatPreferencesKey("expressiveness")
        val SIZE = floatPreferencesKey("size")
    }

    val preferences: Flow<ChefPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            ChefPreferences(
                style = ChefStyle.valueOf(
                    preferences[PreferencesKeys.STYLE] ?: ChefStyle.CLASSIC.name
                ),
                animationSpeed = preferences[PreferencesKeys.ANIMATION_SPEED] ?: 1.0f,
                primaryColor = androidx.compose.ui.graphics.Color(
                    preferences[PreferencesKeys.PRIMARY_COLOR] ?: 0xFF6200EE
                ),
                secondaryColor = androidx.compose.ui.graphics.Color(
                    preferences[PreferencesKeys.SECONDARY_COLOR] ?: 0xFF03DAC5
                ),
                hatColor = androidx.compose.ui.graphics.Color(
                    preferences[PreferencesKeys.HAT_COLOR] ?: 0xFFFFFFFF
                ),
                apronColor = androidx.compose.ui.graphics.Color(
                    preferences[PreferencesKeys.APRON_COLOR] ?: 0xFFFFFFFF
                ),
                expressiveness = preferences[PreferencesKeys.EXPRESSIVENESS] ?: 1.0f,
                size = preferences[PreferencesKeys.SIZE] ?: 1.0f
            )
        }

    suspend fun updateStyle(style: ChefStyle) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STYLE] = style.name
        }
    }

    suspend fun updateAnimationSpeed(speed: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANIMATION_SPEED] = speed.coerceIn(0.5f, 2.0f)
        }
    }

    suspend fun updateColors(
        primaryColor: androidx.compose.ui.graphics.Color? = null,
        secondaryColor: androidx.compose.ui.graphics.Color? = null,
        hatColor: androidx.compose.ui.graphics.Color? = null,
        apronColor: androidx.compose.ui.graphics.Color? = null
    ) {
        context.dataStore.edit { preferences ->
            primaryColor?.let { preferences[PreferencesKeys.PRIMARY_COLOR] = it.value.toLong() }
            secondaryColor?.let { preferences[PreferencesKeys.SECONDARY_COLOR] = it.value.toLong() }
            hatColor?.let { preferences[PreferencesKeys.HAT_COLOR] = it.value.toLong() }
            apronColor?.let { preferences[PreferencesKeys.APRON_COLOR] = it.value.toLong() }
        }
    }

    suspend fun updateExpressiveness(expressiveness: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.EXPRESSIVENESS] = expressiveness.coerceIn(0.5f, 2.0f)
        }
    }

    suspend fun updateSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SIZE] = size.coerceIn(0.5f, 2.0f)
        }
    }

    suspend fun resetToDefaults() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun applyPalette(preferences: ChefPreferences) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.PRIMARY_COLOR] = preferences.primaryColor.value.toLong()
            prefs[PreferencesKeys.SECONDARY_COLOR] = preferences.secondaryColor.value.toLong()
            prefs[PreferencesKeys.HAT_COLOR] = preferences.hatColor.value.toLong()
            prefs[PreferencesKeys.APRON_COLOR] = preferences.apronColor.value.toLong()
        }
    }
} 