package com.example.padicare.edukasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.padicare.R
import com.example.padicare.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_JUDUL = "judul"
        const val EXTRA_KONTEN = "artikel"
        const val EXTRA_COVER = "link foto"
        const val EXTRA_SUMBER = "sumber"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val judul = intent.getStringExtra(EXTRA_JUDUL)
        val konten = intent.getStringExtra(EXTRA_KONTEN)
        val cover = intent.getStringExtra(EXTRA_COVER)
        val sumber = intent.getStringExtra(EXTRA_SUMBER)

        this.supportActionBar?.title= title

        binding.apply {
            tvCafeName.text = judul
            Glide.with(applicationContext)
                .load(cover)
                .placeholder(R.drawable.padicare)
                .error(R.drawable.padicare)
                .into(ivCafeCover)
            tvCafeHours.text = sumber
            tvAlamatLengkap.text = konten
        }

    }

}