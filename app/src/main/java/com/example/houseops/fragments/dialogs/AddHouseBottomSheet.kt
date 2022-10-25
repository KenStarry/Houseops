package com.example.houseops.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.Lottie
import com.example.houseops.Constants
import com.example.houseops.R
import com.example.houseops.Utilities
import com.example.houseops.activities.CaretakerActivity
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*

class AddHouseBottomSheet : BottomSheetDialogFragment() {

    private val TAG = "add house bottomsheet"

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var utils: Utilities

    private lateinit var houseImage: CardView
    private lateinit var housePrice: TextInputEditText
    private lateinit var houseRoomsAvailable: TextInputEditText
    private lateinit var houseDescription: EditText
    private lateinit var houseAddBtn: Button
    private lateinit var houseLottieSuccess: LinearLayout
    private lateinit var houseMainContent: NestedScrollView
    private lateinit var houseAddProgress: ProgressBar

    private lateinit var bedsitter: RelativeLayout
    private lateinit var single: RelativeLayout
    private lateinit var oneBedroom: RelativeLayout
    private lateinit var twoBedroom: RelativeLayout
    private lateinit var threeBedroom: RelativeLayout
    private lateinit var mansion: RelativeLayout
    private lateinit var other: RelativeLayout

    private lateinit var imageUriList: ArrayList<Uri>

    private lateinit var adapter: HouseImagesAdapter
    private lateinit var recyclerViewHouses: RecyclerView
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

    private fun setupRecyclerView() {

        recyclerViewHouses.adapter = adapter
        recyclerViewHouses.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

    }

    private fun listeners(view: View) {

        //  Add the house to the apartments collection
        houseAddBtn.setOnClickListener {

            //  Pick the specific apartment that this caretaker is in charge of
            val currentUser = auth.currentUser
            val apartment = sharedPref.getString(Constants().caretakerApartment, "")

            utils.showViewAHideViewB(houseAddProgress, it)

            //  Add images to firebase storage using coroutines
            lifecycleScope.launch(Dispatchers.IO) {

                async { addFilesToCloudStorage(apartment) }.await()
                async { addHouseToApartmentsCollection(apartment) }.await()
            }
        }

        //  Allow the caretaker to choose an image from gallery
        houseImage.setOnClickListener {

            //  Gallery intent
            openFileChooser()
        }

        //  Category buttons
        single.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "single"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                bedsitter.background = color
                oneBedroom.background = color
                twoBedroom.background = color
                threeBedroom.background = color
                mansion.background = color
                other.background = color
            }
        }

        bedsitter.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "bedsitter"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                single.background = color
                oneBedroom.background = color
                twoBedroom.background = color
                threeBedroom.background = color
                mansion.background = color
                other.background = color
            }
        }

        oneBedroom.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "one bedroom"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                single.background = color
                bedsitter.background = color
                twoBedroom.background = color
                threeBedroom.background = color
                mansion.background = color
                other.background = color
            }
        }

        twoBedroom.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "two bedroom"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                single.background = color
                bedsitter.background = color
                oneBedroom.background = color
                threeBedroom.background = color
                mansion.background = color
                other.background = color
            }
        }

        threeBedroom.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "three bedroom"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                single.background = color
                bedsitter.background = color
                oneBedroom.background = color
                twoBedroom.background = color
                mansion.background = color
                other.background = color
            }
        }

        mansion.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "mansion"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                single.background = color
                bedsitter.background = color
                oneBedroom.background = color
                twoBedroom.background = color
                threeBedroom.background = color
                other.background = color
            }
        }

        other.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.pill_filled)
            houseCategory = "other"

            ContextCompat.getDrawable(requireActivity(), R.drawable.pill_stroke).let { color ->
                single.background = color
                bedsitter.background = color
                oneBedroom.background = color
                twoBedroom.background = color
                threeBedroom.background = color
                mansion.background = color
            }
        }
    }

    private fun openFileChooser() {

        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        pickImage.launch(intent)

    }

    private fun initializeVariables(view: View) {

        db = Firebase.firestore
        auth = Firebase.auth
        sharedPref = requireActivity().getSharedPreferences(
            Constants().caretakerDetails,
            Context.MODE_PRIVATE
        )
        utils = Utilities(requireActivity())
        imageUriList = ArrayList()

        adapter = HouseImagesAdapter(imageUriList)
        recyclerViewHouses = view.findViewById(R.id.add_house_recycler)

        houseImage = view.findViewById(R.id.add_house_image)
        houseRoomsAvailable = view.findViewById(R.id.houseRoomsAvailable)
        housePrice = view.findViewById(R.id.housePrice)
        houseDescription = view.findViewById(R.id.house_description)
        houseAddBtn = view.findViewById(R.id.houseAddBtn)
        houseLottieSuccess = view.findViewById(R.id.bottom_sheet_lottie_success)
        houseMainContent = view.findViewById(R.id.bottom_sheet_main_content)
        houseAddProgress = view.findViewById(R.id.add_house_progress_bar)

        bedsitter = view.findViewById(R.id.house_category_bedsitter)
        single = view.findViewById(R.id.house_category_single)
        oneBedroom = view.findViewById(R.id.house_category_one_bedroom)
        twoBedroom = view.findViewById(R.id.house_category_two_bedroom)
        threeBedroom = view.findViewById(R.id.house_category_three_bedroom)
        mansion = view.findViewById(R.id.house_category_mansion)
        other = view.findViewById(R.id.house_category_other)
    }

    //  Function to add a house to the apartments collection
    private suspend fun addHouseToApartmentsCollection(apartment: String?) {

        val price = housePrice.text.toString()
        val houseRooms = houseRoomsAvailable.text.toString()
        val houseCat = houseCategory
        val houseDesc = houseDescription.text.toString()

        val houseModel =
            HouseModel(apartment, "vacant", price, houseRooms, houseCat, houseDesc, ArrayList())

        db.collection("apartments").document(apartment!!).collection("houses").document(houseCat)
            .set(houseModel, SetOptions.merge())
            .addOnSuccessListener { doc ->

                //  Show the lottie and hide the main content
                utils.showViewAHideViewB(houseLottieSuccess, houseMainContent)

                lifecycleScope.launch(Dispatchers.Main) {
                    delay(3000L)
                    this@AddHouseBottomSheet.dismiss()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addFilesToCloudStorage(apartment: String?) {

        //  We need to add the image URIs to Firebase storage
        storageRef = FirebaseStorage.getInstance().getReference("${apartment}uploads")

        //  There is at least one image
        if (imageUriList.size > 0) {

            val downloadUrlList: MutableList<Uri> = ArrayList()

            for (uri in imageUriList) {

                val fileReference = storageRef.child(
                    "${System.currentTimeMillis()}.${utils.getFileExtension(uri)}"
                )

                fileReference.putFile(uri)
                    .addOnSuccessListener {

                        //  Get the download URL
                        fileReference.downloadUrl.addOnSuccessListener { url ->

                            downloadUrlList.add(url)

                            val houseRef = db.collection("apartments").document(apartment!!)
                                .collection("houses").document(houseCategory)

                            houseRef.update("houseImageDownloadUriList", downloadUrlList)
                                .addOnSuccessListener {
                                }
                        }
                    }
                    .addOnProgressListener {

                    }
                    .addOnFailureListener {

                        Log.d(TAG, it.toString())
                    }
            }
        }

    }

    //  Start an activity for image result
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK
                && result.data != null
                && result.data!!.data != null
            ) {

                //  Pick the image
                val uri = result.data!!.data!!
                imageUriList.add(uri)

                // Display the recyclerview
                setupRecyclerView()
            }

        }
}








