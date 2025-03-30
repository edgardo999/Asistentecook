package com.example.asistentedecocina.core.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeechRecognitionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var isContinuousListening = false

    private val _state = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val state: StateFlow<SpeechState> = _state.asStateFlow()

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _state.value = SpeechState.Listening
        }

        override fun onBeginningOfSpeech() {
            _state.value = SpeechState.Listening
        }

        override fun onRmsChanged(rmsdB: Float) {
            // Actualizar nivel de volumen si se necesita
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // No necesario para reconocimiento básico
        }

        override fun onEndOfSpeech() {
            if (!isContinuousListening) {
                _state.value = SpeechState.ProcessingInput
            }
        }

        override fun onError(error: Int) {
            val errorMessage = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permisos insuficientes"
                SpeechRecognizer.ERROR_NETWORK -> "Error de red"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera de red agotado"
                SpeechRecognizer.ERROR_NO_MATCH -> "No se encontró coincidencia"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Tiempo de espera de voz agotado"
                else -> "Error desconocido"
            }
            _state.value = SpeechState.Error(errorMessage)
            
            if (isContinuousListening && error != SpeechRecognizer.ERROR_NO_MATCH) {
                restartListening()
            }
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val confidence = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
            
            if (!matches.isNullOrEmpty()) {
                _state.value = SpeechState.Success(
                    text = matches[0],
                    confidence = confidence?.get(0) ?: 0f,
                    alternatives = matches.drop(1)
                )
            } else {
                _state.value = SpeechState.Error("No se detectó ningún texto")
            }

            if (isContinuousListening) {
                restartListening()
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                _state.value = SpeechState.PartialResult(matches[0])
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // No necesario para reconocimiento básico
        }
    }

    fun initialize() {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(recognitionListener)
        }
    }

    fun startListening(continuous: Boolean = false, language: String = "es-ES") {
        isContinuousListening = continuous
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        }
        
        speechRecognizer?.startListening(intent)
        isListening = true
        _state.value = SpeechState.Starting
    }

    fun stopListening() {
        isContinuousListening = false
        speechRecognizer?.stopListening()
        isListening = false
        _state.value = SpeechState.Idle
    }

    private fun restartListening() {
        if (isContinuousListening && isListening) {
            speechRecognizer?.cancel()
            startListening(true)
        }
    }

    fun release() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        isListening = false
        isContinuousListening = false
        _state.value = SpeechState.Idle
    }
}

sealed class SpeechState {
    object Idle : SpeechState()
    object Starting : SpeechState()
    object Listening : SpeechState()
    object ProcessingInput : SpeechState()
    data class PartialResult(val text: String) : SpeechState()
    data class Success(
        val text: String,
        val confidence: Float,
        val alternatives: List<String>
    ) : SpeechState()
    data class Error(val message: String) : SpeechState()
} 