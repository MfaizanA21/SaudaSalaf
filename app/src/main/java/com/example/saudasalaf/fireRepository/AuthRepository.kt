package com.example.saudasalaf.fireRepository

import com.example.saudasalaf.model.User
import com.example.saudasalaf.utils.NetworkResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun login(user: User): NetworkResult<FirebaseUser>

    suspend fun signup(user: User): NetworkResult<FirebaseUser>

    suspend fun adminLogin(user: User): Boolean


    fun logout()

}