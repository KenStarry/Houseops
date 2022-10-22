package com.example.houseops.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.R
import com.example.houseops.models.HouseModel
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HousesViewPager(

) : ListAdapter<HouseModel, HousesViewPager.HousesViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HousesViewHolder {
        return HousesViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.recent_houses_viewpager_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HousesViewHolder, position: Int) {

        with(getItem(position)) {

            holder.apply {



            }

            holder.houseCategory.text = houseCategory
            holder.apartmentName.text = houseApartment
            holder.houseNumber.text = houseNo
            holder.housePrice.text = if (housePrice.isBlank())
                "Ksh. 0.00"
            else
                "Ksh. ${NumberFormat.getNumberInstance(Locale.US).format(housePrice.toInt())}"

            if (houseImageDownloadUriList.isNotEmpty())
                Picasso.get()
                    .load(houseImageDownloadUriList[0])
                    .placeholder(R.drawable.ic_baseline_broken_image_24)
                    .fit()
                    .centerCrop()
                    .into(holder.houseImage)

        }
    }

    inner class HousesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val houseCategory: TextView = itemView.findViewById(R.id.recents_card_category)
        val houseImage: RoundedImageView = itemView.findViewById(R.id.recents_card_image)
        val houseWishlist: ImageView = itemView.findViewById(R.id.recents_card_wishlist)
        val housePrice: TextView = itemView.findViewById(R.id.recents_card_price)
        val apartmentName: TextView = itemView.findViewById(R.id.recents_card_apartment_name)
        val houseNumber: TextView = itemView.findViewById(R.id.recents_card_house_number)
    }

}

private val diffCallback = object : DiffUtil.ItemCallback<HouseModel>() {
    override fun areItemsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {

        return oldItem.houseNo == newItem.houseNo
    }

    override fun areContentsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {

        return oldItem.houseNo == newItem.houseNo &&
                oldItem.houseApartment == newItem.houseApartment &&
                oldItem.houseStatus == newItem.houseStatus &&
                oldItem.housePrice == newItem.housePrice &&
                oldItem.houseCategory == newItem.houseCategory &&
                oldItem.houseDescription == newItem.houseDescription &&
                oldItem.houseImageDownloadUriList == newItem.houseImageDownloadUriList
    }
}







