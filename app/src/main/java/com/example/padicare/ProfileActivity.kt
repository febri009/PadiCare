package com.example.padicare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.padicare.auth.LoginActivity
import com.example.padicare.databinding.ActivityHomeBinding
import com.example.padicare.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

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
                        binding.tvName.text = name
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("FirestoreError", "Error getting user document: $e")
                }
        }

        binding.buttonInformasiAkun.setOnClickListener {
            val intent = Intent(this@ProfileActivity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.buttonTentangApp.setOnClickListener {
            val intent = Intent(this@ProfileActivity, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogout.setOnClickListener {
            auth.signOut()
            Intent(this@ProfileActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}