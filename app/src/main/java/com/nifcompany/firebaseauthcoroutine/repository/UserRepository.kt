package com.nifcompany.firebaseauthcoroutine.repository

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.nifcompany.firebaseauthcoroutine.model.User
import com.nifcompany.firebaseauthcoroutine.utils.Result

interface UserRepository {
    suspend fun registerUserFromAuthWithEmailAndPassword(email: String, password: String, context: Context): Result<FirebaseUser?>
    suspend fun createUserInFirestore(user: User): Result<Void?>
    suspend fun checkLoggedIn():FirebaseUser?
    suspend fun logInUserFromAuthWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>
    suspend fun getUserFromFireStore(userId: String): Result<User>?
    suspend fun logOutUser()
}