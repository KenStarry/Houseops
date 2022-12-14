package com.example.houseops.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.Constants
import com.example.houseops.R
import com.example.houseops.adapters.AdminCategoriesAdapter
import com.example.houseops.adapters.CaretakerHousesAdapter
import com.example.houseops.collections.CaretakerCollection
import com.example.houseops.databinding.ActivityCaretakerBinding
import com.example.houseops.fragments.dialogs.AddHouseBottomSheet
import com.example.houseops.models.AdminCategoriesModel
import com.example.houseops.models.HouseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class CaretakerActivity : AppCompatActivity() {

    private lateinit var housesAdapter: CaretakerHousesAdapter
    private lateinit var housesRecyclerView: RecyclerView
    private lateinit var svgHolder: LinearLayout
    private lateinit var binding: ActivityCaretakerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCaretakerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Setting our toolbar
        val toolbar: Toolbar = findViewById(R.id.caretakerToolbar)
        setSupportActionBar(toolbar)

        housesRecyclerView = findViewById(R.id.caretaker_recycler_view)

        supportActionBar!!.title = "Caretaker"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //  Get the caretaker details from shared preferences
        sharedPref = getSharedPreferences(Constants().caretakerDetails, AppCompatActivity.MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()

        auth = Firebase.auth
        db = Firebase.firestore

        val currentUser = auth.currentUser

        //  Listen for changes on the caretaker's collection and apartments collection
        lifecycleScope.launch(Dispatchers.IO) {

            async { queryCaretakerDetails(currentUser) }.await()
            async { queryApartments(currentUser) }.await()
        }


        listeners()

    }

    override fun onStart() {
        super.onStart()
    }

    private suspend fun queryApartments(currentUser: FirebaseUser?) {

        val apartment = sharedPref.getString(Constants().caretakerApartment, "")
//        val apartment = intent.getStringExtra("apartment_extra")

        db.collection("apartments").document(apartment!!).collection("houses")
            .addSnapshotListener{querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                if (querySnapshot!!.isEmpty) {
                    housesRecyclerView.visibility = View.GONE
                    svgHolder.visibility = View.VISIBLE

                } else {
                    housesRecyclerView.visibility = View.VISIBLE
                    svgHolder.visibility = View.GONE

                    val housesList = ArrayList<HouseModel>()

                    for (snapshot in querySnapshot) {

                        val house: HouseModel = snapshot.toObject()
                        housesList.add(house)
                    }

                    //  Setup recyclerview
                    setupRecyclerView(housesList)
                }
            }
    }

    private fun setupRecyclerView(housesList: ArrayList<HouseModel>) {

        housesAdapter = CaretakerHousesAdapter(this, housesList)

        housesRecyclerView.adapter = housesAdapter
        housesRecyclerView.layoutManager = LinearLayoutManager(this)
        toast("Queried successfully")
    }

    private suspend fun queryCaretakerDetails(currentUser: FirebaseUser?) {

        db.collection("caretakers")
            .whereEqualTo("emailAddress", currentUser!!.email)
            .limit(1)
            .addSnapshotListener{querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                //  Query the results
                var apartment = ""
                var name = ""
                var image = ""

                for (snapshot in querySnapshot!!) {

                    val caretaker: CaretakerCollection = snapshot.toObject()

                    apartment = caretaker.apartment
                    name = caretaker.username
                }

                sharedPrefEditor.putString(Constants().caretakerApartment, apartment)
                sharedPrefEditor.commit()

                binding.caretakerName.text = name
                binding.caretakerApartments.text= apartment

            }
    }

    private fun listeners() {

        svgHolder = findViewById(R.id.empty_svg_holder)

        binding.caretakerFab.setOnClickListener {

            //  Open the bottom sheet dialog
            val dialog = AddHouseBottomSheet()
            dialog.show(supportFragmentManager, "AddHouseDialog")
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this@CaretakerActivity, message, Toast.LENGTH_SHORT).show()
    }
}