package com.example.houseops.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.houseops.R
import com.example.houseops.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private val timeout: Long = 4000
    private lateinit var binding: ActivitySplashBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        splashHandler()

        //  Check if darkmode is enabled or not
        checkDarkMode()
    }

    private fun splashHandler() {

        Handler(Looper.getMainLooper()).postDelayed({

            //  Start the login activity if the user is not logged in or Main Activity otherwise
            val currentUser = auth.currentUser

            if (currentUser != null) {

                //  Start the mainactivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, timeout)
    }

    private fun checkDarkMode() {

        //  Check if night mode is enabled or not
        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK

        when (nightModeFlags) {

            //  Night mode enabled
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.splashLogo.setImageResource(R.drawable.houseops_logo_white_new)
            }

            //  Night mode disabled
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.splashLogo.setImageResource(R.drawable.houseops_logo_dark_new)
            }

            //  Night mode undefined
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                binding.splashLogo.setImageResource(R.drawable.houseops_logo_dark_new)
            }
        }
    }
}