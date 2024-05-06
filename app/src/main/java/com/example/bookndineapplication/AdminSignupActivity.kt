package com.example.bookndineapplication

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.databinding.ActivityAdminSignUpBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private var selectedImageUri: Uri? = null
    private var restaurantName:String? = null
    private var restaurantContactNo:String? = null
    private var noOfTables:String? = null
    private var restaurantTime:String? = null
    private var restaurantAddress:String? = null
    private var email:String? = null
    private var password:String? = null
    private var imageUrl:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Hotel Admins") // Change to your data structure
        storageReference = FirebaseStorage.getInstance().reference.child("hotel_images")

        binding.selectRestaurantImageBTN.setOnClickListener {
            openImageChooser()
        }

        binding.submitBTN.setOnClickListener {
            restaurantName = binding.restaurantNameET.text.toString()
            restaurantContactNo = binding.restaurantContactNoET.text.toString()
            noOfTables = binding.noOfTablesET.text.toString()
            restaurantAddress = binding.addressET.text.toString()
            restaurantTime = binding.timeET.text.toString()
            email = binding.adminEmailET.text.toString()
            password = binding.adminPasswordET.text.toString()
            if (restaurantName.isNullOrEmpty() || restaurantContactNo.isNullOrEmpty() || noOfTables.isNullOrEmpty() || restaurantAddress.isNullOrEmpty() || restaurantTime.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                if (selectedImageUri != null) {
                    uploadImageAndSaveData(restaurantName ?: "",restaurantContactNo ?: "",noOfTables ?: "",restaurantAddress ?: "", restaurantTime ?: "",email ?: "",password ?: "")
                } else {
                    Toast.makeText(this@AdminSignupActivity, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addRestaurantDetails(
        restaurantName: String,
        restaurantContactNo: String,
        noOfTables: String,
        restaurantAddress: String,
        restaurantTime: String,
        email: String,
        password: String,
        imageUrl: String,
        adminId: String?
    ) {
        val restaurantData = AddRestaurantData(
            restaurantName,
            restaurantContactNo,
            noOfTables,
            restaurantAddress,
            restaurantTime,
            email,
            password,
            imageUrl,
            adminId
        )

        databaseReference.child(adminId ?: "").setValue(restaurantData)
            .addOnSuccessListener {
                Toast.makeText(this, "Restaurant Added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageAndSaveData(restaurantName: String, restaurantContactNo: String, noOfTables: String, restaurantAddress: String, restaurantTime: String, email: String, password: String) {
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            imageUrl = uriTask.result.toString()
                            signUp(restaurantName,restaurantContactNo,noOfTables,restaurantAddress,restaurantTime,email,password)
                        } else {
                            Toast.makeText(this@AdminSignupActivity, "Image upload failed: ${uriTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@AdminSignupActivity, "Image upload failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp(restaurantName: String, restaurantContactNo: String, noOfTables: String, restaurantAddress: String, restaurantTime: String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val intent = Intent(this, AdminLoginActivity::class.java)
                var adminId = it.user?.uid
                addRestaurantDetails(restaurantName, restaurantContactNo,noOfTables,restaurantAddress,restaurantTime,email,password,imageUrl ?: "",adminId)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Authentication Failed$it",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val fileNameIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                return it.getString(fileNameIndex)
            }
        }
        return ""
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            val fileName = selectedImageUri?.let { getFileNameFromUri(it) }
            binding.selectedImageTV.text = fileName
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}