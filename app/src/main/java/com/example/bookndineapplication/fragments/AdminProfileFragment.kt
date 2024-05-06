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
import com.example.bookndineapplication.databinding.FragmentAdminProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class AdminProfileFragment : Fragment() {

    private lateinit var binding: FragmentAdminProfileBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var userId: String? = null
    private var isEditMode: Boolean = false

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri = data?.data
                uploadProfileImage(imageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminProfileBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Hotel Admins")
        storageReference = FirebaseStorage.getInstance().reference.child("hotel_images")

        userId = SharedPreferences(requireContext()).getAdminUserId()

        binding.editSaveButton.text = "Edit"
        // Disable editing of text fields
        binding.textViewEmail.isEnabled = false
        binding.textViewPassword.isEnabled = false
        binding.textViewPhone.isEnabled = false
        binding.textViewTables.isEnabled = false

        if (userId != null) {
            fetchUserData(userId ?: "")
        }

        binding.editSaveButton.setOnClickListener {
            if (isEditMode) {
                saveChanges()
            } else {
                enterEditMode()
            }
        }

        binding.profileImageView.setOnClickListener {
            if (isEditMode) {
                openGallery()
            }
        }

        return binding.root
    }

    private fun enterEditMode() {
        isEditMode = true
        binding.editSaveButton.text = "Save"
        binding.textViewEmail.isEnabled = true
        binding.textViewPassword.isEnabled = true
        binding.textViewPhone.isEnabled = true
        binding.textViewTables.isEnabled = true
    }

    private fun saveChanges() {
        isEditMode = false
        binding.editSaveButton.text = "Edit"
        // Disable editing of text fields
        binding.textViewEmail.isEnabled = false
        binding.textViewPassword.isEnabled = false
        binding.textViewPhone.isEnabled = false
        binding.textViewTables.isEnabled = false

        // Update user data in Firebase
        val userReference = userId?.let { databaseReference.child(it) }
        val newEmail = binding.textViewEmail.text.toString()
        val newPassword = binding.textViewPassword.text.toString()
        val newPhone = binding.textViewPhone.text.toString()
        val noOfTables = binding.textViewTables.text.toString()
        if (userReference != null) {
            userReference.child("email").setValue(newEmail)
            userReference.child("password").setValue(newPassword)
            userReference.child("restaurantContactNo").setValue(newPhone)
            userReference.child("noOfTables").setValue(noOfTables)
        }

        Toast.makeText(requireContext(), "Changes saved", Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun uploadProfileImage(imageUri: Uri?) {
        if (imageUri != null) {
            val profileImageRef = storageReference.child("${System.currentTimeMillis()}.jpg")
            profileImageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        updateProfileImage(imageUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateProfileImage(imageUrl: String) {
        val userReference = userId?.let { databaseReference.child(it) }
        userReference?.child("imageUrl")?.setValue(imageUrl)?.addOnSuccessListener {
            // Update profile image in UI
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.profileImageView)
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
                        binding.textViewEmail.text = userData.email?.toEditable()
                        binding.textViewPassword.text = userData.password?.toEditable()
                        binding.textViewPhone.text = userData.restaurantContactNo?.toEditable()
                        binding.textViewTables.text = userData.noOfTables?.toEditable()

                        // Load profile image using Picasso
                        if (!userData.imageUrl.isNullOrEmpty()) {
                            Picasso.get()
                                .load(userData.imageUrl)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(binding.profileImageView)
                        } else {
                            // Set default image if imageUrl is empty or null
                            binding.profileImageView.setImageResource(R.drawable.ic_launcher_foreground)
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