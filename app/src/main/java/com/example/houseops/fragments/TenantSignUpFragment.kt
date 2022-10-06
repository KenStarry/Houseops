package com.example.houseops.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.houseops.R
import com.example.houseops.activities.MainActivity
import com.example.houseops.collections.UsersCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TenantSignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var signUpBtn: AppCompatButton
    private lateinit var username: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_tenant_sign_up, container, false)

        auth = Firebase.auth
        db = Firebase.firestore

        signUpBtn = view.findViewById(R.id.signUpButtonTenant)
        username = view.findViewById(R.id.usernameTenantSignUp)
        phone = view.findViewById(R.id.phoneTenantSignUp)
        email = view.findViewById(R.id.emailTenantSignUp)
        password = view.findViewById(R.id.passwordTenantSignUp)
        passwordConfirm = view.findViewById(R.id.passwordConfirmTenantSignUp)

        listeners(view)
        return view

    }

    private fun listeners(v: View) {

        signUpBtn.setOnClickListener {
            verifyDetails()
        }
    }

    //  Function to verify user details
    private fun verifyDetails() {

        val username = username.text.toString()
        val phone = phone.text.toString()
        val email =email.text.toString()
        val password = password.text.toString()
        val passwordConfirm = passwordConfirm.text.toString()

        if (username.isBlank()) {
            toast("Enter username")

        } else if (email.isBlank()) {
            toast("Enter email")

        } else if (password.isBlank()) {
            toast("Enter password")

        } else if (passwordConfirm.isBlank()) {
            toast("confirm your password")

        } else {
            //  Check if the user confirmed the password correctly
            if (password == passwordConfirm) {
                //  Proceed with authentication
                createUserAccount(email, password)
                createUsersCollection(username, phone, email, password)
            }

        }
    }

    //  Function to create the user account
    private fun createUserAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    //  Created account successfully
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                    toast("Created account successfully")
                }
            }

    }

    //  Create a collection for the users
    private fun createUsersCollection(
        username: String,
        phone: String,
        email: String,
        password: String
    ) {

        //  Create a users collection
        val user = UsersCollection(
            username, phone, email, password
        )

        db.collection("users").document(email).set(user)
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}