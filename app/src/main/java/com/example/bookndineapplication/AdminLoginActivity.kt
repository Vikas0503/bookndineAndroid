package com.example.bookndineapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookndineapplication.HelperClasses.SharedPreferences
import com.example.bookndineapplication.databinding.ActivityAdminLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.adminLoginButton.setOnClickListener {
            var adminEmail = binding.adminLoginEmail.text.toString()
            var adminPassword = binding.adminLoginPassword.text.toString()
            if (adminEmail.isEmpty() || adminPassword.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                login(adminEmail, adminPassword)
            }
        }

        binding.signUpBtn.setOnClickListener {
            val sIntent = Intent(this, AdminSignupActivity::class.java)
            startActivity(sIntent)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val adminUserId = auth.currentUser?.uid
                    SharedPreferences(this).saveAdminUserId(adminUserId ?: "")
                    val intent = Intent(this, MainActivity::class.java)
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