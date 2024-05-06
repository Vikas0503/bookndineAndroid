package com.example.bookndineapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookndineapplication.databinding.ActivityChooseRegisterBinding

class ChooseRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customerTV.setOnClickListener{
            startActivity(Intent(this,CustomerLoginActivity::class.java))
        }

        binding.hotelAdminTV.setOnClickListener{
            startActivity(Intent(this,AdminLoginActivity::class.java))
        }
    }
}