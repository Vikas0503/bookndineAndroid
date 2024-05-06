package com.example.bookndineapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bookndineapplication.databinding.ActivityAdminLoginBinding
import com.example.bookndineapplication.databinding.ActivityMainBinding
import com.example.bookndineapplication.fragments.AddMenusFragment
import com.example.bookndineapplication.fragments.AdminProfileFragment
import com.example.bookndineapplication.fragments.Ordersfragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adminBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.orders -> {
                    val ordersfragment = Ordersfragment()
                    replaceFragment(ordersfragment)
                }
                R.id.menu -> {
                    // Pass UserData object to PassViewFragment
                    val addMenusFragment = AddMenusFragment()
                    replaceFragment(addMenusFragment)
                }
                R.id.profile -> {
                    val adminProfileFragment = AdminProfileFragment()
                    replaceFragment(adminProfileFragment)
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
        fragmentTransaction.replace(R.id.admin_framelayout, fragment)
        fragmentTransaction.commit()
    }
}