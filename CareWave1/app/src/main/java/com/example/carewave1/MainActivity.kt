package com.example.carewave1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen) // Use splash screen layout

        // Display splash screen for 3 seconds
        Handler().postDelayed({
            // Start the LoginActivity after 3 seconds
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            //finish() // Finish the MainActivity to prevent returning to it via back navigation
        }, 3000) // Delay for 3 seconds (3000 milliseconds)
    }
}