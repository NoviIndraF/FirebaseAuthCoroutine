package com.nifcompany.firebaseauthcoroutine.repository

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.nifcompany.firebaseauthcoroutine.MainActivity
import com.nifcompany.firebaseauthcoroutine.R
import com.nifcompany.firebaseauthcoroutine.model.User
import com.nifcompany.firebaseauthcoroutine.repository.implementation.UserRepositoryImpl
import com.nifcompany.firebaseauthcoroutine.ui.LoginActivity
import com.nifcompany.firebaseauthcoroutine.ui.RegisterActivity
import com.nifcompany.firebaseauthcoroutine.utils.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FirebaseViewModel(
    var userRepository: UserRepository
) :ViewModel() {
    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
    get() = _toast

    private val _spinner = MutableLiveData(false)
    val spinner: LiveData<Boolean>
    get() = _spinner

    private val _currentUserMLD = MutableLiveData(User())
    val currentUserLD: LiveData<User>
    get() = _currentUserMLD

    fun registerUSerFromAuthWithEmailAndPassword(name: String, email: String, password: String, activity: Activity){
        launchDataLoad {
            viewModelScope.launch {
                when(val result = userRepository.registerUserFromAuthWithEmailAndPassword(email, password, activity.applicationContext))
                {
                    is Result.Success -> {
                        Log.e(TAG, "Result.Success")
                        result.data?.let { firebaseUser ->
                            createUserInFirestore(createUserObject(firebaseUser,name), activity)
                        }
                    }

                    is Result.Error -> {
                        Log.e(TAG, "${result.exception.message}")
                        _toast.value = result.exception.message
                    }

                    is Result.Canceled -> {
                        Log.e(TAG, "${result.exception!!.message}")
                        _toast.value = activity.getString(R.string.request_canceled)
                    }
                }
            }
        }
    }

    private suspend fun createUserInFirestore(user: User,activity: Activity)
    {
        Log.d(TAG, "Result - ${user.name}")
        when(val result = userRepository.createUserInFirestore(user)){
            is Result.Success -> {
                Log.d(TAG, activity::class.java.simpleName)
                when(activity)
                {
                    is RegisterActivity ->{
                        _toast.value =  activity.getString(R.string.registration_successful)
                    }
                    is LoginActivity -> {
                        Log.d(TAG, "Result - ${user.name}")
                        _toast.value = activity.getString(R.string.login_successful)
                    }
                }
                Log.d(TAG, "Result.Error - ${user.name}")
                _currentUserMLD.value = user
                startMainActivity(activity)
            }

            is Result.Error -> {
                _toast.value = result.exception.message
            }

            is Result.Canceled -> {
                _toast.value = activity.getString(R.string.request_canceled)
            }
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job{
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            }
            catch (error: Throwable){
                _toast.value = error.message
            }
            finally {
                _spinner.value = false
            }
        }
    }

    fun logInUserFromAuthWithEmailAndPassword(email: String, password: String, activity: Activity){
        launchDataLoad {
            viewModelScope.launch {
                when(val result=userRepository.logInUserFromAuthWithEmailAndPassword(email, password))
                {
                    is Result.Success ->{
                        Log.i(TAG, "SignIn - Result.Success - User: ${result.data}")
                        result.data?.let { firebaseUser ->
                            getUserFromFirestore(firebaseUser.uid, activity)
                            _toast.value = activity.getString(R.string.login_successful)
                        }
                    }

                    is Result.Error ->{
                        _toast.value = result.exception.message
                    }

                    is Result.Canceled-> {
                        _toast.value = activity.getString(R.string.request_canceled)
                    }
                }
            }
        }
    }

    fun logOutUser(){
        viewModelScope.launch {
            userRepository.logOutUser()
        }
    }

    suspend fun getUserFromFirestore(userId: String, activity: Activity){
        when(val result = userRepository.getUserFromFireStore(userId))
        {
            is Result.Success<*> ->{
                val _user = result.data
                _currentUserMLD.value = _user as User?
                startMainActivity(activity = activity)
            }

            is Result.Error-> {
                _toast.value = result.exception.message
            }

            is Result.Canceled-> {
                _toast.value = activity.getString(R.string.request_canceled)
            }
        }
    }

    fun checkUserLoggedIn(): FirebaseUser?{
        var _firebaseUser: FirebaseUser? = null
        viewModelScope.launch {
            _firebaseUser = userRepository.checkLoggedIn()
        }
        return  _firebaseUser
    }

    fun onToastShown(){
        _toast.value = null
    }

    private fun startMainActivity(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }

    fun createUserObject(firebaseUser: FirebaseUser, name: String, profilePicture: String=""): User{
        val currentUser = User(
            id = firebaseUser.uid,
            name = name,
            profilePicture = profilePicture
        )

        return currentUser
    }
}