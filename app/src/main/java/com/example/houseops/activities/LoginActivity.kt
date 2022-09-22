package com.example.houseops.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
        Log.d("LoginActivity", "loginUser: Logging User")

        //  Show progress bar
        binding.progressBar.visibility = View.VISIBLE
        binding.signInButton.visibility = View.GONE

        //  Check if input fields are empty or not
        val email = binding.emailSignIn.text.toString()
        val password = binding.passwordSignIn.text.toString()

        if (email.isBlank()) {
            Toast.makeText(this@LoginActivity, "email cannot be empty", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.VISIBLE
            binding.signInButton.visibility = View.GONE

        }
        else if (password.isBlank()) {
            Toast.makeText(this@LoginActivity, "password cannot be blank", Toast.LENGTH_SHORT)
                .show()
            binding.progressBar.visibility = View.GONE
            binding.signInButton.visibility = View.VISIBLE
            Log.d("LoginActivity", "loginUser: Password Not reached")
        }
        else {
            //  Everything is fine
            allowAccessToAccount(email, password)
        }
    }

    private fun allowAccessToAccount(email: String, password: String) {
        Log.d("LoginActivity", "allowAccessToAccount: Allowing Access ")

        //  Sign in the user using the email address
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    binding.progressBar.visibility = View.GONE
                    binding.signInButton.visibility = View.VISIBLE

                    //  Logged in Successfully
                    if (email == "starrycodes@gmail.com") {

                        //  Admin User
                        val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                        startActivity(intent)
                        finish()

                        toast("Logged in as admin!")

                    } else {

                        //  Normal user
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                        toast("Logged in successfully!")
                    }

                } else {

                    binding.progressBar.visibility = View.GONE
                    binding.signInButton.visibility = View.VISIBLE

                    toast("Something went wrong...")
                }
            }

    }

    private fun toast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

}










