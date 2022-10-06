package com.example.houseops.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.houseops.R
import com.example.houseops.adapters.AdminCategoriesAdapter
import com.example.houseops.databinding.ActivityCaretakerBinding
import com.example.houseops.fragments.dialogs.AddHouseBottomSheet
import com.example.houseops.models.AdminCategoriesModel

class CaretakerActivity : AppCompatActivity() {

    private lateinit var adapter: AdminCategoriesAdapter
    private lateinit var binding: ActivityCaretakerBinding

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
        binding = ActivityCaretakerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Setting our toolbar
        val toolbar: Toolbar = findViewById(R.id.caretakerToolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Caretaker"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        listeners()

    }

    private fun listeners() {

        binding.caretakerFab.setOnClickListener {

            //  Open the bottom sheet dialog
            val dialog = AddHouseBottomSheet()
            dialog.show(supportFragmentManager, "AddHouseDialog")
        }
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

    private fun toast(message: String) {
        Toast.makeText(this@CaretakerActivity, message, Toast.LENGTH_SHORT).show()
    }
}