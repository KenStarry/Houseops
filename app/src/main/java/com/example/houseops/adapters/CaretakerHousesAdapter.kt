package com.example.houseops.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.houseops.R
import com.example.houseops.models.HouseModel

class CaretakerHousesAdapter(

    private val housesArrayList: ArrayList<HouseModel>

) : RecyclerView.Adapter<CaretakerHousesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.caretaker_house_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = housesArrayList[position]

//        holder.houseImage.setImageBitmap(getUserImageBitmap(model.houseImageBitmap!!))
        holder.houseNumber.text = model.houseNo
    }

    override fun getItemCount(): Int {
        return housesArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val houseImage: ImageView = itemView.findViewById(R.id.house_card_image)
        val houseNumber: TextView = itemView.findViewById(R.id.house_card_number)
    }

    private fun getUserImageBitmap(encodedImage: String): Bitmap {

        //  An array of bytes containing the image resource
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}