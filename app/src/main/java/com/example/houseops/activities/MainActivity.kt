package com.example.houseops.activities

import android.content.Context
import android.content.SharedPreferences
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
import com.example.houseops.collections.CaretakerCollection
import com.example.houseops.collections.UsersCollection
import com.example.houseops.databinding.ActivityLoginBinding
import com.example.houseops.databinding.ActivityMainBinding
import com.example.houseops.fragments.BookedFragment
import com.example.houseops.fragments.BookmarksFragment
import com.example.houseops.fragments.HomeFragment
import com.example.houseops.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var bundle: Bundle

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bundle = Bundle()
        db = Firebase.firestore
        auth = Firebase.auth
        sharedPrefs = getSharedPreferences("user_type", Context.MODE_PRIVATE)

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

    }


    //  Override on back pressed method to prevent fragments from prepopulating
    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0)
            super.onBackPressed()
        else {
            val fm = supportFragmentManager

            val frag = HomeFragment()
            frag.arguments = bundle

            fm.beginTransaction()
                .replace(R.id.fragments_container, frag)
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

                val frag = HomeFragment()
                frag.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, frag)
                    .commit()
            }

            R.id.booked_menu -> {

                val frag = BookedFragment()
                frag.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, frag)
                    .addToBackStack(null)
                    .commit()
            }

            R.id.bookmarks_menu -> {

                val frag = BookmarksFragment()
                frag.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, frag)
                    .addToBackStack(null)
                    .commit()
            }

            R.id.profile_menu -> {

                val frag = ProfileFragment()
                frag.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, frag)
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










