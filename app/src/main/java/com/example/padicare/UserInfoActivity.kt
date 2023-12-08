package com.example.padicare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ToggleButton
import com.example.padicare.databinding.ActivityHomeBinding
import com.example.padicare.databinding.ActivityUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserInfoBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()  // Add this line
    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()  // Add this line
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid


        if (userId != null){
            //Get user doc from Firestore
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()){
                        val name = documentSnapshot.getString("fName")
                        val email = documentSnapshot.getString("email")

                        binding.name.text = name
                        binding.email.text = email
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("FirestoreError", "Error getting user document: $e")
                }
        }
    }
}