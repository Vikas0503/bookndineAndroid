package com.example.bookndineapplication.fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.DataClasses.AddUserData
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.R
import com.example.bookndineapplication.databinding.FragmentCustomerProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class CustomerProfileFragment : Fragment() {

    private lateinit var binding:FragmentCustomerProfileBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerProfileBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")

        userId = SharedPreferences(requireContext()).getCustomerUserId()

        fetchUserData(userId ?: "")

        binding.editSaveButton.setOnClickListener {
            updateProfile()
        }

        return binding.root
    }

    private fun updateProfile() {
        // Update user data in Firebase
        val userReference = userId?.let { databaseReference.child(it) }
        val newEmail = binding.textViewEmail.text.toString()
        val newPassword = binding.textViewPassword.text.toString()
        val newPhone = binding.textViewPhone.text.toString()
        val userName = binding.customerUserNameET.text.toString()
        if (userReference != null) {
            userReference.child("email").setValue(newEmail)
            userReference.child("password").setValue(newPassword)
            userReference.child("mobileNumber").setValue(newPhone)
            userReference.child("userName").setValue(userName)
        }

        Toast.makeText(requireContext(), "Changes saved", Toast.LENGTH_SHORT).show()
    }


    private fun fetchUserData(userId: String) {
        val userReference = databaseReference.child(userId)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.getValue(AddUserData::class.java)

                    if (userData != null) {
                        // Update UI elements with user data
                        binding.textViewEmail.text = userData.email?.toEditable()
                        binding.textViewPassword.text = userData.password?.toEditable()
                        binding.textViewPhone.text = userData.mobileNumber?.toEditable()
                        binding.customerUserNameET.text = userData.userName?.toEditable()
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

    fun String?.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this ?: "")
    }
}