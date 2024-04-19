package com.example.carewave1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Dummy function for sending data to the database (You can remove this if not needed)
        fun sendData(view: View) {
            // Write a message to the database
            // This function is not related to Firebase Authentication, you may remove it if not needed.
        }
    }
}
