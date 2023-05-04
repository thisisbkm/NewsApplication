package com.thisisbkm.newsapplicationproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.thisisbkm.newsapplicationproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.reset.setOnClickListener {
            startActivity(Intent(this, ResetActivity::class.java))
        }
        binding.login.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.textEmail.text.toString()
            val password = binding.textPass.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(this) { task ->
                        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }

            }
        }
    }

    override fun onResume() {
        if(auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onResume()
    }
}