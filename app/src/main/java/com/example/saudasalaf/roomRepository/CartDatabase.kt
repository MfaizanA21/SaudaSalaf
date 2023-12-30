package com.example.saudasalaf.roomRepository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartProducts::class], version = 1)
abstract class CartDatabase: RoomDatabase() {
    abstract fun saudaSalafDao(): SaudaSalafDao
}