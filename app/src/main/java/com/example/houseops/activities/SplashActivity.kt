package com.example.houseops.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.houseops.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val timeout: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashHandler()
    }

    override fun onStart() {
        super.onStart()

        checkDarkMode()
    }

    private fun splashHandler() {

        Handler(Looper.getMainLooper()).postDelayed({

            //  Start the mainactivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }, timeout)
    }

    private fun checkDarkMode() {

        //  Check if night mode is enabled or not
        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK

        when (nightModeFlags) {

            //  Night mode enabled
            Configuration.UI_MODE_NIGHT_YES -> {
                splash_logo.setImageResource(R.drawable.houseops_logo_white_new)
            }

            //  Night mode disabled
            Configuration.UI_MODE_NIGHT_NO -> {
                splash_logo.setImageResource(R.drawable.houseops_logo_dark_new)
            }

            //  Night mode undefined
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                splash_logo.setImageResource(R.drawable.houseops_logo_dark_new)
            }
        }
    }

}