package com.example.carewave1

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class MedicationReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        val medicineName = intent?.getStringExtra("medicineName") ?: ""
        val dose = intent?.getStringExtra("dose") ?: ""
        val time = intent?.getStringExtra("time") ?: ""

        showNotification(context, medicineName, dose, time)
    }


    private fun showNotification(context: Context, medicineName: String, dose: String, time: String) {
        val notificationMessage = "\nMedicine: $medicineName\nDose: $dose\nTime: $time"

        // Create a notification builder with high priority
        val builder = NotificationCompat.Builder(context, "medicationReminderChannelId")
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle("Medication Reminder")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set high priority
            .setCategory(NotificationCompat.CATEGORY_ALARM) // Set category to alarm
            .setAutoCancel(true) // Automatically dismiss the notification when clicked

        // Create an intent to launch the app when notification is clicked
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        // Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }


}
