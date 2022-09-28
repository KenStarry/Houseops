package com.example.houseops.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.houseops.R
import com.example.houseops.models.AdminCategoriesModel
import com.makeramen.roundedimageview.RoundedImageView

class AdminCategoriesAdapter(
    private val context: Context,
    private val categoriesList: List<AdminCategoriesModel>

) : RecyclerView.Adapter<AdminCategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.admin_category_item,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = categoriesList[position]

        //  Set items in the adapter
        holder.title.text = model.title
        holder.image.setImageResource(model.image)

    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.admin_category_title)
        val image: RoundedImageView = itemView.findViewById(R.id.admin_category_image)
    }
}