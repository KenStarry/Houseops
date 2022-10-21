package com.example.houseops.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.houseops.R
import com.example.houseops.models.HouseModel
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class HousesViewPager(

    private val context: Context,
    private val houses: ArrayList<HouseModel>

) : RecyclerView.Adapter<HousesViewPager.HousesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HousesViewHolder {
        return HousesViewHolder(LayoutInflater.from(context).inflate(
            R.layout.recent_houses_viewpager_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HousesViewHolder, position: Int) {

        val model = houses[position]
        holder.houseCategory.text = model.houseCategory
        holder.apartmentName.text = model.houseApartment
        holder.houseNumber.text = model.houseNo

        if (model.houseImageDownloadUriList.isNotEmpty())
            Picasso.get()
                .load(model.houseImageDownloadUriList[0])
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .fit()
                .centerCrop()
                .into(holder.houseImage)
    }

    override fun getItemCount(): Int {
        return houses.size
    }

    inner class HousesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val houseCategory: TextView = itemView.findViewById(R.id.recents_card_category)
        val houseImage: RoundedImageView = itemView.findViewById(R.id.recents_card_image)
        val apartmentName: TextView = itemView.findViewById(R.id.recents_card_apartment_name)
        val houseNumber: TextView = itemView.findViewById(R.id.recents_card_house_number)
    }
}







