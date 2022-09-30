package com.example.houseops.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.houseops.R
import com.example.houseops.adapters.AdminCategoriesAdapter
import com.example.houseops.databinding.ActivityAdminBinding
import com.example.houseops.models.AdminCategoriesModel

class AdminActivity : AppCompatActivity() {

    private lateinit var adapter: AdminCategoriesAdapter
    private lateinit var binding: ActivityAdminBinding

    //  Houses List
    private val categories = listOf(
        AdminCategoriesModel("Singles", R.drawable.house1),
        AdminCategoriesModel("Self-Contained", R.drawable.house2),
        AdminCategoriesModel("Mansions", R.drawable.house5),
        AdminCategoriesModel("Bedsitters", R.drawable.house3),
        AdminCategoriesModel("One Bedroom", R.drawable.house4),
        AdminCategoriesModel("Two Bedroom", R.drawable.house1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Setting our toolbar
        val toolbar: Toolbar = findViewById(R.id.adminToolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Admin"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
    }

    //  Setting up the recyclerview
    private fun setupRecyclerView() {

        adapter = AdminCategoriesAdapter(this@AdminActivity, categories)
        binding.adminRecyclerView.adapter = adapter
        binding.adminRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            GridLayoutManager(this@AdminActivity, 2, GridLayoutManager.VERTICAL, false)
    }

}