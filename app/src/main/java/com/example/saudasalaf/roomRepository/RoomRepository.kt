package com.example.saudasalaf.roomRepository

import javax.inject.Inject

interface RoomRepository  {

    suspend fun addInCart(cartProducts: CartProducts)

}