package com.example.houseops.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.houseops.R
import com.example.houseops.activities.LoginActivity
import com.example.houseops.collections.CaretakerCollection
import com.example.houseops.collections.UsersCollection
import com.example.houseops.fragments.dialogs.ProfileDetailsBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var userProfile: ImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        userProfile = view.findViewById(R.id.user_profile)

        listeners()

        return view
    }

    private fun listeners() {

        userProfile.setOnClickListener {
            //  Open Bottom Sheet Dialog
            openProfileDetailsBottomSheet()
        }
    }

    private fun openProfileDetailsBottomSheet() {

        val dialog = ProfileDetailsBottomSheet()
        dialog.show(requireActivity().supportFragmentManager, "ProfileDetailsDialog")
    }

    override fun onStart() {
        super.onStart()

        auth = Firebase.auth
        db = Firebase.firestore

        sharedPrefs = requireContext().getSharedPreferences("user_type", Context.MODE_PRIVATE)
        val currentUser = auth.currentUser

        if (sharedPrefs.getString("type", "") == "user") {
            queryUserDetails(currentUser)
        } else
            queryCaretakerDetails(currentUser)
    }

    private fun queryCaretakerDetails(currentUser: FirebaseUser?) {

        db.collection("caretakers")
            .whereEqualTo("emailAddress", currentUser!!.email)
            .addSnapshotListener { querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                var apartment = ""
                var email = ""
                var id = ""
                var username = ""
                var isVerified = false

                for (snapshot in querySnapshot!!) {

                    val caretaker: CaretakerCollection = snapshot.toObject()

                    apartment = caretaker.apartment
                    email = caretaker.emailAddress
                    id = caretaker.idNo
                    username = caretaker.username
                    isVerified = caretaker.isVerfied
                }
            }
    }

    private fun queryUserDetails(currentUser: FirebaseUser?) {

        db.collection("users")
            .whereEqualTo("emailAddress", currentUser!!.email)
            .addSnapshotListener { querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                var email = ""
                var phone = ""
                var username = ""

                for (snapshot in querySnapshot!!) {

                    val user: UsersCollection = snapshot.toObject()

                    email = user.email!!
                    phone = user.phone!!
                    username = user.username!!
                }
            }
    }
}