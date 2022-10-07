package com.example.houseops.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.houseops.Utilities
import com.example.houseops.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var util: Utilities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore
        util = Utilities(this)

        listeners()
    }

    private fun listeners() {

        //  Forgot password text
        binding.forgotPasswordText.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.signUpText.setOnClickListener {

            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {

            loginUser()
        }
    }

    private fun loginUser() {

        //  Show progress bar
        util.showViewAHideViewB(binding.progressBar, binding.signInButton)

        //  Check if input fields are empty or not
        val email = binding.emailSignIn.text.toString()
        val password = binding.passwordSignIn.text.toString()

        if (email.isBlank()) {
            util.toast("email cannot be empty")
            util.showViewAHideViewB(binding.signInButton, binding.progressBar)

        }
        else if (password.isBlank()) {
            util.toast("password cannot be blank")
            util.showViewAHideViewB(binding.signInButton, binding.progressBar)
        }
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

                    //  Login as a Normal user
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    util.showViewAHideViewB(binding.signInButton, binding.progressBar)
                    toast("Logged in successfully!")

                } else {

                    util.showViewAHideViewB(binding.signInButton, binding.progressBar)
                    toast("Something went wrong...")
                }
            }
            .addOnFailureListener {

                Log.w(TAG, "Error while fetching data : $it")
            }

    }

    private fun toast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

}










