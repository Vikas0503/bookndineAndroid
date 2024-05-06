package com.example.bookndineapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.databinding.ActivityRestaurantDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class RestaurantDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailsBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    val intent = Intent()
    private var adminId:String? = null
    private var restaurantName:String? = null
    private var restaurantImage:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Hotel Admins")

        if (intent.hasExtra("ADMIN_ID")) {
            adminId = intent.getStringExtra("ADMIN_ID")
        }

        fetchRestaurantDetails(adminId ?: "")

        binding.restaurantBookBtn.setOnClickListener {
            val intent = Intent(this,BookingDetailsActivity::class.java)
            intent.putExtra("RESTAURANT_IMAGE",restaurantImage)
            intent.putExtra("RESTAURANT_NAME",restaurantName)
            intent.putExtra("ADMIN_ID",adminId)
            startActivity(intent)
        }
    }

    private fun fetchRestaurantDetails(adminId: String) {
        val userRef = databaseReference.child(adminId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (restaurantSnapshot in dataSnapshot.children) {
                    val restaurant = restaurantSnapshot.getValue(AddRestaurantData::class.java)
                    if (restaurant != null) {
                        restaurantName = restaurant.restaurantName.toString()
                        restaurantImage = restaurant.imageUrl
                        binding.restaurantNameTv.text = restaurant.restaurantName
                        binding.restaurantAddressTv.text = restaurant.restaurantAddress
                        binding.restaurantContactNoTv.text = restaurant.restaurantContactNo
                        binding.restaurantTimeTv.text = restaurant.restaurantTime
                        binding.restaurantAboutContentTv.text = restaurant.description
                        Picasso.get().load(restaurant.menuUrl).into(binding.restaurantMenuImg)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "Error fetching restaurant data: ${databaseError.message}")
            }
        })
    }
}