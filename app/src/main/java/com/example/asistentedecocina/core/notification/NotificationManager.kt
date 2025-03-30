package com.example.asistentedecocina.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.asistentedecocina.R
import com.example.asistentedecocina.presentation.MainActivity

class NotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_TIMER,
                    "Temporizadores",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificaciones para temporizadores de recetas"
                    enableVibration(true)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_TIPS,
                    "Consejos Culinarios",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Consejos y trucos diarios"
                    enableVibration(false)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_REMINDERS,
                    "Recordatorios",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Recordatorios de recetas favoritas"
                    enableVibration(false)
                    enableLights(true)
                }
            )

            channels.forEach { channel ->
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun showTimerNotification(
        recipeName: String,
        stepNumber: Int,
        timeRemaining: String
    ) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_TIMER)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle("Temporizador: $recipeName")
            .setContentText("Paso $stepNumber - Tiempo restante: $timeRemaining")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    fun showDailyTipNotification(tipTitle: String, tipContent: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("screen", "tips")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_TIPS)
            .setSmallIcon(R.drawable.ic_lightbulb)
            .setContentTitle("Consejo del día: $tipTitle")
            .setContentText(tipContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    fun showRecipeReminderNotification(
        recipeName: String,
        daysSinceLastCooked: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("screen", "recipes")
            putExtra("recipe", recipeName)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(R.drawable.ic_restaurant)
            .setContentTitle("¡Hace tiempo que no cocinas $recipeName!")
            .setContentText("Han pasado $daysSinceLastCooked días desde la última vez")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val CHANNEL_TIMER = "timer_channel"
        const val CHANNEL_TIPS = "tips_channel"
        const val CHANNEL_REMINDERS = "reminders_channel"
    }
} 