package com.example.carewave1

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.gsm.SmsManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import android.os.Looper


class AlertManager(private val context: Context) {

    private var isAlertShowing = false
    private lateinit var windowManager: WindowManager
    private lateinit var alert_dialog: AlertDialog
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        // Initialize WindowManager
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    fun triggerAlert() {
        if (!isAlertShowing) {
            isAlertShowing = true

            // Inflate the layout for the alert dialog
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.alert_dialog, null)

            // Find views in the inflated layout
            val btnFalseAlarm = dialogView.findViewById<Button>(R.id.btnFalseAlarm)
            val btnSOS = dialogView.findViewById<Button>(R.id.btnSOS)

            // Create AlertDialog with inflated layout
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setView(dialogView)
            alert_dialog = alertDialogBuilder.create()

            // Set click listeners for buttons
            btnFalseAlarm.setOnClickListener {
                dismissAlert()
            }

            btnSOS.setOnClickListener {
                sendEmergencyAlert()
                dismissAlert()
            }

            // Show the AlertDialog
            alert_dialog.show()
        }
    }

    fun sendEmergencyAlert() {

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        // Get last known location
        fusedLocationClient.lastLocation
            .addOnSuccessListener(context as Activity) { location ->
                // Check if location is available
                if (location != null) {
                    // Construct the Google Maps URL with latitude and longitude
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val mapsUrl =
                        "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

                    // Send SMS with Google Maps URL
                    val message = "Emergency SOS! User's current location: $mapsUrl"
                    val phoneNumber =
                        "+91 8129076731" // Specify the phone number of the emergency contact
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                    Toast.makeText(context, "Emergency alert sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Inform the user that the location is not available
                    Toast.makeText(
                        context,
                        "Unable to retrieve user's location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    fun hasSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun dismissAlert() {
        if (isAlertShowing) {
            alert_dialog.dismiss()
            isAlertShowing = false
        }
    }
}

