package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.TimePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditMedicationActivity : AppCompatActivity() {

    // Medication data class
    data class Medication(
        val medicineName: String = "",
        val dose: String = "",
        val time: String = ""
    )

    private lateinit var database: FirebaseDatabase
    private lateinit var medicationsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medication)

        database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        medicationsRef = database.getReference("medications").child(userId.toString())

        val editTextMedicineName = findViewById<EditText>(R.id.editTextMedicineName)
        val editTextDose = findViewById<EditText>(R.id.editTextDose)
        val editTextTime = findViewById<TimePicker>(R.id.editTextTime)
        val saveMedicationButton = findViewById<Button>(R.id.buttonSaveMedication)

        saveMedicationButton.setOnClickListener {
            val enteredMedicineName = editTextMedicineName.text.toString()
            val enteredDose = editTextDose.text.toString()
            val enteredHour = editTextTime.hour.toString()
            val enteredMinute = editTextTime.minute.toString()
            val enteredTime = "$enteredHour:$enteredMinute"

            val medicationId = medicationsRef.push().key
            val medication = Medication(enteredMedicineName, enteredDose, enteredTime)

            medicationId?.let {
                medicationsRef.child(it)
                    .setValue(medication)
                    .addOnSuccessListener {
                        val intent = Intent(this, ViewMedicationActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        // Error handling
                    }
            }
        }
    }
}
