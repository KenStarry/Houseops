package com.example.houseops.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.houseops.R
import com.example.houseops.adapters.AdminCategoriesAdapter
import com.example.houseops.databinding.ActivityAdminBinding
import com.example.houseops.models.AdminCategoriesModel

class CaretakerActivity : AppCompatActivity() {

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

        adapter = AdminCategoriesAdapter(this@CaretakerActivity, categories)
        binding.adminRecyclerView.adapter = adapter
        binding.adminRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            GridLayoutManager(this@AdminActivity, 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.admin_toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            //  Go back to the main activity when clicked
            R.id.quitAdminMenu -> {
                val intent = Intent(this@CaretakerActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return true
    }
}