package com.example.bookndineapplication.DataClasses

data class AddRestaurantData(
    val restaurantName:String? = null,
    val restaurantContactNo:String? = null,
    val noOfTables:String? = null,
    val restaurantAddress:String? = null,
    val restaurantTime:String? = null,
    val email:String? = null,
    val password:String? = null,
    val imageUrl:String? = null,
    val menuUrl:String? = null,
    val description:String? = null,
    val adminId:String? = null
)
