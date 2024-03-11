package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.TimePicker

class Edit_Medication_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medication)


                // Assuming you have EditText fields for entering medication name, dose, and time
                val editTextMedicineName = findViewById<EditText>(R.id.editTextMedicineName)
                val editTextDose = findViewById<EditText>(R.id.editTextDose)
                val editTextTime = findViewById<TimePicker>(R.id.editTextTime)

                // Assuming you have a button with ID "saveButton" in your layout
                val saveButton = findViewById<Button>(R.id.buttonSaveMedication)

                saveButton.setOnClickListener {
                    // Retrieve entered medication information
                    val enteredMedicineName = editTextMedicineName.text.toString()
                    val enteredDose = editTextDose.text.toString()
                    val enteredHour = editTextTime.hour.toString() // Get selected hour from TimePicker
                    val enteredMinute = editTextTime.minute.toString() // Get selected minute from TimePicker
                    val enteredTime = "$enteredHour:$enteredMinute" // Combine hour and minute


                    // Create an Intent to navigate to ViewMedicationActivity
                    val intent = Intent(this, View_Medication_Activity::class.java).apply {
                        putExtra("medicineName", enteredMedicineName)
                        putExtra("dose", enteredDose)
                        putExtra("time", enteredTime)
                    }

                    // Start ViewMedicationActivity
                    startActivity(intent)
                }



    }
}