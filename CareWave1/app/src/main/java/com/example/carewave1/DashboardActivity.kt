package com.example.carewave1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button.*
import com.example.carewave1.R
import com.example.carewave1.R.id.buttonHeartRate
import com.google.android.material.bottomnavigation.BottomNavigationView

//import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Dashboard >>> HeartRate Page
        val buttonHeartRate=findViewById<Button>(R.id.buttonHeartRate)
        buttonHeartRate.setOnClickListener {
            val intent = Intent(this, HeartRateActivity::class.java)
            startActivity(intent)
        }

        // Dashboard >>> SpO2 Page
        val buttonSpO2=findViewById<Button>(R.id.buttonSpO2)
        buttonSpO2.setOnClickListener {
            val intent = Intent(this, SpO2Activity::class.java)
            startActivity(intent)
        }
//        // Dashboard >>> Pressure Page
//        val buttonPressure=findViewById<Button>(R.id.buttonPressure)
//        buttonPressure.setOnClickListener {
//            val intent = Intent(this, PressureActivity::class.java)
//            startActivity(intent)
//        }

//        // Dashboard >>> Steps Page
//        val buttonSteps=findViewById<Button>(R.id.buttonSpO2)
//        buttonSteps.setOnClickListener {
//            val intent = Intent(this, StepsActivity::class.java)
//            startActivity(intent)
//        }

        // Dashboard >> Edit_MedicationPage
        val buttonMedicationReminder =findViewById<Button>(R.id.buttonMedicationReminder)
        buttonMedicationReminder.setOnClickListener {
            val intent = Intent(this, EditMedicationActivity::class.java)
            startActivity(intent)
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////// NavBar to other pages //////////////////////////////////////

// Dashboard(nav bar icons) >> Respective Page
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val homeMenuItemId = R.id.navigation_item2
        bottomNavigationView.setSelectedItemId(homeMenuItemId)


        bottomNavigationView.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.navigation_item1 -> {
                    // Navigate to View Medication 2 page
                    val intent = Intent(this, ViewMedicationActivity2::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_item2 -> {
                    // Navigate to Home page
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_item3 -> {
                    // Navigate to Profile page
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }

        }



    }
    override fun onResume() {
        super.onResume()

        // Get reference to your BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Get the ID of the "Home" menu item
        val homeMenuItemId = R.id.navigation_item2 // Replace with the ID of your "Home" menu item

        // Check if the "Home" item is not already selected
        if (bottomNavigationView.selectedItemId != homeMenuItemId) {
            // Set the "Home" menu item as the selected item
            bottomNavigationView.selectedItemId = homeMenuItemId
        }
    }

}
