package com.example.bookndineapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bookndineapplication.databinding.ActivityMain2Binding
import com.example.bookndineapplication.fragments.AddMenusFragment
import com.example.bookndineapplication.fragments.AdminProfileFragment
import com.example.bookndineapplication.fragments.CustomerBookedHistoryFragment
import com.example.bookndineapplication.fragments.CustomerHomeFragment
import com.example.bookndineapplication.fragments.CustomerProfileFragment
import com.example.bookndineapplication.fragments.Ordersfragment

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customerBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val homeFragment = CustomerHomeFragment()
                    replaceFragment(homeFragment)
                }
                R.id.history -> {
                    // Pass UserData object to PassViewFragment
                    val bookedHistoryFragment = CustomerBookedHistoryFragment()
                    replaceFragment(bookedHistoryFragment)
                }
                R.id.customerProfile -> {
                    val customerProfileFragment = CustomerProfileFragment()
                    replaceFragment(customerProfileFragment)
                }

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.customer_framelayout, fragment)
        fragmentTransaction.commit()
    }
}