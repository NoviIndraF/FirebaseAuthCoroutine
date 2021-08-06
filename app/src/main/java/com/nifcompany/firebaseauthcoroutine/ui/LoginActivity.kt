package com.nifcompany.firebaseauthcoroutine.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.nifcompany.firebaseauthcoroutine.R
import com.nifcompany.firebaseauthcoroutine.databinding.ActivityLoginBinding
import com.nifcompany.firebaseauthcoroutine.repository.FirebaseViewModel
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    val firebaseViewModel: FirebaseViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            if (validateEmail(binding.tietLoginEmail) && validatePassword())
            {
                firebaseViewModel.logInUserFromAuthWithEmailAndPassword(
                    binding.tietLoginEmail.text.toString(),
                    binding.tietLoginPassword.text.toString(),
                    this
                )
            }
        }

        binding.tvLoginLoginnow.setOnClickListener {
            startRegisterActivity()
        }
    }

    private fun validateEmail(view: View): Boolean{
        val email = (view as TextInputEditText).text.toString().trim()
        return if (!email.contains("@") && !email.contains("."))
        {
            Log.d("TAG", "Email : " + email)
            view.error = "Enter a valid email"
            false
        }
        else if (email.length < 6)
        {
            view.error = "Use at least 5 characters"
            false
        }
        else {
            true
        }
    }

    private fun validatePassword(): Boolean
    {
        val password = binding.tietLoginPassword.text.toString().trim()
        return if (password.length <6){
            binding.tietLoginPassword.error = "Use at least 5 characters"
            false
        }
        else{
            true
        }
    }

    private fun startRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}