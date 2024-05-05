package com.example.carewave1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var userAgeTextView: TextView
    private lateinit var userContactTextView: TextView
    private lateinit var editButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize the back button
        val backButton: ImageView = findViewById(R.id.icon_back_arrow)

        // Set OnClickListener to the back button
        backButton.setOnClickListener {
            // Navigate to the dashboard activity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent returning to it via back navigation
        }

        // Initialize views
        userNameTextView = findViewById(R.id.userNameTextView)
        userAgeTextView = findViewById(R.id.userAgeTextView)
        userContactTextView = findViewById(R.id.userContactTextView)
        editButton = findViewById(R.id.editButton)
        val signOutButton = findViewById<Button>(R.id.signOutButton)

        // Set click listener for sign out button
        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Redirect the user to the login screen or any other desired activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finish the current activity to prevent the user from coming back to it after sign out
        }

        // Retrieve the name of the logged-in user from Firebase and display it in TextView
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            // If the user is logged in, fetch the user data from Firebase Realtime Database
            val userId = currentUser.uid
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name = dataSnapshot.child("name").getValue(String::class.java)
                    name?.let {
                        // Display the user's name in the TextView
                        userNameTextView.text = "Username: $name"
                    }
                    val age = dataSnapshot.child("age").getValue(String::class.java)
                    age?.let {
                        // Display the user's age in the TextView
                        userAgeTextView.text = "Age: $age"
                    }
                    val contact = dataSnapshot.child("contact").getValue(String::class.java)
                    contact?.let {
                        // Display the user's contact in the TextView
                        userContactTextView.text = "Contact: $contact"
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    // For now, log the error
                    Log.e("ProfileActivity", "Database Error: ${databaseError.message}")
                }
            })
        }

        // Set click listener for edit button
        editButton.setOnClickListener {
            if (editButton.text == "Edit") {
                // Enable editing mode
                enableEditing()
                // Change button text to "Save"
                editButton.text = "Save"
            } else {
                // Save edited details to Firebase
                saveDetails()
                // Disable editing mode
                disableEditing()
                // Change button text to "Edit"
                editButton.text = "Edit"
            }
        }
    }

    private fun enableEditing() {
        // Enable editing of details (e.g., enable EditText fields)
    }

    private fun saveDetails() {
        // Save edited details to Firebase
    }

    private fun disableEditing() {
        // Disable editing of details (e.g., disable EditText fields)
    }
}
