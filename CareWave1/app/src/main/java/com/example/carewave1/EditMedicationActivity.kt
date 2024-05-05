package com.example.carewave1

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EditMedicationActivity : AppCompatActivity() {

    // Medication data class
    data class Medication(
        val medicineName: String = "",
        val dose: String = "",
        val time: String = ""
    )

    private lateinit var database: FirebaseDatabase
    private lateinit var medicationsRef: DatabaseReference

    private val medications = mutableListOf<Medication>() // List to store medications

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medication)

        database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        medicationsRef = database.getReference("medications").child(userId.toString())

        val editTextMedicineName = findViewById<EditText>(R.id.editTextMedicineName)
        val editTextDose = findViewById<EditText>(R.id.editTextDose)
        val editTextTime = findViewById<TimePicker>(R.id.editTextTime)
        val saveMedicationButton = findViewById<Button>(R.id.buttonSaveMedication)


        // Create a Notification Channel
        createNotificationChannel()

        saveMedicationButton.setOnClickListener {
            val enteredMedicineName = editTextMedicineName.text.toString()
            val enteredDose = editTextDose.text.toString()
            val enteredHour = editTextTime.hour // Get the hour from TimePicker
            val enteredMinute = editTextTime.minute // Get the minute from TimePicker
            val enteredTime =
                String.format("%02d:%02d", enteredHour, enteredMinute) // Format time as "HH:MM"

            val medicationId = medicationsRef.push().key
            val medication = Medication(enteredMedicineName, enteredDose, enteredTime)
            medications.add(medication) // Add to medication list

            medicationId?.let {
                medicationsRef.child(it)
                    .setValue(medication)
                    .addOnSuccessListener {

                        // Navigate to ViewMedicationActivity
                        val intent = Intent(this, ViewMedicationActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        // Error handling
                    }
            }
            // Update UI to display medications (optional)

            // Save medications to Firebase (optional, if needed)
            // ... existing code to save medications to Firebase ...

            // Schedule reminders for all medications in the list
            scheduleAllReminders()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medication Reminder"
            val descriptionText = "Reminders for taking medication"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("medicationReminderChannelId", name, importance).apply {
                    description = descriptionText
                }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission", "ScheduleExactAlarm")
    private fun scheduleAllReminders() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        for (medication in medications) {
            val parts = medication.time.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()

            if (currentHour < hour || (currentHour == hour && currentMinute < minute)) {
                // Schedule alarm for today
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
            } else {
                // Schedule alarm for tomorrow
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to the next day
            }

            // Create intent with medication details as extras
            val intent = Intent(this, MedicationReminderReceiver::class.java).apply {
                putExtra("medicineName", medication.medicineName)
                putExtra("dose", medication.dose)
                putExtra("time", medication.time)
            }

            // Create a unique PendingIntent for each medication
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                UUID.randomUUID().hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Schedule alarm
            alarmManager.setExact(RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

        // Clear the medication list after scheduling reminders
        medications.clear()
    }
}

