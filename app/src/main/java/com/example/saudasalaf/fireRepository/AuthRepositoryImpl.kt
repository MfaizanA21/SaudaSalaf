package com.example.saudasalaf.fireRepository

import com.example.saudasalaf.model.User
import com.example.saudasalaf.utils.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

     override suspend fun login(user: User): NetworkResult<FirebaseUser> = withContext(Dispatchers.IO){
         return@withContext try {
             firebaseAuth.signInWithEmailAndPassword(user.email, user.password).await()?.user?.let {
                 NetworkResult.Success(it)
             }
         } catch (e: Exception) {
             e.printStackTrace()
             NetworkResult.Error("$e")
         }
     }!!

    override suspend fun signup(user: User): NetworkResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(user.username).build())?.await()
            NetworkResult.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("$e")
        }

    }

    override suspend fun adminLogin(user: User): Boolean {

        return user.email == "adminFaisal92@gmail.com" && user.password == "Faisa1Chandio"

    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}