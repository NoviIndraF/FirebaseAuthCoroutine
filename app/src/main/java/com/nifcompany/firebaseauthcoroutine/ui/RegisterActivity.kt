package com.nifcompany.firebaseauthcoroutine.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nifcompany.firebaseauthcoroutine.databinding.ActivityRegisterBinding
import com.nifcompany.firebaseauthcoroutine.repository.FirebaseViewModel

private val TAG = "RegisterActivity"
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseViewModel = ViewModelProvider(this)
            .get(FirebaseViewModel::class.java)

        binding.btnRegisterLogin.setOnClickListener {
            if (validateName() && validateEmail() && validatePassword()){
                firebaseViewModel.registerUSerFromAuthWithEmailAndPassword(
                    binding.tietRegisterName.text.toString(),
                    binding.tietRegisterEmail.text.toString(),
                    binding.tietRegisterPassword.text.toString(),
                    this
                )
            }
        }

        firebaseViewModel.toast.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                firebaseViewModel.onToastShown()
            }
        })

        firebaseViewModel.spinner.observe(this, Observer { value ->
            value.let {show ->
                binding.spinnerRegister.visibility = if (show) View.VISIBLE else View.GONE
                Log.i(TAG,"$show")
            }
        })
    }

    private fun validateName(): Boolean{
        val name = binding.tietRegisterName.text.toString().trim()
        return if (name.length < 7){
            binding.tietRegisterName.error = "Use at least 8 characters"
            false
        } else {
            true
        }
    }

    private fun validateEmail(): Boolean{
        val email = binding.tietRegisterEmail.text.toString().trim()
        return if (!email.contains("@") && !email.contains("."))
        {
            binding.tietRegisterEmail.error = "Enter a valid email"
            false
        }
        else if (email.length < 6)
        {
            binding.tietRegisterEmail.error = "Use at least 5 characters"
            false
        }
        else {
            true
        }
    }

    private fun validatePassword(): Boolean{
        val password = binding.tietRegisterPassword.text.toString().trim()

        return if (password.length < 6){
            binding.tietRegisterPassword.error = "Use at least 5 characters"
            false
        }
        else{
            true
        }
    }
}