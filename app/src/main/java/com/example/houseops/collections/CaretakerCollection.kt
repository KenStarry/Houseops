package com.example.houseops.collections

data class CaretakerCollection(

    var apartment: String,
    var idNo: String,
    var username: String,
    var emailAddress: String,
    var phone: String,
    var pass: String,
    var isVerfied: Boolean

) {

    constructor() : this(
        "", "", "", "", "", "", false
    )
}