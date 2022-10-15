package com.example.houseops.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.Constants
import com.example.houseops.R
import com.example.houseops.adapters.HouseImagesAdapter
import com.example.houseops.models.HouseModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.makeramen.roundedimageview.RoundedImageView
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class AddHouseBottomSheet : BottomSheetDialogFragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var houseImage: RoundedImageView
    private lateinit var houseNumber: TextInputEditText
    private lateinit var houseDescription: EditText
    private lateinit var houseAddBtn: Button

    private lateinit var vacantToggle: RelativeLayout
    private lateinit var occupiedToggle: RelativeLayout
    private lateinit var bedsitter: RelativeLayout
    private lateinit var single: RelativeLayout
    private lateinit var oneBedroom: RelativeLayout
    private lateinit var twoBedroom: RelativeLayout
    private lateinit var threeBedroom: RelativeLayout
    private lateinit var mansion: RelativeLayout
    private lateinit var other: RelativeLayout

    private lateinit var encodedImage: String

    private lateinit var adapter: HouseImagesAdapter
    private val encodedImagesList: ArrayList<String> = ArrayList()
    private lateinit var sharedPref: SharedPreferences

    private var houseStatus = "vacant"
    private var houseCategory = "single"


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.caretaker_new_house_bottomsheet, container, false)

        //  Get the views
        initializeVariables(view)
        listeners(view)

        return view
    }

    private fun listeners(view: View) {

        //  Add the house to the apartments collection
        houseAddBtn.setOnClickListener {

            //  Pick the specific apartment that this caretaker is in charge of
            val currentUser = auth.currentUser
            val apartment = sharedPref.getString(Constants().caretakerApartment, "")

            addHouseToApartmentsCollection(apartment)
        }

        //  Allow the caretaker to choose an image from gallery
        houseImage.setOnClickListener {

            //  Gallery intent
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            pickImage.launch(intent)
        }

        vacantToggle.setOnClickListener { toggle ->

            //  Change the background color of the vacant toggle button
            toggle.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners_active)
            occupiedToggle.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners)
            houseStatus = "vacant"

        }

        occupiedToggle.setOnClickListener { toggle ->

            toggle.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners_active)
            vacantToggle.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners)
            houseStatus = "occupied"
        }

        //  Category buttons
        single.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "single"

            bedsitter.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            oneBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            twoBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            threeBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            mansion.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            other.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }

        bedsitter.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "bedsitter"

            single.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            oneBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            twoBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            threeBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            mansion.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            other.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }

        oneBedroom.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "one bedroom"

            bedsitter.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            single.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            twoBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            threeBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            mansion.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            other.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }

        twoBedroom.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "two bedroom"

            bedsitter.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            oneBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            single.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            threeBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            mansion.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            other.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }

        threeBedroom.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "three bedroom"

            bedsitter.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            oneBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            twoBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            single.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            mansion.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            other.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }

        mansion.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "mansion"

            bedsitter.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            oneBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            twoBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            threeBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            single.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            other.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }

        other.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)
            houseCategory = "other"

            bedsitter.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            oneBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            twoBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            threeBedroom.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            mansion.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
            single.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text)
        }
    }

    private fun initializeVariables(view: View) {

        db = Firebase.firestore
        auth = Firebase.auth
        sharedPref = requireActivity().getSharedPreferences(Constants().caretakerDetails, Context.MODE_PRIVATE)

        houseImage = view.findViewById(R.id.add_house_image)
        houseNumber = view.findViewById(R.id.houseNumber)
        houseDescription = view.findViewById(R.id.house_description)
        houseAddBtn = view.findViewById(R.id.houseAddBtn)

        vacantToggle = view.findViewById(R.id.vacantToggle)
        occupiedToggle = view.findViewById(R.id.occupiedToggle)
        bedsitter = view.findViewById(R.id.house_category_bedsitter)
        single = view.findViewById(R.id.house_category_single)
        oneBedroom = view.findViewById(R.id.house_category_one_bedroom)
        twoBedroom = view.findViewById(R.id.house_category_two_bedroom)
        threeBedroom = view.findViewById(R.id.house_category_three_bedroom)
        mansion = view.findViewById(R.id.house_category_mansion)
        other = view.findViewById(R.id.house_category_other)
    }

    //  Function to encode our image to a string
    private fun encodeImage(uri: Uri ,bitmap: Bitmap): String {

        //  Setting our previewBitmap
        val previewBitmap = when {

            Build.VERSION.SDK_INT < 28 -> {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            }
            else -> {
                val source: ImageDecoder.Source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }

        val byteArrayOutputStream = ByteArrayOutputStream()

        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    //  Function to add a house to the apartments collection
    private fun addHouseToApartmentsCollection(apartment: String?) {

        val houseStatus = houseStatus
        val houseNo = houseNumber.text.toString()
        val houseCat = houseCategory
        val houseDesc = houseDescription.text.toString()
        val houseImageBitmap = encodedImage

        val houseModel =
            HouseModel(apartment, houseStatus, houseNo, houseCat, houseDesc, houseImageBitmap)

        db.collection("apartments").document(apartment!!).collection("houses").document(houseNo)
            .set(houseModel, SetOptions.merge())
            .addOnSuccessListener { doc ->
                Toast.makeText(requireActivity(), "House Added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, e.toString())
                Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    //  Start an activity for image result
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                //  Get the image uri
                if (result.data != null) {

                    val uri: Uri? = result.data!!.data

                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(uri!!)
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

                        //  Set the image picked by the user
                        encodedImage = encodeImage(uri, bitmap)
                        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
                        val previewBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                        houseImage.setImageBitmap(previewBitmap)

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }

        }
}








