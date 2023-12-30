package com.example.saudasalaf.fireRepository

import android.net.Uri
import com.example.saudasalaf.model.CartItems
import com.example.saudasalaf.model.Order
import com.example.saudasalaf.utils.NetworkResult
import com.google.firebase.auth.FirebaseUser

interface FirestoreCartRepository {

    val currentUser: FirebaseUser?

    suspend fun addToCart(cart: CartItems): NetworkResult<Any>
    suspend fun showCart(): List<CartItems>
    suspend fun removeFromCart(cartId: String): NetworkResult<Any>
    suspend fun placeOrder(order: Order): NetworkResult<Any>
    suspend fun getOrders(): NetworkResult<Any>
}