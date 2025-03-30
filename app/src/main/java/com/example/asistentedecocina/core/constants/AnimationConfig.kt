package com.example.asistentedecocina.core.constants

object AnimationConfig {
    // Configuración general
    const val DEFAULT_SIZE = 300
    const val DEFAULT_SPEED = 1.0f
    
    // Configuración del personaje
    object ChefAppearance {
        // Dimensiones del personaje
        const val CHARACTER_WIDTH = 200
        const val CHARACTER_HEIGHT = 250
        
        // Colores principales
        const val UNIFORM_PRIMARY = "#FFFFFF"     // Blanco uniforme
        const val UNIFORM_SECONDARY = "#2C3E50"   // Azul oscuro para detalles
        const val HAT_COLOR = "#FFFFFF"          // Blanco para el gorro
        const val ACCENT_COLOR = "#E74C3C"       // Rojo para detalles
        const val SKIN_TONE = "#FDB98F"          // Tono de piel medio
        
        // Características faciales
        const val HAS_MUSTACHE = true
        const val HAS_BEARD = false
        const val HAS_GLASSES = false
        
        // Accesorios
        const val HAS_CHEF_HAT = true
        const val HAS_NECKERCHIEF = true
        const val HAS_APRON = true
    }
    
    // Configuración de estados
    object StateAnimations {
        // Idle
        const val IDLE_DURATION = 2000L      // 2 segundos por ciclo
        const val IDLE_LOOP = true
        
        // Listening
        const val LISTENING_DURATION = 1500L  // 1.5 segundos por ciclo
        const val LISTENING_LOOP = true
        
        // Speaking
        const val SPEAKING_DURATION = 1000L   // 1 segundo por ciclo
        const val SPEAKING_LOOP = true
        
        // Thinking
        const val THINKING_DURATION = 2500L   // 2.5 segundos por ciclo
        const val THINKING_LOOP = true
    }
    
    // Configuración de efectos
    object Effects {
        const val USE_SHADOW = true
        const val SHADOW_OPACITY = 0.3f
        const val USE_GRADIENT = true
        const val USE_BLUR = false
    }
} 