package com.example.bookndineapplication.HelperClasses

import android.content.Context

class SharedPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveAdminUserId(userId: String) {
        sharedPreferences.edit().putString("adminUserId", userId).apply()
    }

    fun getAdminUserId(): String? {
        return sharedPreferences.getString("adminUserId", null)
    }

    fun clearAdminUserId() {
        sharedPreferences.edit().remove("adminUserId").apply()
    }

    fun saveCustomerUserId(userId: String) {
        sharedPreferences.edit().putString("customerUserId", userId).apply()
    }

    fun getCustomerUserId(): String? {
        return sharedPreferences.getString("customerUserId", null)
    }

    fun clearCustomerUserId() {
        sharedPreferences.edit().remove("customerUserId").apply()
    }

    fun saveEmailId(name: String) {
        sharedPreferences.edit().putString("emailId", name).apply()
    }

    fun getEmailId(): String? {
        return sharedPreferences.getString("emailId", null)
    }

    fun saveOrdersCount(count: Int) {
        sharedPreferences.edit().putInt("count", count).apply()
    }

    fun getOrdersCount(): Int {
        return sharedPreferences.getInt("count", 0)
    }
}