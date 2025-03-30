package com.example.asistentedecocina.core.animation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ChefState {
    IDLE, LISTENING, SPEAKING, THINKING
}

enum class ChefEmotion {
    HAPPY, SAD, SURPRISED, CONFUSED, NEUTRAL
}

enum class ChefAction {
    COOKING, CHOPPING, MIXING, PRESENTING
}

data class AnimationState(
    val currentState: ChefState = ChefState.IDLE,
    val currentEmotion: ChefEmotion = ChefEmotion.NEUTRAL,
    val currentAction: ChefAction? = null,
    val isPlaying: Boolean = true,
    val speed: Float = 1f,
    val iterations: Int = -1
)

class AnimationManager {
    private val _animationState = MutableStateFlow(AnimationState())
    val animationState: StateFlow<AnimationState> = _animationState.asStateFlow()

    fun getAnimationPath(state: ChefState? = null, emotion: ChefEmotion? = null, action: ChefAction? = null): String {
        return when {
            action != null -> "animations/actions/chef_${action.name.lowercase()}.json"
            emotion != null -> "animations/emotions/chef_${emotion.name.lowercase()}.json"
            state != null -> "animations/states/chef_${state.name.lowercase()}.json"
            else -> "animations/states/chef_idle.json"
        }
    }

    fun setState(state: ChefState) {
        _animationState.value = _animationState.value.copy(
            currentState = state,
            currentAction = null
        )
    }

    fun setEmotion(emotion: ChefEmotion) {
        _animationState.value = _animationState.value.copy(
            currentEmotion = emotion,
            currentAction = null
        )
    }

    fun setAction(action: ChefAction) {
        _animationState.value = _animationState.value.copy(
            currentAction = action
        )
    }

    fun clearAction() {
        _animationState.value = _animationState.value.copy(
            currentAction = null
        )
    }

    fun setPlaybackSpeed(speed: Float) {
        _animationState.value = _animationState.value.copy(
            speed = speed.coerceIn(0.25f, 3f)
        )
    }

    fun togglePlayback() {
        _animationState.value = _animationState.value.copy(
            isPlaying = !_animationState.value.isPlaying
        )
    }

    fun setIterations(iterations: Int) {
        _animationState.value = _animationState.value.copy(
            iterations = iterations
        )
    }

    fun getCurrentAnimationPath(): String {
        return with(_animationState.value) {
            when {
                currentAction != null -> getAnimationPath(action = currentAction)
                currentEmotion != ChefEmotion.NEUTRAL -> getAnimationPath(emotion = currentEmotion)
                else -> getAnimationPath(state = currentState)
            }
        }
    }
} 