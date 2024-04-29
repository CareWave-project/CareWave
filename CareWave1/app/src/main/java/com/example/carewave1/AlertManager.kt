package com.example.carewave1

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.PixelFormat
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.telephony.SmsManager
import android.widget.Toast
import java.util.*


class AlertManager(private val context: Context) {

    private var isAlertShowing = false
    private lateinit var windowManager: WindowManager
    private lateinit var alertDialog: AlertDialog

    fun triggerAlert() {
        if (!isAlertShowing) {
            isAlertShowing = true

            // Create alert dialog to ask user whether it's a false alarm or SOS
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Alert!")
            alertDialogBuilder.setMessage("Was this a false alarm or SOS?")
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton("False Alarm") { dialogInterface: DialogInterface, _: Int ->
                // If user responds as false alarm, dismiss the alert
                dialogInterface.dismiss()
                dismissAlert()
            }
            alertDialogBuilder.setNegativeButton("SOS") { dialogInterface: DialogInterface, _: Int ->
                // If user responds as SOS, send emergency alert
                dialogInterface.dismiss()
                sendEmergencyAlert()
                dismissAlert()
            }
            alertDialog = alertDialogBuilder.create()

            // Create WindowManager and add the AlertDialog as a system alert window
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val params: WindowManager.LayoutParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT
                )
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT
                )
            }

            params.gravity = Gravity.CENTER
            windowManager.addView(alertDialog.window!!.decorView, params)

            // Set a timer to automatically dismiss the alert if user doesn't respond within 10 seconds
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (isAlertShowing) {
                        dismissAlert()
                        sendEmergencyAlert()
                    }
                }
            }, 10000)
        }
    }

    private fun sendEmergencyAlert() {
        // Get user's location
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location? = try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (ex: SecurityException) {
            null
        }

        // Send emergency alert with user's location to emergency contacts
        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            val message = "Emergency SOS! User's current location: $latitude, $longitude"

            try {
                // Replace phoneNumber with the actual phone number of the emergency contact
                val phoneNumber = "8129076731"
                @Suppress("DEPRECATION")
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Toast.makeText(context, "Emergency alert sent successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to send emergency alert", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(context, "Unable to retrieve user's location", Toast.LENGTH_SHORT).show()
        }
    }


    private fun dismissAlert() {
        // Dismiss the alert dialog and remove it from the window manager
        alertDialog.dismiss()
        windowManager.removeView(alertDialog.window!!.decorView)
        isAlertShowing = false
    }
}
