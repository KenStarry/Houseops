package com.example.houseops.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.houseops.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddHouseBottomSheet : BottomSheetDialogFragment() {

    private lateinit var vacantToggle: RelativeLayout
    private lateinit var occupiedToggle: RelativeLayout

    private lateinit var bedsitter: RelativeLayout
    private lateinit var single: RelativeLayout
    private lateinit var oneBedroom: RelativeLayout
    private lateinit var twoBedroom: RelativeLayout
    private lateinit var threeBedroom: RelativeLayout
    private lateinit var mansion: RelativeLayout
    private lateinit var other: RelativeLayout

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

        vacantToggle.setOnClickListener { toggle ->

            //  Change the background color of the vacant toggle button
            toggle.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners_active)
            occupiedToggle.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners)

        }

        occupiedToggle.setOnClickListener { toggle ->

            toggle.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners_active)
            vacantToggle.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_corners)
        }
    }

    private fun initializeVariables(view: View) {

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
}