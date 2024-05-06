package com.example.bookndineapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookndineapplication.Adapters.RestaurantListAdapter
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.R
import com.example.bookndineapplication.RestaurantDetailsActivity
import com.example.bookndineapplication.databinding.FragmentCustomerHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CustomerHomeFragment : Fragment() {

    private lateinit var binding: FragmentCustomerHomeBinding
    private lateinit var adapter: RestaurantListAdapter
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var restaurantData = ArrayList<AddRestaurantData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerHomeBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Hotel Admins")

        fetchRestaurantData()
        setRestaurantListAdapter()

        return binding.root
    }

    private fun fetchRestaurantData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                restaurantData.clear() // Clear previous data before fetching new data

                for (snapshot in dataSnapshot.children) {
                    val restaurant = snapshot.getValue(AddRestaurantData::class.java)
                    restaurant?.let {
                        restaurantData.add(it)
                    }
                }

                adapter.notifyDataSetChanged() // Notify adapter after updating data
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Log.e("Error", "Error fetching restaurant data: ${databaseError.message}")
            }
        })
    }

    private fun setRestaurantListAdapter() {
        binding.restaurantListRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter = RestaurantListAdapter(restaurantData)
        binding.restaurantListRV.adapter = adapter
        adapter.setOnClickListeners(object : RestaurantListAdapter.OnItemClickListeners{
            override fun onClickDetails(restaurant: AddRestaurantData, position: Int) {
                val intent = Intent(requireContext(),RestaurantDetailsActivity::class.java)
                intent.putExtra("ADMIN_ID",restaurant.adminId)
                startActivity(intent)
            }
        })
    }
}