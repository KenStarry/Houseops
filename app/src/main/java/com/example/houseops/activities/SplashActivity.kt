package com.example.houseops.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.houseops.R
import com.example.houseops.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val timeout: Long = 4000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashHandler()
    }

    override fun onStart() {
        super.onStart()

        checkDarkMode()
    }

    private fun splashHandler() {

        Handler(Looper.getMainLooper()).postDelayed({

            //  Start the mainactivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

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