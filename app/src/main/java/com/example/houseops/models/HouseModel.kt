package com.example.houseops.models

data class HouseModel(

    val houseApartment: String?,
    val houseStatus: String?,
    val houseNo: String?,
    val houseCategory: String?,
    val houseDescription: String?,
    val houseImageUriList: ArrayList<String>?

) {

    constructor() : this("", "", "", "", "", null)
}