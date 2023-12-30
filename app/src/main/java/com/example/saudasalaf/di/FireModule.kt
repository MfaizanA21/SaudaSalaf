package com.example.saudasalaf.di

import android.content.Context
import androidx.room.Room
import com.example.saudasalaf.fireRepository.AuthRepository
import com.example.saudasalaf.fireRepository.AuthRepositoryImpl
import com.example.saudasalaf.fireRepository.FirestoreCartRepository
import com.example.saudasalaf.fireRepository.FirestoreCartRepositoryImpl
import com.example.saudasalaf.fireRepository.FirestoreRepository
import com.example.saudasalaf.fireRepository.FirestoreRepositoryImpl
import com.example.saudasalaf.roomRepository.CartDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class FireModule {

    @Singleton
    @Provides
    fun providesFireAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFireAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository = authRepository

    @Singleton
    @Provides
    fun providesFirestore():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesFirestoreReposidtory(firestoreRepository: FirestoreRepositoryImpl): FirestoreRepository = firestoreRepository

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(app, CartDatabase::class.java,"SaudaSalafDB").build()

    @Singleton
    @Provides
    fun providesDao(db: CartDatabase) = db.saudaSalafDao()

    @Singleton
    @Provides
    fun providesCartRepository(fireCart: FirestoreCartRepositoryImpl): FirestoreCartRepository = fireCart

}