package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar

class HeartRateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)

        val backButton: ImageView = findViewById(R.id.icon_back_arrow)

        // Set OnClickListener to the back arrow ImageView
        backButton.setOnClickListener {
            // Perform the action to navigate back to the previous page
            onBackPressed()
        }

    }
}