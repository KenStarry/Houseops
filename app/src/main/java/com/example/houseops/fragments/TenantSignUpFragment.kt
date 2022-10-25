package com.example.houseops.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.houseops.R
import com.example.houseops.Utilities
import com.example.houseops.activities.MainActivity
import com.example.houseops.collections.UsersCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TenantSignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var utils: Utilities

    private lateinit var signUpBtn: AppCompatButton
    private lateinit var progressBarTenant: ProgressBar
    private lateinit var username: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var imageTenant: RoundedImageView

    private lateinit var imageUri: Uri

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
        utils = Utilities(requireContext())

        signUpBtn = view.findViewById(R.id.signUpButtonTenant)
        username = view.findViewById(R.id.usernameTenantSignUp)
        phone = view.findViewById(R.id.phoneTenantSignUp)
        email = view.findViewById(R.id.emailTenantSignUp)
        password = view.findViewById(R.id.passwordTenantSignUp)
        passwordConfirm = view.findViewById(R.id.passwordConfirmTenantSignUp)
        progressBarTenant = view.findViewById(R.id.progressBarTenant)
        imageTenant = view.findViewById(R.id.imageTenantSignup)

        listeners(view)
        return view

    }

    private fun listeners(v: View) {

        signUpBtn.setOnClickListener {
            verifyDetails()
        }

        imageTenant.setOnClickListener {

            openFileChooser()

        }
    }

    //  Function to verify user details
    private fun verifyDetails() {

        utils.showViewAHideViewB(progressBarTenant, signUpBtn)

        val username = username.text.toString()
        val phone = phone.text.toString()
        val email =email.text.toString()
        val password = password.text.toString()
        val passwordConfirm = passwordConfirm.text.toString()

        if (username.isBlank()) {
            toast("Enter username")
            utils.showViewAHideViewB(signUpBtn, progressBarTenant)

        } else if (email.isBlank()) {
            toast("Enter email")
            utils.showViewAHideViewB(signUpBtn, progressBarTenant)

        } else if (password.isBlank()) {
            toast("Enter password")
            utils.showViewAHideViewB(signUpBtn, progressBarTenant)

        } else if (passwordConfirm.isBlank()) {
            toast("confirm your password")
            utils.showViewAHideViewB(signUpBtn, progressBarTenant)

        } else {
            //  Check if the user confirmed the password correctly
            if (password == passwordConfirm) {
                //  Proceed with authentication
                lifecycleScope.launch(Dispatchers.IO) {

                    withContext(Dispatchers.Default) {
                        createUserAccount(
                            email,
                            password
                        )
                        createUsersCollection(
                            username,
                            phone,
                            email,
                            password
                        )
                        uploadImageToFirestore(email)
                    }
                }
            }

        }
    }

    //  Function to create the user account
    private fun createUserAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                    utils.showViewAHideViewB(signUpBtn, progressBarTenant)

                    toast("Account created successfully")
                }
            }
    }

    //  Allow user to pick images from the file manager
    private fun openFileChooser() {

        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        pickImage.launch(intent)
    }

    private suspend fun uploadImageToFirestore(email: String) {

        storageRef = FirebaseStorage.getInstance().getReference("tenant_images")

        val fileRef = storageRef.child(
            "${System.currentTimeMillis()}.${utils.getFileExtension(imageUri)}"
        )

        //  Add the image to cloud firestor
        fileRef.putFile(imageUri)
            .addOnSuccessListener {

                //  Grab the download url
                fileRef.downloadUrl.addOnSuccessListener { url ->

                    //  Set the url in the user data in the collection
                    val userRef = db.collection("users").document(email)
                    userRef.update("tenantImageUrl", url)
                }

            }
            .addOnFailureListener {

            }
            .addOnProgressListener {

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
            username, "", phone, email, password
        )

        db.collection("users").document(email).set(user)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                toast("Something went wrong...")
            }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->

            if (result.resultCode == Activity.RESULT_OK
                && result.data != null
                && result.data!!.data != null) {

                //  Grab the image uri
                result.data!!.data!!.let { imageUri = it }

                //  Set the image View to that image
                Picasso.get()
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .into(imageTenant)
            }
        }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}