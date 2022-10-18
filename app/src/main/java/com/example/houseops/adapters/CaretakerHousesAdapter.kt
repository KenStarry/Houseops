package com.example.houseops.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.houseops.R
import com.example.houseops.models.HouseModel
import com.squareup.picasso.Picasso

class CaretakerHousesAdapter(

    private val context: Context,
    private val housesArrayList: ArrayList<HouseModel>

) : RecyclerView.Adapter<CaretakerHousesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.caretaker_house_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = housesArrayList[position]

        //  Setting the house number
        holder.houseNumber.text = model.houseNo

        //  Setting the image using Picasso Library
        if (model.houseImageDownloadUriList != null && model.houseImageDownloadUriList.isNotEmpty())
            Picasso.get()
                .load(model.houseImageDownloadUriList[0])
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .fit()
                .centerCrop()
                .into(holder.houseImage)
    }

    override fun getItemCount(): Int {
        return housesArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val houseImage: ImageView = itemView.findViewById(R.id.house_card_image)
        val houseNumber: TextView = itemView.findViewById(R.id.house_card_number)
    }
}