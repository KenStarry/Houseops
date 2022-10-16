package com.example.houseops.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
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

class CaretakerActivity : AppCompatActivity() {

    private lateinit var housesAdapter: CaretakerHousesAdapter
    private lateinit var housesRecyclerView: RecyclerView
    private lateinit var binding: ActivityCaretakerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    //  Houses List
    private val categories = listOf(
        AdminCategoriesModel("Singles", R.drawable.house1),
        AdminCategoriesModel("Self-Contained", R.drawable.house2),
        AdminCategoriesModel("Mansions", R.drawable.house5),
        AdminCategoriesModel("Bedsitters", R.drawable.house3),
        AdminCategoriesModel("One Bedroom", R.drawable.house4),
        AdminCategoriesModel("Two Bedroom", R.drawable.house1)
    )

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

        sharedPref = getSharedPreferences(Constants().caretakerDetails, AppCompatActivity.MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()

        listeners()

    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth
        db = Firebase.firestore

        val currentUser = auth.currentUser

        //  Listen for changes on the caretaker's collection and apartments collection
        queryApartments(currentUser)
        queryCaretakerDetails(currentUser)
    }

    private fun queryApartments(currentUser: FirebaseUser?) {

        val apartment = sharedPref.getString(Constants().caretakerApartment, "")
        db.collection("apartments").document(apartment!!).collection("houses")
            .addSnapshotListener{querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                val housesList = ArrayList<HouseModel>()

                for (snapshot in querySnapshot!!) {

                    val house: HouseModel = snapshot.toObject()
                    housesList.add(house)
                }

                //  Setup recyclerview
//                setupRecyclerView(housesList)
            }
    }

    private fun setupRecyclerView(housesList: ArrayList<HouseModel>) {

        housesAdapter = CaretakerHousesAdapter(housesList)

        housesRecyclerView.adapter = housesAdapter
        housesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun queryCaretakerDetails(currentUser: FirebaseUser?) {

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