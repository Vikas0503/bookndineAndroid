package com.example.bookndineapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookndineapplication.Adapters.OrdersListAdapter
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.R
import com.example.bookndineapplication.databinding.FragmentOrdersfragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class Ordersfragment : Fragment() {

    private lateinit var binding:FragmentOrdersfragmentBinding
    private lateinit var adapter:OrdersListAdapter
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var orderReference: DatabaseReference

    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersfragmentBinding.inflate(layoutInflater)

        userId = SharedPreferences(requireContext()).getAdminUserId()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Hotel Admins")
        orderReference = firebaseDatabase.reference.child("Bookings")

        setOrderListAdapter()
        fetchUserData(userId ?: "")

        return binding.root
    }

    private fun setOrderListAdapter() {
        binding.orderListRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter = OrdersListAdapter()
        binding.orderListRV.adapter = adapter
    }

    private fun fetchUserData(userId:String) {
        val userReference = databaseReference.child(userId)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.getValue(AddRestaurantData::class.java)

                    if (userData != null) {
                            binding.restaurantNameTV.text = userData.restaurantName
                            binding.restaurantLocationTV.text = userData.restaurantAddress
//                            binding.numberOfTablesBookedTV.text = userData.restaurantAddress
                            Picasso.get()
                                .load(userData.imageUrl)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(binding.restaurantIV)
                    }
                } else {
                    // User data not found
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(requireContext(), "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
