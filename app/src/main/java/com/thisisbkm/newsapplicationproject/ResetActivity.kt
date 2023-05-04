package com.thisisbkm.newsapplicationproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.thisisbkm.newsapplicationproject.databinding.ActivityResetBinding

class ResetActivity : AppCompatActivity() {
    private lateinit var binding:ActivityResetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()

        binding.forgotPassword.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.editEmail.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }else{
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "Reset email sent", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this, "Email doesn't exist", Toast.LENGTH_SHORT).show()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
            }
        }
    }
}