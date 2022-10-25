package com.example.houseops.collections

class UsersCollection(

    var username: String?,
    var tenantImageUrl: String?,
    var phone: String?,
    var email: String?,
    var password: String?

) {

    constructor() : this("", "", "", "", "")
}