package com.nifcompany.firebaseauthcoroutine.repository.implementation

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.nifcompany.firebaseauthcoroutine.model.User
import com.nifcompany.firebaseauthcoroutine.repository.UserRepository
import com.nifcompany.firebaseauthcoroutine.utils.await
import com.nifcompany.firebaseauthcoroutine.utils.Result

private val TAG = "UserRepositoryImpl"
class UserRepositoryImpl : UserRepository{
    private val USER_COLLECTION_NAME = "users"

    private val firestoreInstance =  FirebaseFirestore.getInstance()
    private val userCollection = firestoreInstance.collection(USER_COLLECTION_NAME)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun registerUserFromAuthWithEmailAndPassword(email: String, password : String, context: Context): Result<FirebaseUser?> {
        try {
            return when(val resultDocumentSnapshot = firebaseAuth.createUserWithEmailAndPassword(email,password).await()){
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultDocumentSnapshot.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Canceled -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Canceled(resultDocumentSnapshot.exception)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Error(resultDocumentSnapshot.exception)
                }
                is Result.Loading -> TODO()
            }
        }
        catch (exception: Exception){
            return Result.Error(exception)
        }
    }

    override suspend fun createUserInFirestore(user: User): Result<Void?> {
        return try {
            userCollection.document(user.id).set(user).await()
        } catch (exception : Exception){
            Result.Error(exception)
        }
    }

    override suspend fun checkLoggedIn(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}