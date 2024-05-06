package com.example.bookndineapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookndineapplication.DataClasses.AddRestaurantData
import com.example.bookndineapplication.DataClasses.AddUserData
import com.example.bookndineapplication.databinding.ActivityCustomerSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CustomerSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var userName:String? = null
    private var email:String? = null
    private var password:String? = null
    private var mobileNumber:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")

        binding.createAccountBTN.setOnClickListener {
            userName = binding.userNameET.text.toString()
            email = binding.customerEmailET.text.toString()
            password = binding.customerPasswordET.text.toString()
            mobileNumber = binding.customerMobileNumberET.text.toString()
            if (userName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || mobileNumber.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                signUp(email ?: "",password ?: "",userName ?: "",mobileNumber ?: "")
            }
        }
    }

    fun signUp(email: String, password: String, userName: String, mobileNumber: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val intent = Intent(this, CustomerLoginActivity::class.java)
                var userId = it.user?.uid
                addUserDetails(userName,email,password,userId ?: "",mobileNumber)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Authentication Failed$it",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addUserDetails(
        userName: String,
        email: String,
        password: String,
        userId: String,
        mobileNumber: String
    ) {
        val userData = AddUserData(
            userName,
            email,
            password,
            mobileNumber,
            userId
        )

        databaseReference.child(userId).setValue(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "User SignedUp Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
            }
    }
}