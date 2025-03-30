package com.example.asistentedecocina.core.constants

object AnimationConstants {
    // Estados básicos
    const val ANIMATION_IDLE = "animations/states/chef_idle.json"
    const val ANIMATION_LISTENING = "animations/states/chef_listening.json"
    const val ANIMATION_SPEAKING = "animations/states/chef_speaking.json"
    const val ANIMATION_THINKING = "animations/states/chef_thinking.json"
    
    // Emociones
    const val ANIMATION_HAPPY = "animations/emotions/chef_happy.json"
    const val ANIMATION_SAD = "animations/emotions/chef_sad.json"
    const val ANIMATION_SURPRISED = "animations/emotions/chef_surprised.json"
    const val ANIMATION_CONFUSED = "animations/emotions/chef_confused.json"
    const val ANIMATION_NEUTRAL = "animations/emotions/chef_neutral.json"
    
    // Acciones de cocina
    const val ANIMATION_COOKING = "animations/actions/chef_cooking.json"
    const val ANIMATION_CHOPPING = "animations/actions/chef_chopping.json"
    const val ANIMATION_MIXING = "animations/actions/chef_mixing.json"
    const val ANIMATION_PRESENTING = "animations/actions/chef_presenting.json"

    // Paleta de colores del chef
    object ChefColors {
        const val UNIFORM_PRIMARY = "#FFFFFF"     // Blanco para el uniforme principal
        const val UNIFORM_SECONDARY = "#000000"   // Negro para detalles del uniforme
        const val HAT = "#FFFFFF"                 // Blanco para el gorro de chef
        const val ACCENT = "#FF4081"             // Rosa para detalles y accesorios
    }
} 