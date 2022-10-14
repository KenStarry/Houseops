package com.example.houseops.fragments.dialogs

import android.app.Activity
import android.app.Dialog
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.R
import com.example.houseops.adapters.HouseImagesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class AddHouseBottomSheet : BottomSheetDialogFragment() {

    private lateinit var houseImage: CardView

    private lateinit var vacantToggle: RelativeLayout
    private lateinit var occupiedToggle: RelativeLayout
    private lateinit var bedsitter: RelativeLayout
    private lateinit var single: RelativeLayout
    private lateinit var oneBedroom: RelativeLayout
    private lateinit var twoBedroom: RelativeLayout
    private lateinit var threeBedroom: RelativeLayout
    private lateinit var mansion: RelativeLayout
    private lateinit var other: RelativeLayout
    private lateinit var houses_recycler_view: RecyclerView

    private lateinit var encodedImage: String

    private lateinit var adapter: HouseImagesAdapter
    private val encodedImagesList: ArrayList<String> = ArrayList()

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

        }

        occupiedToggle.setOnClickListener { toggle ->

            toggle.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners_active)
            vacantToggle.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners)
        }

        //  Category buttons
        single.setOnClickListener {
            it.background =
                ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_edit_text_accent)

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

        houseImage = view.findViewById(R.id.add_house_image)
        houses_recycler_view = view.findViewById(R.id.imagesRecyclerView)

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

    private fun setupRecyclerView(encodedImagesList: ArrayList<String>) {

        adapter = HouseImagesAdapter(encodedImagesList)

        houses_recycler_view.adapter = adapter
        houses_recycler_view.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    //  Function to encode our image to a string
    private fun encodeImage(bitmap: Bitmap): String {

        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width

        //  The preview bitmap
        val previewBitmap: Bitmap =
            Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()

        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
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
                        encodedImage = encodeImage(bitmap)
                        encodedImagesList.add(encodedImage)

                        setupRecyclerView(encodedImagesList)

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }

        }
}








