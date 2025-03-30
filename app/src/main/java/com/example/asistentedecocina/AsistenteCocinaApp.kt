package com.example.asistentedecocina

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AsistenteCocinaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar la base de datos
        AppDatabase.getDatabase(this)
    }
} 