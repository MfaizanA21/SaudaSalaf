package com.example.saudasalaf.fireRepository

import android.net.Uri
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.utils.NetworkResult

interface FirestoreRepository {

    val result: Any?

    //suspend fun withImage(product: Product, imageUri: Uri): NetworkResult<Any>


    suspend fun uploadProduct(product: Product, imageUri: Uri): NetworkResult<Any>
    suspend fun deleteProduct(productId: String): NetworkResult<Any>
    suspend fun updateProduct(id: String, title: String, description: String, price: Int): NetworkResult<Any>
    suspend fun getProduct(): List<Product>
}