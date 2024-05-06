package com.example.bookndineapplication.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.R
import com.example.bookndineapplication.databinding.FragmentAddMenusBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class AddMenusFragment : Fragment() {

    private lateinit var binding: FragmentAddMenusBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    var description = ""
    private var userId: String? = null
    private var imageUrl: String? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                binding.selectedMenuIV.visibility = View.VISIBLE
                binding.selectMenuTV.visibility = View.GONE
                uploadMenuImage(imageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMenusBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Hotel Admins")
        storageReference = FirebaseStorage.getInstance().reference.child("hotel_images")

        userId = SharedPreferences(requireContext()).getAdminUserId()

        description = binding.descriptionET.text.toString()

        fetchUserData(userId ?: "")

        binding.selectMenuTV.setOnClickListener {
                openGallery()
        }

        binding.selectedMenuIV.setOnClickListener {
            openGallery()
        }

        binding.uploadMenuBTN.setOnClickListener {
            if (imageUrl.isNullOrEmpty() && description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                updateProfileImage(imageUrl ?: "",description ?: "")
            }
        }
        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun uploadMenuImage(imageUri: Uri?) {
        if (imageUri != null) {
            val profileImageRef = storageReference.child("${System.currentTimeMillis()}")
            profileImageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateProfileImage(imageUrl: String, description: String) {
        val userReference = userId?.let { databaseReference.child(it) }
        userReference?.child("description")?.setValue(description)
        userReference?.child("menuUrl")?.setValue(imageUrl)?.addOnSuccessListener {
            // Update profile image in UI
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.selectedMenuIV)
            Toast.makeText(requireContext(), "Profile image updated", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Failed to update profile image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserData(userId: String) {
        val userReference = databaseReference.child(userId)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.getValue(AddRestaurantData::class.java)

                    if (userData != null) {
                        // Update UI elements with user data
                        binding.descriptionET.text = userData.description?.toEditable()

                        // Load profile image using Picasso
                        if (!userData.menuUrl.isNullOrEmpty()) {
                            binding.selectedMenuIV.visibility = View.VISIBLE
                            binding.selectMenuTV.visibility = View.GONE
                            Picasso.get()
                                .load(userData.menuUrl)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(binding.selectedMenuIV)
                        } else {
                            // Set default image if imageUrl is empty or null
                            binding.selectedMenuIV.visibility = View.GONE
                            binding.selectMenuTV.visibility = View.VISIBLE
                            binding.selectedMenuIV.setImageResource(R.drawable.ic_launcher_foreground)
                        }
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