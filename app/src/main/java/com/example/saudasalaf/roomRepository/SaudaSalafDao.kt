package com.example.saudasalaf.roomRepository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaudaSalafDao {

    @Insert
    suspend fun addInCart(cartProduct: CartProducts)

    @Query("SELECT * FROM Cart")
    fun getCart(): LiveData<List<CartProducts>>
}