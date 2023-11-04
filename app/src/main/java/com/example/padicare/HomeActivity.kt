package com.example.padicare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        bottomNavigationView.background = null

        bottomNavigationView.menu.getItem(2).isEnabled = false

        fab.setOnClickListener{
            Toast.makeText(this, "I'm working dude!", Toast.LENGTH_SHORT).show()
        }
    }
}