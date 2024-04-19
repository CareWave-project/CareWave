package com.example.carewave1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import com.example.carewave1.LoginActivity
import android.widget.Toast
import com.example.carewave1.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.loginLink.setOnClickListener{
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSignup.setOnClickListener{
            val email = binding.emailS.text.toString()
            val pass=binding.passwordS.text.toString()
            val confirmPass=binding.confirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()){
                if (pass == confirmPass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(){
                        if(it.isSuccessful){
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
            }

        }
    }
}