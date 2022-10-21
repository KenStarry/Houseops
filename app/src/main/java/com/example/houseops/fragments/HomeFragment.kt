package com.example.houseops.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.viewpager2.widget.ViewPager2
import com.example.houseops.R
import com.example.houseops.activities.MainActivity
import com.example.houseops.adapters.HousesViewPager
import com.example.houseops.collections.CaretakerCollection
import com.example.houseops.collections.UsersCollection
import com.example.houseops.fragments.dialogs.ProfileDetailsBottomSheet
import com.example.houseops.models.HouseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var userProfile: ImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var viewpager2: ViewPager2
    private lateinit var viewpagerAdapter: HousesViewPager

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        sharedPrefs = requireContext().getSharedPreferences("user_type", Context.MODE_PRIVATE)
        val currentUser = auth.currentUser

        if (sharedPrefs.getString("type", "") == "user") {
            queryUserDetails(currentUser)
            //  Query all houses
            queryHouses(currentUser)

        } else {
            queryCaretakerDetails(currentUser)
            //  Query all houses
            queryHouses(currentUser)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPrefs = requireContext().getSharedPreferences("user_type", Context.MODE_PRIVATE)
        val currentUser = auth.currentUser

        if (sharedPrefs.getString("type", "") == "user") {
            queryUserDetails(currentUser)
            //  Query all houses
            queryHouses(currentUser)

        } else {
            queryCaretakerDetails(currentUser)
            //  Query all houses
            queryHouses(currentUser)
        }

        userProfile = view.findViewById(R.id.user_profile)
        viewpager2 = view.findViewById(R.id.main_view_pager_2)

        listeners()

        return view
    }

    private fun listeners() {

        userProfile.setOnClickListener {
            //  Open Bottom Sheet Dialog
            openProfileDetailsBottomSheet()
        }
    }

    private fun openProfileDetailsBottomSheet() {

        val dialog = ProfileDetailsBottomSheet()
        dialog.show(requireActivity().supportFragmentManager, "ProfileDetailsDialog")
    }

    //  function to query all the houses
    private fun queryHouses(currentUser: FirebaseUser?) {

        val housesArrayList: ArrayList<HouseModel> = ArrayList()

        db.collectionGroup("houses")
            .whereEqualTo("houseStatus", "vacant")
            .addSnapshotListener { querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                for (snapshot in querySnapshot!!) {
                    val house: HouseModel = snapshot.toObject()
                    housesArrayList.add(house)

                }

                Toast.makeText(
                    requireActivity(),
                    "arraylist size : ${housesArrayList.size}",
                    Toast.LENGTH_SHORT
                ).show()
                buildViewPager(housesArrayList)
            }

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
            }
    }

    private fun queryUserDetails(currentUser: FirebaseUser?) {

        db.collection("users")
            .whereEqualTo("emailAddress", currentUser!!.email)
            .addSnapshotListener { querySnapshot, error ->

                if (error != null)
                    return@addSnapshotListener

                var email = ""
                var phone = ""
                var username = ""

                for (snapshot in querySnapshot!!) {

                    val user: UsersCollection = snapshot.toObject()

                    email = user.email!!
                    phone = user.phone!!
                    username = user.username!!
                }
            }
    }

    //  Build the viewpager 2
    private fun buildViewPager(housesArrayList: ArrayList<HouseModel>) {

        viewpagerAdapter = HousesViewPager(housesArrayList)

        viewpager2.apply {

            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 1
            getChildAt(0).overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
            adapter = viewpagerAdapter

        }

        //  Setting the view of the viewpager
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.page_margin)
        val offsetMargin = resources.getDimensionPixelOffset(R.dimen.pager_offset)

        viewpager2.setPageTransformer { page, position ->

            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetMargin + pageMargin)

            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {

                page.translationX =
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL)
                        -offset
                    else
                        offset
            } else
                page.translationY = offset

            //  viewpager animation
            page.apply {
                translationY = abs(position) * 100f
                scaleX = 1f
                scaleY = 1f
            }
        }

    }
}










