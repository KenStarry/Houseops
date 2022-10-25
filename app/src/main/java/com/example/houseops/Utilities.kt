package com.example.houseops

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.BoolRes
import androidx.annotation.RequiresApi

class Utilities(

    private val context: Context
) {

    //  Check if device is connected to a network
    @RequiresApi(Build.VERSION_CODES.M)
    fun hasNetworkAvailable(context: Context): Boolean {

        val service = Context.CONNECTIVITY_SERVICE
        val manager = context.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetwork

        return (network != null)
    }

    //  Function to toast a message
    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    //  Function to hide signinbutton
    fun showViewAHideViewB(viewA: View, viewB: View) {
        viewA.visibility = View.VISIBLE
        viewB.visibility = View.GONE
    }

    //  Method to return file extensions
    fun getFileExtension(uri: Uri): String {

        val cr = context.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri))!!
    }
}