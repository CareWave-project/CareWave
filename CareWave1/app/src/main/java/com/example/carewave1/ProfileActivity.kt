package com.example.carewave1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val signOutButton = findViewById<Button>(R.id.signOutButton)
        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Redirect the user to the login screen or any other desired activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finish the current activity to prevent the user from coming back to it after sign out
        }
    }
}