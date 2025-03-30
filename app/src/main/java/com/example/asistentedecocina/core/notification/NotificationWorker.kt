package com.example.asistentedecocina.core.notification

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val notificationManager = NotificationManager(applicationContext)
        
        // Mostrar consejo del día
        val tipTitle = inputData.getString(KEY_TIP_TITLE) ?: "Consejo del día"
        val tipContent = inputData.getString(KEY_TIP_CONTENT) ?: "¡Aprende algo nuevo hoy!"
        notificationManager.showDailyTipNotification(tipTitle, tipContent)

        return Result.success()
    }

    companion object {
        private const val KEY_TIP_TITLE = "tip_title"
        private const val KEY_TIP_CONTENT = "tip_content"

        fun scheduleDailyTip(context: Context, tipTitle: String, tipContent: String) {
            val inputData = workDataOf(
                KEY_TIP_TITLE to tipTitle,
                KEY_TIP_CONTENT to tipContent
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val dailyTipRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                1, TimeUnit.DAYS
            )
                .setInputData(inputData)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "daily_tip",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    dailyTipRequest
                )
        }

        fun scheduleRecipeReminder(
            context: Context,
            recipeName: String,
            daysSinceLastCooked: Int
        ) {
            val inputData = workDataOf(
                "recipe_name" to recipeName,
                "days_since_last_cooked" to daysSinceLastCooked
            )

            val reminderRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(inputData)
                .setInitialDelay(1, TimeUnit.DAYS)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    "recipe_reminder_$recipeName",
                    ExistingWorkPolicy.REPLACE,
                    reminderRequest
                )
        }
    }
} 