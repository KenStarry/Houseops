package com.example.houseops.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.houseops.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        listeners()
    }

    private fun listeners() {

        sign_up_text.setOnClickListener {


        }
    }

}