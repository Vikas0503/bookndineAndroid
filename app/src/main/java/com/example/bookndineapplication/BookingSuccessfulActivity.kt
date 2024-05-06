package com.example.bookndineapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookndineapplication.databinding.ActivityBookingSuccessfulBinding

class BookingSuccessfulActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingSuccessfulBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingSuccessfulBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}