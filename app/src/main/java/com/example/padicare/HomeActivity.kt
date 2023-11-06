package com.example.padicare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.padicare.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get firestore reference
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
                        binding.textViewUsername.text = name
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("FirestoreError", "Error getting user document: $e")
                }
        }

        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        binding.fab.setOnClickListener{
            Toast.makeText(this, "I'm working dude!", Toast.LENGTH_SHORT).show()
        }
    }
}