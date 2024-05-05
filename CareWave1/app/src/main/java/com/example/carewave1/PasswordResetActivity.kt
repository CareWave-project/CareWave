package com.example.carewave1
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carewave1.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var buttonResetPassword: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        mAuth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.userNameR)
        buttonResetPassword = findViewById(R.id.buttonResetR)

        buttonResetPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            sendPasswordResetEmail(email)
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(this@PasswordResetActivity,
                        "Password reset email sent",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // Failed to send password reset email
                    Toast.makeText(this@PasswordResetActivity,
                        "Failed to send password reset email",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
