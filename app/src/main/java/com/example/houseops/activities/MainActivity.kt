package com.example.houseops.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.houseops.R
import com.example.houseops.databinding.ActivityLoginBinding
import com.example.houseops.databinding.ActivityMainBinding
import com.example.houseops.fragments.BookedFragment
import com.example.houseops.fragments.BookmarksFragment
import com.example.houseops.fragments.HomeFragment
import com.example.houseops.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore
        auth = Firebase.auth

        listeners()
        changeStatusBarColor()
    }

    private fun listeners() {

        binding.bottomNavigationView.setOnItemSelectedListener(this)
    }


    override fun onStart() {
        super.onStart()

        //  Show the home fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, HomeFragment())
            .commit()

        //  Watch the current user's activities
        val currentUser = auth.currentUser

        Toast.makeText(this, currentUser!!.email, Toast.LENGTH_SHORT).show()
    }


    //  Override on back pressed method to prevent fragments from prepopulating
    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0)
            super.onBackPressed()
        else {
            val fm = supportFragmentManager

            fm.beginTransaction()
                .replace(R.id.fragments_container, HomeFragment())
                .commit()

            //  Pop everything in the backstack
            for (i in 0..fm.backStackEntryCount) {
                fm.popBackStack()
            }
        }

    }


    //  The bottom navigation menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home_menu -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, HomeFragment())
                    .commit()
            }

            R.id.booked_menu -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, BookedFragment())
                    .addToBackStack(null)
                    .commit()
            }

            R.id.bookmarks_menu -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, BookmarksFragment())
                    .addToBackStack(null)
                    .commit()
            }

            R.id.profile_menu -> {

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return true
    }


    private fun changeStatusBarColor() {

        this.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.accent_blue_dull)
        }
    }
}










