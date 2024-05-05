package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import com.example.carewave1.LoginActivity
import android.widget.Toast
import com.example.carewave1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    data class UserData(
        val name: String = "",
        val age: String = "",
        val contact: String=""
    )


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.loginLink.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSignup.setOnClickListener{
            val email = binding.emailS.text.toString()
            val pass=binding.passwordS.text.toString()
            val confirmPass=binding.confirmPassword.text.toString()
            val name = binding.userNameS.text.toString() // New: Get user's name
            val age = binding.age.text.toString()   // New: Get user's age
            val contact = binding.userContactTextView.text.toString() // New: Get user's contact

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty() && age.isNotEmpty() && contact.isNotEmpty() ){
                if (pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(){
                        if(it.isSuccessful){
                            val user = firebaseAuth.currentUser
                            val userId = user?.uid
                            val userRef = database.getReference("users").child(userId ?: "")
                            userRef.setValue(UserData(name, age,contact)) // New: Store user data in Firebase Realtime Database
                            val intent= Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }

                }
                else{
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this,"Please fill in all fields",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
