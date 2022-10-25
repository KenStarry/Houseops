package com.example.houseops.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.houseops.R
import com.example.houseops.Utilities
import com.example.houseops.activities.MainActivity
import com.example.houseops.collections.CaretakerCollection
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

class CaretakerSignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var utils: Utilities
    private lateinit var storageRef: StorageReference

    private lateinit var apartment: EditText
    private lateinit var id: EditText
    private lateinit var username: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var signUpBtn: AppCompatButton
    private lateinit var progressBarCaretaker: ProgressBar
    private lateinit var imageCaretaker: RoundedImageView

    private lateinit var imageUri: Uri

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
        utils = Utilities(requireContext())

        apartment = view.findViewById(R.id.apartmentsCaretakerSignUp)
        id = view.findViewById(R.id.idCaretakerSignUp)
        username = view.findViewById(R.id.usernameCaretakerSignUp)
        phone = view.findViewById(R.id.phoneCaretakerSignUp)
        email = view.findViewById(R.id.emailCaretakerSignUp)
        password = view.findViewById(R.id.passwordCaretakerSignUp)
        passwordConfirm = view.findViewById(R.id.passwordConfirmCaretakerSignUp)
        signUpBtn = view.findViewById(R.id.signUpButtonCaretaker)
        progressBarCaretaker = view.findViewById(R.id.progressBarCaretaker)
        imageCaretaker = view.findViewById(R.id.imageCaretakerSignup)

        listeners()

        return view
    }

    private fun listeners() {

        signUpBtn.setOnClickListener {
            verifyDetails()
        }

        imageCaretaker.setOnClickListener {
            openFileChooser()
        }
    }

    private fun verifyDetails() {

        //  Start the progress bar
        utils.showViewAHideViewB(progressBarCaretaker, signUpBtn)

        val apartmentText = apartment.text.toString()
        val idText = id.text.toString()
        val usernameText = username.text.toString()
        val phoneText = phone.text.toString()
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val passwordConfirmText = passwordConfirm.text.toString()

        if (usernameText.isBlank()) {
            toast("Enter username")
            utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

        } else if (apartmentText.isBlank()) {
            toast("Enter Apartment")
            utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

        } else if (idText.isBlank()) {
            toast("Enter id number")
            utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

        } else if (emailText.isBlank()) {
            toast("Enter email")
            utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

        } else if (passwordText.isBlank()) {
            toast("Enter password")
            utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

        } else if (passwordConfirmText.isBlank()) {
            toast("confirm your password")
            utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

        } else {
            //  Check if the user confirmed the password correctly
            if (passwordText == passwordConfirmText) {
                //  Proceed with authentication
                lifecycleScope.launch(Dispatchers.IO) {
                    launch(Dispatchers.Main) {
                        toast("Began process")
                    }
                    async { createUserAccount(emailText, passwordText) }.await()
                    async { createCaretakersCollection(
                        apartmentText,
                        idText,
                        usernameText,
                        phoneText,
                        emailText,
                        passwordText
                    ) }.await()
                    async { uploadImageToFirestore(emailText) }.await()
                }
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
                    val caretakerRef = db.collection("caretakers").document(email)
                    caretakerRef.update("caretakerImageUrl", url)
                }

            }
            .addOnFailureListener {

            }
            .addOnProgressListener {

            }
    }

    private fun createUserAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                    utils.showViewAHideViewB(signUpBtn, progressBarCaretaker)

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
            apartmentText, idText, usernameText, emailText, phoneText, passwordText, false, ""
        )

        db.collection("caretakers").add(caretaker)
            .addOnSuccessListener { docRef ->

                Log.d(TAG, "document added successfully!")
            }
            .addOnFailureListener { e ->

                Log.w(TAG, "error Adding document", e)
            }

    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

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
                    .into(imageCaretaker)
            }
        }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}