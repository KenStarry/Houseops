package com.example.houseops.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.houseops.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddHouseBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.caretaker_new_house_bottomsheet, container, false)

        //  Set the theme/style of the bottom sheet
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        return view
    }
}