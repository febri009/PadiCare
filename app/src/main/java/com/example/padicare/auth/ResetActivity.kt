package com.example.padicare.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.padicare.R
import com.example.padicare.databinding.ActivityResetBinding
import com.google.firebase.auth.FirebaseAuth

class ResetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()){
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.error = "Email tidak valid"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reset_password, null)
                    val builder = AlertDialog.Builder(this).setView(dialogView)
                    val alertDialog = builder.create()
                    
                    alertDialog.show()
                    
                    alertDialog.setOnDismissListener {
                        Intent(this@ResetActivity, LoginActivity::class.java).also{ it ->
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    }

//                    Toast.makeText(this, "Cek email untuk reset password", Toast.LENGTH_SHORT).show()
//                    Intent(this@ResetActivity, LoginActivity::class.java).also{
//                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(it)
//                    }
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}