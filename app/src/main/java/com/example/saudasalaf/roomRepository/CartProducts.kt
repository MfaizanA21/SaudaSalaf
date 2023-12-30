package com.example.saudasalaf.roomRepository

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Cart")
data class CartProducts(
    @PrimaryKey
    val ProductId: String,
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val image: String
)
