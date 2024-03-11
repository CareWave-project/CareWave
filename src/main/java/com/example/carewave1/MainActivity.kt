package com.example.carewave1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.example.carewave1.R.id.buttonPressure
import com.example.carewave1.R.id.buttonSaveMedication
import com.example.carewave1.R.id.buttonSpO2
import com.example.carewave1.R.id.buttonSteps
import com.example.carewave1.R.layout.activity_dashboard

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_dashboard)
// Dashboard >>> HeartRate Page
        val buttonHeartRate=findViewById<Button>(R.id.buttonHeartRate)
        buttonHeartRate.setOnClickListener {
            val intent = Intent(this, HeartRateActivity::class.java)
            startActivity(intent)
        }

        // Dashboard >>> SpO2 Page
        val buttonSpO2=findViewById<Button>(buttonSpO2)
        buttonSpO2.setOnClickListener {
            val intent = Intent(this, SpO2Activity::class.java)
            startActivity(intent)
        }



        // Dashboard >>> Pressure Page
        val buttonPressure=findViewById<Button>(buttonPressure)
        buttonPressure.setOnClickListener {
            val intent = Intent(this, PressureActivity::class.java)
            startActivity(intent)
        }

        // Dashboard >>> Steps Page
        val buttonSteps=findViewById<Button>(buttonSteps)
        buttonSteps.setOnClickListener {
            val intent = Intent(this, StepsActivity::class.java)
            startActivity(intent)
        }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Dashboard >> Edit_MedicationPage
        val buttonMedicationReminder =findViewById<Button>(R.id.buttonMedicationReminder)
        buttonMedicationReminder.setOnClickListener {
            val intent = Intent(this, Edit_Medication_Activity::class.java)
            startActivity(intent)
        }




    }
}