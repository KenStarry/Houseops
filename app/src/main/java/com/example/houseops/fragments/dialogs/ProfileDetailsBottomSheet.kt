package com.example.houseops.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.houseops.R
import com.example.houseops.Utilities
import com.example.houseops.activities.CaretakerActivity
import com.example.houseops.activities.LoginActivity
import com.example.houseops.collections.CaretakerCollection
import com.example.houseops.collections.UsersCollection
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileDetailsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var utils: Utilities

    private lateinit var profileUsername: TextView
    private lateinit var profileImage: ImageView
    private lateinit var profileCaretakerAdminBtn: Button
    private lateinit var profileDetailsLogout: ImageView

    private lateinit var apartmentExtra:String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.profile_details_bottomsheet, container, false)

        profileUsername = view.findViewById(R.id.profile_details_username)
        profileImage = view.findViewById(R.id.profile_details_image)
        profileCaretakerAdminBtn = view.findViewById(R.id.profile_caretaker_admin_btn)
        profileDetailsLogout = view.findViewById(R.id.profile_details_logout)

        //  Open caretaker activity
        profileCaretakerAdminBtn.setOnClickListener {
            Intent(requireActivity(), CaretakerActivity::class.java).also {

                it.putExtra("apartment_extra", apartmentExtra)
                startActivity(it)
            }

            this@ProfileDetailsBottomSheet.dismiss()
        }

        //  Logout the user
        profileDetailsLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        db = Firebase.firestore
        auth = Firebase.auth
        utils = Utilities(requireContext())
        sharedPrefs = requireContext().getSharedPreferences("user_type", Context.MODE_PRIVATE)

        val currentUser = auth.currentUser

        if (sharedPrefs.getString("type", "") == "user") {
            //  Show user details
            queryUserDetails(currentUser)
            profileCaretakerAdminBtn.visibility = View.GONE

        } else {
            //  Show caretaker details
            queryCaretakerDetails(currentUser)
            profileCaretakerAdminBtn.visibility = View.VISIBLE
        }


    }

    //  Logout the user
    private fun logoutUser() {

        auth.signOut()

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
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

                apartmentExtra = apartment
                profileUsername.text = username
            }
    }

    private fun queryUserDetails(currentUser: FirebaseUser?) {

        db.collection("users")
            .document(currentUser!!.email!!)
            .addSnapshotListener { snapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                val user: UsersCollection = snapshot!!.toObject()!!

                var email = user.email!!
                var phone = user.phone!!
                var username = user.username!!
                var image = user.tenantImageUrl!!


                profileUsername.text = username

                Picasso.get()
                    .load(image)
                    .fit()
                    .centerCrop()
                    .into(profileImage)
            }
    }
}