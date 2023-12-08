package com.example.padicare.util

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    val myPref = "myPref"
    val myToken = "userToken"
    val myPreferences: SharedPreferences

    init {
        myPreferences = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    }

    // Fungsi untuk mendapatkan data token
    fun getUserToken(): String? {
        return myPreferences.getString(myToken, " ")
    }
}