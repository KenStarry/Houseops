package com.example.houseops.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.R
import com.makeramen.roundedimageview.RoundedImageView

class HouseImagesAdapter(

    private val encodedHouseImagesList: MutableList<String>

) : RecyclerView.Adapter<HouseImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.houses_images_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.houseImage.setImageBitmap(getUserImageBitmap(encodedHouseImagesList[position]))
    }

    override fun getItemCount(): Int {
        return encodedHouseImagesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val houseImage: RoundedImageView = itemView.findViewById(R.id.recycler_house_image)
    }

    private fun getUserImageBitmap(encodedImage: String): Bitmap {

        //  An array of bytes containing the image resource
        val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun addImage(image: String) {
        encodedHouseImagesList.add(image)
        notifyDataSetChanged()
    }
}