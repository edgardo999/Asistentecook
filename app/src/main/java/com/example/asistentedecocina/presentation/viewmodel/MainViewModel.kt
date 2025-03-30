package com.example.asistentedecocina.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentedecocina.core.model.ChatMessage
import com.example.asistentedecocina.core.speech.SpeechRecognitionManager
import com.example.asistentedecocina.core.speech.SpeechState
import com.example.asistentedecocina.data.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val speechRecognitionManager: SpeechRecognitionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Observar el estado de conectividad
            conversationRepository.isOnline.collect { isOnline ->
                _uiState.update { it.copy(isOnline = isOnline) }
                if (isOnline) {
                    syncPendingConversations()
                }
            }
        }

        viewModelScope.launch {
            // Observar el estado del reconocimiento de voz
            speechRecognitionManager.state.collect { speechState ->
                _uiState.update { it.copy(speechState = speechState) }
                
                // Procesar resultados exitosos automáticamente
                if (speechState is SpeechState.Success) {
                    processVoiceInput(speechState.text)
                }
            }
        }

        // Inicializar el reconocimiento de voz
        speechRecognitionManager.initialize()
    }

    fun startVoiceRecognition(continuous: Boolean = false) {
        speechRecognitionManager.startListening(continuous = continuous)
        _uiState.update { it.copy(isListening = true) }
    }

    fun stopVoiceRecognition() {
        speechRecognitionManager.stopListening()
        _uiState.update { it.copy(isListening = false) }
    }

    fun processVoiceInput(text: String) {
        viewModelScope.launch {
            val currentMessages = _uiState.value.currentConversation.toMutableList()
            
            // Agregar mensaje del usuario
            val userMessage = ChatMessage(
                id = System.currentTimeMillis(),
                content = text,
                type = ChatMessage.MessageType.USER
            )
            currentMessages.add(userMessage)
            
            // Procesar el comando o generar respuesta
            val response = processCommand(text) ?: "Entendí: $text"
            
            // Agregar respuesta del asistente
            val assistantMessage = ChatMessage(
                id = System.currentTimeMillis() + 1,
                content = response,
                type = ChatMessage.MessageType.ASSISTANT
            )
            currentMessages.add(assistantMessage)

            // Actualizar estado
            _uiState.update { it.copy(currentConversation = currentMessages) }

            // Guardar conversación
            conversationRepository.saveConversation(currentMessages)
        }
    }

    private fun processCommand(text: String): String? {
        // Comandos básicos
        return when {
            text.contains("ayuda", ignoreCase = true) -> 
                "Puedo ayudarte con recetas, técnicas de cocina y consejos culinarios. ¿Qué te gustaría saber?"
            
            text.contains("receta", ignoreCase = true) -> 
                "¿Qué tipo de receta te gustaría preparar?"
            
            text.contains("ingredientes", ignoreCase = true) -> 
                "¿De qué receta quieres conocer los ingredientes?"
            
            text.contains("paso siguiente", ignoreCase = true) -> 
                "Avanzando al siguiente paso de la receta..."
            
            text.contains("repetir", ignoreCase = true) -> 
                "Repitiendo el paso actual..."
            
            else -> null
        }
    }

    private suspend fun syncPendingConversations() {
        conversationRepository.syncPendingConversations()
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognitionManager.release()
    }
}

data class MainUiState(
    val isOnline: Boolean = true,
    val isListening: Boolean = false,
    val currentConversation: List<ChatMessage> = emptyList(),
    val speechState: SpeechState = SpeechState.Idle,
    val isLoading: Boolean = false,
    val error: String? = null
) 