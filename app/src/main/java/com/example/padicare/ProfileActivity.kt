package com.example.padicare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.padicare.databinding.ActivityHomeBinding
import com.example.padicare.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonInformasiAkun.setOnClickListener {
            val intent = Intent(this@ProfileActivity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.buttonTentangApp.setOnClickListener {
            val intent = Intent(this@ProfileActivity, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}