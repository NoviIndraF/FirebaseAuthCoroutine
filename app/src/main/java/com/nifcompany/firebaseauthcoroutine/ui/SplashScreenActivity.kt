package com.nifcompany.firebaseauthcoroutine.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.nifcompany.firebaseauthcoroutine.MainActivity
import com.nifcompany.firebaseauthcoroutine.R
import com.nifcompany.firebaseauthcoroutine.databinding.ActivitySplashScreenBinding
import com.nifcompany.firebaseauthcoroutine.repository.FirebaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private val TAG = "SplashScreen"
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding

    private val firebaseViewModel: FirebaseViewModel by inject()
    private var currentFirebaseUser: FirebaseUser? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        coroutineScope.launch {
            delay(3_000)
            currentFirebaseUser = firebaseViewModel.checkUserLoggedIn()
            if (currentFirebaseUser == null){
                startActivity(RegisterActivity())
            }
            else{
                currentFirebaseUser?.let { firebaseUser ->
                    Log.i(TAG, firebaseUser.uid)
                    startActivity(MainActivity())
                }
            }
        }

    }

    private fun startActivity(activity: Activity){
        val intent = Intent(this, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}