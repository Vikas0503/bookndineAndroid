package com.example.bookndineapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.bookndineapplication.DataClasses.AddBookingData
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.databinding.ActivityBookingDetailsBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingDetailsBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var bookingData: AddBookingData

    val intent = Intent()
    private var restaurantName: String? = null
    private var restaurantImage: String? = null
    private var adminId: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private var address: String? = null
    private var mobileNumber: String? = null
    private var noOfPersons: String? = null
    private var date: String? = null
    private var time: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("RESTAURANT_IMAGE")) {
            restaurantImage = intent.getStringExtra("RESTAURANT_IMAGE")
        }

        if (intent.hasExtra("RESTAURANT_NAME")) {
            restaurantName = intent.getStringExtra("RESTAURANT_NAME")
        }

        if (intent.hasExtra("ADMIN_ID")) {
            adminId = intent.getStringExtra("ADMIN_ID")
        }

        Picasso.get().load(restaurantImage).into(binding.restaurantIV)
        binding.restaurantNameTV.text = restaurantName

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Bookings")

        ArrayAdapter.createFromResource(
            this,
            R.array.time_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.timeSpinner.adapter = adapter
        }

        userId = SharedPreferences(this).getCustomerUserId()
        userName = binding.userNameET.text.toString()
        address = binding.address.text.toString()
        mobileNumber = binding.phone.text.toString()
        noOfPersons = binding.personET.text.toString()
        date = binding.dateTV.text.toString()

        binding.timeSpinner.setOnItemClickListener { parent, _, position, _ ->
            time = parent.getItemAtPosition(position).toString()
        }

        bookingData = AddBookingData(
            restaurantImage,
            restaurantName,
            userName,
            address,
            mobileNumber,
            noOfPersons,
            date,
            time,
            userId,
            adminId,
            "Pending"
        )

        binding.bookNowBTN.setOnClickListener {
            saveBookingData(bookingData)
        }
    }

    private fun saveBookingData(bookingData: AddBookingData) {
        val bookingId = databaseReference.push().key

        databaseReference.child(bookingId ?: "").setValue(bookingData)
            .addOnSuccessListener {
                // Booking data saved successfully
                Log.d(TAG, "Booking data saved successfully")
                Toast.makeText(this, "Booking Request Sent successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity2::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Failed to save booking data
                Log.e(TAG, "Failed to save booking data: ${e.message}")
            }
    }
}