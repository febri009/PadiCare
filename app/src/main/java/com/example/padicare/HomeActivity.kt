package com.example.padicare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.padicare.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

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

        //Carousel
        val carousel: ImageCarousel = findViewById(R.id.carousel)
        carousel.registerLifecycle(lifecycle)

        val list = mutableListOf<CarouselItem>()
        list.add(CarouselItem(imageDrawable = R.drawable.awal))
        list.add(CarouselItem(imageDrawable = R.drawable.kedua))
        list.add(CarouselItem(imageDrawable = R.drawable.ketiga))
        list.add(CarouselItem(imageDrawable = R.drawable.keempat))

        carousel.setData(list)

        //Bottom navigation
        binding.bottomNavigationView.background = null

        binding.fab.setOnClickListener{
            val intent = Intent(this@HomeActivity, DetectionActivity::class.java)
            startActivity(intent)
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.miProfile -> {
                    val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}