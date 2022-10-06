package com.example.houseops.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.houseops.R
import com.example.houseops.collections.UsersCollection
import com.example.houseops.databinding.ActivitySignUpBinding
import com.example.houseops.fragments.CaretakerSignUpFragment
import com.example.houseops.fragments.TenantSignUpFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.caretakerToggle.setOnClickListener {

            it.background = ContextCompat.getDrawable(this@SignUpActivity, R.drawable.rounded_corners_active)
            binding.tenantToggle.background = ContextCompat.getDrawable(this@SignUpActivity, R.drawable.rounded_corners)

            //  Open caretaker fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.signUpFragmentContainer, CaretakerSignUpFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.tenantToggle.setOnClickListener {

            it.background = ContextCompat.getDrawable(this@SignUpActivity, R.drawable.rounded_corners_active)
            binding.caretakerToggle.background = ContextCompat.getDrawable(this@SignUpActivity, R.drawable.rounded_corners)

            //  Open tenant fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.signUpFragmentContainer, TenantSignUpFragment())
                .addToBackStack(null)
                .commit()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.signUpFragmentContainer, TenantSignUpFragment())
            .addToBackStack(null)
            .commit()

    }
}









