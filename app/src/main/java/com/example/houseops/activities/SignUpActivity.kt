package com.example.houseops.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.houseops.R
import com.example.houseops.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        listeners()
    }

    private fun listeners() {

        binding.signUpButton.setOnClickListener {
            verifyDetails()
        }
    }

    //  Function to verify user details
    private fun verifyDetails() {

        val username = binding.usernameSignUp.text.toString()
        val email = binding.emailSignUp.text.toString()
        val password = binding.passwordSignUp.text.toString()
        val passwordConfirm = binding.passwordConfirmSignUp.text.toString()

        if (username.isBlank()) {
            toast("Enter username")

        } else if (email.isBlank()){
            toast("Enter email")

        } else if (password.isBlank()) {
            toast("Enter password")

        } else if (passwordConfirm.isBlank()) {
            toast("confirm your password")

        } else {
            //  Check if the user confirmed the password correctly
            if (password == passwordConfirm) {
                //  Proceed with authentication
                createUserAccount(email, password)
            }

        }
    }

    //  Function to create the user account
    private fun createUserAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    //  Created account successfully
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    toast("Created account successfully")
                }
            }

    }

    private fun toast(message: String) {
        Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
    }
}









