package com.thisisbkm.newsapplicationproject

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.thisisbkm.newsapplicationproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()
        binding.signin.setOnClickListener {
            finish()
        }
        binding.register.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.textEmail.text.toString()
            val password = binding.textPass.text.toString()
            val confirm = binding.textPassConfirm.text.toString()
            if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }

            else if(password != confirm){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
            else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener (this){
                        Toast.makeText(this, "Registration Successful ....", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

}