package com.example.bookndineapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.databinding.ActivityCustomerLoginBinding
import com.google.firebase.auth.FirebaseAuth

class CustomerLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.customerLoginButton.setOnClickListener {
            var userEmail = binding.customerLoginEmail.text.toString()
            var userPassword = binding.customerLoginPassword.text.toString()
            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                login(userEmail, userPassword)
            }
        }

        binding.customerSignup.setOnClickListener {
            val sIntent = Intent(this, CustomerSignupActivity::class.java)
            startActivity(sIntent)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val customerUserId = auth.currentUser?.uid
                    SharedPreferences(this).saveCustomerUserId(customerUserId ?: "")
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this, "Login Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}