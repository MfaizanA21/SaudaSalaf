package com.example.saudasalaf.fireRepository

import android.net.Uri
import android.util.Log
import com.example.saudasalaf.model.CartItems
import com.example.saudasalaf.model.Order
import com.example.saudasalaf.utils.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreCartRepositoryImpl @Inject constructor(private val fireCart : FirebaseFirestore): FirestoreCartRepository {
    override val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

     val result = fireCart



    private val storage = FirebaseStorage.getInstance().reference
    override suspend fun addToCart(cart: CartItems): NetworkResult<Any> = withContext(Dispatchers.IO) {
        return@withContext try {
            NetworkResult.Loading<Any>()

            val uId  = currentUser?.email!!
            val cartRef = fireCart.collection("Cart").document(cart.cartId)
//            val imageRef = storage.child("Cart_image/${cartRef.id}.jpg")
//
//            imageRef.putFile(imageUri).await()
//            val imageUrl = imageRef.downloadUrl.await().toString()
//
//            cartRef.set(cart.copy(image = imageUrl)).await()
            cartRef.set(cart.copy(userId = uId)).await()

            NetworkResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("$e")
        }

    }

    override suspend fun showCart(): List<CartItems> = withContext(Dispatchers.IO){
        val items = mutableListOf<CartItems>()
        try {
            fireCart.collection("Cart").get()
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        val result = it.result
                        for(document in result.documents) {
                            Log.d("dataa",document.data.toString())
                            val carts = document.toObject(CartItems::class.java)
                            if (carts != null && carts.userId == currentUser?.email) {
                                items.add(carts)
                            }
                        }
                    }
                }.addOnFailureListener {
                    Log.d("dataa","data Won't come!")
                }
                .await()

        } catch (e: Exception) {
            e.printStackTrace()

        }
        return@withContext items
    }

    override suspend fun removeFromCart(cartId: String): NetworkResult<Any> = withContext(Dispatchers.IO){
        return@withContext try {
            fireCart.collection("Cart").document(cartId).delete()
                .addOnSuccessListener {
                    Log.d("Deleting","Successful")
                }.await()
            NetworkResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("$e")
        }

    }

    override suspend fun placeOrder(order: Order): NetworkResult<Any> = withContext(Dispatchers.IO){
        return@withContext try {
            val uId = currentUser?.email!!

            fireCart.collection("Orders").document().set(order.copy(userId = uId))
                .addOnSuccessListener {
                    Log.d("doned", "order placed")
                }.await()
            NetworkResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("notdoned", "$e")
            NetworkResult.Error("$e")

        }
    }

    override suspend fun getOrders(): NetworkResult<Any> {
        TODO("Not yet implemented")
    }
}