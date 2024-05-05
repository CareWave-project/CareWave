package com.example.carewave1

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*

import androidx.appcompat.app.ActionBar

class HeartRateActivity : AppCompatActivity() {

    private lateinit var alertManager: AlertManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)

        alertManager = AlertManager(this)

// Reference to the database node where your heart rate data is stored
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("sensor_data")

// Add a value event listener to listen for data changes
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if data exists
                if (dataSnapshot.exists()) {
                    // Get the entire data snapshot as a HashMap
                    val dataMap = dataSnapshot.value as HashMap<*, *>

                    // Extract specific values from the HashMap
                    val beatAvg = dataMap["heartRate"]?.toString()?.toInt() ?: 0
                    val beatsPerMinute = dataMap["beatsPerMinute"]?.toString()?.toInt() ?: 0
                    val timestamp = dataMap["timestamp"]?.toString()?.toLong() ?: 0L

                    if (dataSnapshot.exists()) {
                        // Get the heart rate value
                        val heartRate = dataMap["heartRate"]?.toString()?.toInt() ?: 0

                        // Update the heart rate text view
                        val heartRateTextView = findViewById<TextView>(R.id.heartRateText)
                        heartRateTextView.text = heartRate.toString() + " BPM"

                        // Determine heart rate status (normal or abnormal)
                        val normalRange = 60..100
                        val statusText = if (heartRate in normalRange) {
                            "Your Heart Rate is Normal" // Normal case
                        } else {
                            "Abnormal Heart Rate Detected" // Abnormal case
                            alertManager.triggerAlert()
                        }

                        // Update the heart rate status text view
                        val heartRateStatusTextView = findViewById<TextView>(R.id.heartRateStatusText)
                        heartRateStatusTextView.text = statusText.toString()

                        // You can also update the UI based on the status
                        // For example, change the text color of the status text view
                        if (heartRate !in normalRange) {
                            heartRateStatusTextView.setTextColor(Color.RED) // Or any warning color
                        } else {
                            heartRateStatusTextView.setTextColor(Color.BLACK) // Or a normal color
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors in case data retrieval fails
               // Log.w("Firebase", "Error retrieving data: $databaseError")
            }
        }

// Attach the value event listener to the database reference
        databaseReference.addValueEventListener(valueEventListener)


        // Initialize the back button
        val backButton: ImageView = findViewById(R.id.icon_back_arrow)

        // Set OnClickListener to the back button
        backButton.setOnClickListener {
            // Navigate to the dashboard activity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent returning to it via back navigation
        }

    }
}