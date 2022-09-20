package com.example.houseops.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.houseops.R
import com.example.houseops.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        listeners()
    }

    private fun listeners() {

        binding.signUpText.setOnClickListener {

            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {

            loginUser()
        }
    }

    private fun loginUser() {

        //  Check if input fields are empty or not
        val email = binding.emailSignIn.text.toString()
        val password = binding.passwordSignIn.text.toString()

        if (email.isBlank())
            Toast.makeText(this@LoginActivity, "email cannot be empty", Toast.LENGTH_SHORT).show()
        else if (password.isBlank())
            Toast.makeText(this@LoginActivity, "password cannot be blank", Toast.LENGTH_SHORT)
                .show()
        else {
            //  Everything is fine
            allowAccessToAccount(email, password)
        }
    }

    private fun allowAccessToAccount(email: String, password: String) {

        //  Sign in the user using the email address
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    //  Logged in Successfully
                    Toast.makeText(this@LoginActivity, "Logged in successfully!", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this@LoginActivity, "Something went wrong...", Toast.LENGTH_SHORT).show()
                }
            }

    }

}










