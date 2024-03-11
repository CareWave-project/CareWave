package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class View_Medication_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_medication)

        val textViewMedicineName = findViewById<TextView>(R.id.textViewMedicineName)
        val textViewDose = findViewById<TextView>(R.id.textViewDose)
        val textViewTime = findViewById<TextView>(R.id.textViewTime)

        // Retrieve medication information from Intent extras
        val medicineName = intent.getStringExtra("medicineName")
        val dose = intent.getStringExtra("dose")
        val time = intent.getStringExtra("time")

        // Set the values of TextViews with the retrieved information
        textViewMedicineName.text = medicineName
        textViewDose.text = dose
        textViewTime.text = time

    }
}