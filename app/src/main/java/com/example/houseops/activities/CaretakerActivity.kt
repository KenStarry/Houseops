package com.example.houseops.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.houseops.R
import com.example.houseops.adapters.AdminCategoriesAdapter
import com.example.houseops.collections.CaretakerCollection
import com.example.houseops.databinding.ActivityCaretakerBinding
import com.example.houseops.fragments.dialogs.AddHouseBottomSheet
import com.example.houseops.models.AdminCategoriesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CaretakerActivity : AppCompatActivity() {

    private lateinit var adapter: AdminCategoriesAdapter
    private lateinit var binding: ActivityCaretakerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

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

        supportActionBar!!.title = "Caretaker"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        listeners()

    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth
        db = Firebase.firestore

        val currentUser = auth.currentUser

        //  Listen for changes on the caretaker's collection
        queryCaretakerDetails(currentUser)
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