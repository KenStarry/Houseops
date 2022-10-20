package com.example.houseops.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.houseops.R
import com.example.houseops.Utilities
import com.example.houseops.models.HouseModel
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CaretakerHousesAdapter(

    private val context: Context,
    private val housesArrayList: ArrayList<HouseModel>

) : RecyclerView.Adapter<CaretakerHousesAdapter.ViewHolder>() {

    private lateinit var utils: Utilities

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

        utils = Utilities(context)

        val model = housesArrayList[position]

        //  Setting the house number
        holder.houseNumber.text = model.houseNo

        //  Setting house price
        holder.housePrice.text = if (model.housePrice.isBlank())
            "Ksh. 0.00"
        else
            "Ksh. ${NumberFormat.getNumberInstance(Locale.US).format(model.housePrice.toInt())}"

        //  one bedroom, two bedroom, three bedroom e.t.c
        holder.houseCategory.text = model.houseCategory

        //  Updating the number of images for that house
        holder.houseImageCount.text = if (model.houseImageDownloadUriList.size - 1 >= 0) {
            "+${model.houseImageDownloadUriList.size - 1}"
        } else {
            "+0"
        }

        //  Check the current status of the house
        if (model.houseStatus == "occupied") {
            utils.showViewAHideViewB(holder.houseOccupied, holder.houseVacant)
        }
        else {
            utils.showViewAHideViewB(holder.houseVacant, holder.houseOccupied)
        }

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
        val housePrice: TextView = itemView.findViewById(R.id.house_card_price)
        val houseCategory: TextView = itemView.findViewById(R.id.house_card_category)
        val houseImageCount: TextView = itemView.findViewById(R.id.house_card_image_count)
        val houseVacant: LinearLayout = itemView.findViewById(R.id.house_card_vacant_holder)
        val houseOccupied: LinearLayout = itemView.findViewById(R.id.house_card_occupied_holder)
    }
}