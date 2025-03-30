package com.example.asistentedecocina.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.asistentedecocina.core.constants.AnimationConstants
import com.example.asistentedecocina.core.model.AvatarState
import com.example.asistentedecocina.core.model.CookingAction
import com.example.asistentedecocina.presentation.viewmodel.AvatarViewModel

@Composable
fun Avatar(
    viewModel: AvatarViewModel,
    modifier: Modifier = Modifier
) {
    val avatarState by viewModel.avatarState.collectAsState()
    
    Box(modifier = modifier.size(300.dp)) {
        when (avatarState) {
            is AvatarState.Idle -> IdleAnimation()
            is AvatarState.Listening -> ListeningAnimation()
            is AvatarState.Speaking -> SpeakingAnimation()
            is AvatarState.Thinking -> ThinkingAnimation()
            is AvatarState.Expressing -> {
                val emotion = (avatarState as AvatarState.Expressing).emotion
                EmotionAnimation(emotion)
            }
            is AvatarState.Cooking -> {
                val action = (avatarState as AvatarState.Cooking).action
                CookingAnimation(action)
            }
        }
    }
}

@Composable
private fun IdleAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(AnimationConstants.ANIMATION_IDLE)
    )
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE
    )
}

@Composable
private fun ListeningAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(AnimationConstants.ANIMATION_LISTENING)
    )
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE
    )
}

@Composable
private fun SpeakingAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(AnimationConstants.ANIMATION_SPEAKING)
    )
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE
    )
}

@Composable
private fun ThinkingAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(AnimationConstants.ANIMATION_THINKING)
    )
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE
    )
}

@Composable
private fun EmotionAnimation(emotion: com.example.asistentedecocina.core.model.AvatarEmotion) {
    val animationFile = when (emotion) {
        com.example.asistentedecocina.core.model.AvatarEmotion.HAPPY -> AnimationConstants.ANIMATION_HAPPY
        com.example.asistentedecocina.core.model.AvatarEmotion.SAD -> AnimationConstants.ANIMATION_SAD
        com.example.asistentedecocina.core.model.AvatarEmotion.SURPRISED -> AnimationConstants.ANIMATION_SURPRISED
        com.example.asistentedecocina.core.model.AvatarEmotion.CONFUSED -> AnimationConstants.ANIMATION_CONFUSED
        com.example.asistentedecocina.core.model.AvatarEmotion.NEUTRAL -> AnimationConstants.ANIMATION_NEUTRAL
    }
    
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE
    )
}

@Composable
private fun CookingAnimation(action: CookingAction) {
    val animationFile = when (action) {
        CookingAction.COOKING -> AnimationConstants.ANIMATION_COOKING
        CookingAction.CHOPPING -> AnimationConstants.ANIMATION_CHOPPING
        CookingAction.MIXING -> AnimationConstants.ANIMATION_MIXING
        CookingAction.PRESENTING -> AnimationConstants.ANIMATION_PRESENTING
    }
    
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    LottieAnimation(
        composition = composition,
        iterations = Int.MAX_VALUE
    )
} 