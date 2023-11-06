package com.example.padicare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.padicare.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        val username = user?.email

        binding.textViewUsername.text = username

        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        binding.fab.setOnClickListener{
            Toast.makeText(this, "I'm working dude!", Toast.LENGTH_SHORT).show()
        }
    }
}