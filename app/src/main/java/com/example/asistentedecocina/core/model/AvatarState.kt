package com.example.asistentedecocina.core.model

sealed class AvatarState {
    object Idle : AvatarState()
    object Listening : AvatarState()
    object Speaking : AvatarState()
    object Thinking : AvatarState()
    data class Expressing(val emotion: AvatarEmotion) : AvatarState()
    data class Cooking(val action: CookingAction) : AvatarState()
}

enum class AvatarEmotion {
    HAPPY,
    SAD,
    SURPRISED,
    CONFUSED,
    NEUTRAL
}

enum class CookingAction {
    COOKING,
    CHOPPING,
    MIXING,
    PRESENTING
} 