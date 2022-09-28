package com.example.houseops.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.houseops.R
import com.example.houseops.Utilities
import com.example.houseops.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var util: Utilities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.forgotToolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        auth = Firebase.auth
        util = Utilities(this)

        binding.submitForgotPasswordButton.setOnClickListener {
            val userEmail = binding.emailForgot.text.toString()
            beginRecovery(userEmail)
        }
    }

    //  Begin password recovery
    private fun beginRecovery(userEmail: String) {

        util.showViewAHideViewB(binding.forgotPassProgressBar, binding.submitForgotPasswordButton)

        auth.sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    util.toast("Email sent successfully!")
                    util.showViewAHideViewB(binding.submitForgotPasswordButton, binding.forgotPassProgressBar)

                } else {

                    util.toast("Error occured!")
                    util.showViewAHideViewB(binding.submitForgotPasswordButton, binding.forgotPassProgressBar)
                }
            }
            .addOnFailureListener { task ->

                util.toast("Something went wrong...")
                util.showViewAHideViewB(binding.submitForgotPasswordButton, binding.forgotPassProgressBar)
            }
    }
}