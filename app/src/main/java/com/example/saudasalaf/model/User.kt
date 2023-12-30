package com.example.saudasalaf.model

data class User (
    var username: String?,
    val email: String,
    val password: String,
    val isCustomer: Boolean? = true
    )