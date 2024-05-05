package com.example.carewave1

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*

class SpO2Activity : AppCompatActivity() {

    private lateinit var alertManager: AlertManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp_o2)

        alertManager = AlertManager(this)

        // Reference to the database node where your SpO2 data is stored
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("sensor_data")

        // Add a value event listener to listen for data changes
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if data exists
                if (dataSnapshot.exists()) {
                    // Get the entire data snapshot as a HashMap
                    val dataMap = dataSnapshot.value as HashMap<*, *>

                    // Extract specific values from the HashMap
                    val spo2Value = dataMap["spo2"]?.toString()?.toInt() ?: 0
                    val timestamp = dataMap["timestamp"]?.toString()?.toLong() ?: 0L

                    // Update the SpO2 text view
                    val spo2TextView = findViewById<TextView>(R.id.spo2Text)
                    spo2TextView.text = "$spo2Value %"

                    // Determine SpO2 status (normal or abnormal)
                    val normalRange = 95..100
                    val statusText = if (spo2Value in normalRange) {
                        "Your SpO2 Level is Normal" // Normal case
                    } else {
                        "Abnormal SpO2 Level Detected" // Abnormal case
                        alertManager.triggerAlert()
                    }

                    // Update the SpO2 status text view
                    val spo2StatusTextView = findViewById<TextView>(R.id.spo2StatusText)
                    spo2StatusTextView.text = statusText.toString()

                    // You can also update the UI based on the status
                    // For example, change the text color of the status text view
                    if (spo2Value !in normalRange) {
                        spo2StatusTextView.setTextColor(Color.RED) // Or any warning color
                    } else {
                        spo2StatusTextView.setTextColor(Color.BLACK) // Or a normal color
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