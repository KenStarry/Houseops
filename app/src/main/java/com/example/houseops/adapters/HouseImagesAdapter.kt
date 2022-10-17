package com.example.houseops.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.R
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class HouseImagesAdapter(

    private val houseImagesUriList: ArrayList<Uri>

) : RecyclerView.Adapter<HouseImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.houses_images_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentHouse = houseImagesUriList[position]

        Picasso.get()
            .load(currentHouse)
            .fit()
            .centerCrop()
            .into(holder.houseImage)
    }

    override fun getItemCount(): Int {
        return houseImagesUriList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val houseImage: RoundedImageView = itemView.findViewById(R.id.recycler_house_image)
    }

    fun addImage(imageUri: Uri) {
        houseImagesUriList.add(imageUri)
        notifyDataSetChanged()
    }
}