package com.example.houseops.collections

import android.net.Uri

data class CaretakerCollection(

    var apartment: String,
    var idNo: String,
    var username: String,
    var emailAddress: String,
    var phone: String,
    var pass: String,
    var isVerfied: Boolean,
    val caretakerImageUrl: String

) {

    constructor() : this(
        "", "", "", "", "", "", false, ""
    )
}