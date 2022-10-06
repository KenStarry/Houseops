package com.example.houseops.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.houseops.R
import com.example.houseops.activities.CaretakerActivity
import com.example.houseops.collections.CaretakerCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CaretakerSignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var apartment: EditText
    private lateinit var id: EditText
    private lateinit var username: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var signUpBtn: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_caretaker_sign_up, container, false)

        auth = Firebase.auth
        db = Firebase.firestore

        apartment = view.findViewById(R.id.apartmentsCaretakerSignUp)
        id = view.findViewById(R.id.idCaretakerSignUp)
        username = view.findViewById(R.id.usernameCaretakerSignUp)
        phone = view.findViewById(R.id.phoneCaretakerSignUp)
        email = view.findViewById(R.id.emailCaretakerSignUp)
        password = view.findViewById(R.id.passwordCaretakerSignUp)
        passwordConfirm = view.findViewById(R.id.passwordConfirmCaretakerSignUp)
        signUpBtn = view.findViewById(R.id.signUpButtonCaretaker)

        listeners()

        return view
    }

    private fun listeners() {

        signUpBtn.setOnClickListener {
            verifyDetails()
        }
    }

    private fun verifyDetails() {
        val apartmentText = apartment.text.toString()
        val idText = id.text.toString()
        val usernameText = username.text.toString()
        val phoneText = phone.text.toString()
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val passwordConfirmText = passwordConfirm.text.toString()

        if (usernameText.isBlank()) {
            toast("Enter username")

        } else if (apartmentText.isBlank()) {
            toast("Enter Apartment")

        } else if (idText.isBlank()) {
            toast("Enter id number")

        } else if (emailText.isBlank()) {
            toast("Enter email")

        } else if (passwordText.isBlank()) {
            toast("Enter password")

        } else if (passwordConfirmText.isBlank()) {
            toast("confirm your password")

        } else {
            //  Check if the user confirmed the password correctly
            if (passwordText == passwordConfirmText) {
                //  Proceed with authentication
                createUserAccount(emailText, passwordText)
                createCaretakersCollection(
                    apartmentText,
                    idText,
                    usernameText,
                    phoneText,
                    emailText,
                    passwordText
                )
            }

        }
    }

    private fun createUserAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val intent = Intent(requireActivity(), CaretakerActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                    toast("Account Created Successfully")
                }
            }
    }

    private fun createCaretakersCollection(
        apartmentText: String,
        idText: String,
        usernameText: String,
        phoneText: String,
        emailText: String,
        passwordText: String
    ) {

        val caretaker = CaretakerCollection(
            apartmentText, idText, usernameText, emailText, phoneText, passwordText, false
        )

        db.collection("caretakers").add(caretaker)
            .addOnSuccessListener { docRef ->

                Log.d(TAG, "document added successfully!")
            }
            .addOnFailureListener { e ->

                Log.w(TAG, "error Adding document", e)
            }

    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}