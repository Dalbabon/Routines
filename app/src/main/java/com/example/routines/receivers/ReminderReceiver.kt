package com.example.routines.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.routines.MainActivity

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title") ?: "Напоминание"
        val message = intent.getStringExtra("message") ?: ""

        // Создаем Intent для открытия приложения
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("reminder_id", notificationId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Проверяем разрешение на отправку уведомлений
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            val notification = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            try {
                NotificationManagerCompat.from(context).notify(notificationId, notification)
            } catch (e: SecurityException) {
                Log.e("ReminderReceiver", "Notification permission denied", e)
            }
        } else {
            Log.w("ReminderReceiver", "Notifications are disabled for this app")
        }
    }
}