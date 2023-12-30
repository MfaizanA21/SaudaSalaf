package com.example.saudasalaf.roomRepository

import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(private val ssDao: SaudaSalafDao): RoomRepository {
    override suspend fun addInCart(cartProducts: CartProducts) {

    }
}