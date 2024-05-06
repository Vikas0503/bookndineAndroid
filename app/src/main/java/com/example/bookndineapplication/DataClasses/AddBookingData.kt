package com.example.bookndineapplication.DataClasses

data class AddBookingData(
    val restaurantImage:String? = null,
    val restaurantName:String? = null,
    val userName:String? = null,
    val userAddress:String? = null,
    val userMobileNumber:String? = null,
    val noOfPersons:String? = null,
    val date:String? = null,
    val time:String? = null,
    val userId:String? = null,
    val adminId:String? = null,
    val status:String? = null,
)
