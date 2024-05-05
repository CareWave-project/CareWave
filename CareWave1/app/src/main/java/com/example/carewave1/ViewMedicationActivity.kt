package com.example.carewave1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewMedicationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_medication)

        // Get the current user's ID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val backButton: ImageView = findViewById(R.id.icon_back_arrow)

// Set OnClickListener to the back arrow ImageView
        backButton.setOnClickListener {
            // Navigate back to the dashboard activity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            // Finish the current activity to navigate back to the previous page
            finish()
        }





        // If the user is not logged in, return
        if (userId == null) {
            // Handle the case where the user is not logged in
            return
        }

        // Get a reference to the Firebase Realtime Database for the user's medications
        val database = FirebaseDatabase.getInstance()
        val medicationsRef = database.getReference("medications/$userId")

        // Assuming you have TextViews to display medication information
        val textViewMedicineName = findViewById<TextView>(R.id.textViewMedicineName)
        val textViewDose = findViewById<TextView>(R.id.textViewDose)
        val textViewTime = findViewById<TextView>(R.id.textViewTime)

        // Read medication data from Firebase Realtime Database
        medicationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Loop through all medications for the current user
                for (medicationSnapshot in dataSnapshot.children) {
                    // Get medication data
                    val medication = medicationSnapshot.getValue(EditMedicationActivity.Medication::class.java)
                    // Display medication data in TextViews
                    textViewMedicineName.text = medication?.medicineName
                    textViewDose.text = medication?.dose
                    textViewTime.text = medication?.time
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}