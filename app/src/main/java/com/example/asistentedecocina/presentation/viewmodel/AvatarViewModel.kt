package com.example.asistentedecocina.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentedecocina.core.model.AvatarState
import com.example.asistentedecocina.core.model.AvatarEmotion
import com.example.asistentedecocina.core.model.CookingAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor() : ViewModel() {
    
    private val _avatarState = MutableStateFlow<AvatarState>(AvatarState.Idle)
    val avatarState: StateFlow<AvatarState> = _avatarState.asStateFlow()

    fun updateState(newState: AvatarState) {
        viewModelScope.launch {
            _avatarState.emit(newState)
        }
    }

    fun setEmotion(emotion: AvatarEmotion) {
        updateState(AvatarState.Expressing(emotion))
    }

    fun setCookingAction(action: CookingAction) {
        updateState(AvatarState.Cooking(action))
    }

    fun startListening() {
        updateState(AvatarState.Listening)
    }

    fun startSpeaking() {
        updateState(AvatarState.Speaking)
    }

    fun startThinking() {
        updateState(AvatarState.Thinking)
    }

    fun setIdle() {
        updateState(AvatarState.Idle)
    }

    fun startCooking() {
        setCookingAction(CookingAction.COOKING)
    }

    fun startChopping() {
        setCookingAction(CookingAction.CHOPPING)
    }

    fun startMixing() {
        setCookingAction(CookingAction.MIXING)
    }

    fun presentDish() {
        setCookingAction(CookingAction.PRESENTING)
    }
} 