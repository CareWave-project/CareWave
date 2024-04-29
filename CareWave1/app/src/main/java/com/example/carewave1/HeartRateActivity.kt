package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HeartRateActivity : AppCompatActivity() {

    private lateinit var mlModelExecutor: MLModelExecutor
    private lateinit var alertManager: AlertManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)

        // Initialize MLModelExecutor and AlertManager
        mlModelExecutor = MLModelExecutor(this)
        alertManager = AlertManager(this)

        val backButton: ImageView = findViewById(R.id.icon_back_arrow)

        // Set OnClickListener to the back arrow ImageView
        backButton.setOnClickListener {
            // Perform the action to navigate back to the previous page
            finish()
        }
        // Example: Manually input heart rate data
        val heartRateData = 70.0.toFloat() // Replace with your actual heart rate data

        // Perform prediction using the manually input data
        processHeartRateData(heartRateData)

        // Fetch data from the database in real-time
        //fetchDataFromDatabase()
    }

   /* private fun fetchDataFromDatabase() {
        // Example code to fetch data from the database
        GlobalScope.launch(Dispatchers.IO) {
            // Fetch heart rate data from the database
            val heartRateData = // Your database query to fetch heart rate data

        // Process the fetched data
            processHeartRateData(heartRateData)

        }
    }*/

    private fun processHeartRateData(heartRateData: Float) {
        // Perform prediction
        val predictionResult = mlModelExecutor.executeModel(floatArrayOf(heartRateData))

        // Check if prediction result indicates an emergency
        /*if (predictionResult == 1) {
            // Trigger alert mechanism
            alertManager.triggerAlert()
        }*/
    }
}



