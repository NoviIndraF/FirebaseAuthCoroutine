package com.nifcompany.firebaseauthcoroutine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.nifcompany.firebaseauthcoroutine.databinding.ActivityMainBinding
import com.nifcompany.firebaseauthcoroutine.module.firebaseViewModule
import com.nifcompany.firebaseauthcoroutine.repository.FirebaseViewModel
import com.nifcompany.firebaseauthcoroutine.ui.LoginActivity
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val firebaseViewModel :FirebaseViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseViewModel.currentUserLD.observe(this, Observer { currentUser ->
            binding.tvMainLoggedInUser.text =currentUser.name
        })

        binding.btnMainactivityLogOut.setOnClickListener {
            logOut()
        }
    }

    private fun logOut(){
        firebaseViewModel.logOutUser()
        startLoginActivity()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TASK or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}