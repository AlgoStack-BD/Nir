package com.algostack.nir.services.model

data class SingleUserResponseData(
    val _id: String,
    val createdAt: String,
    val email: String,
    val image: String,
    val isAdmin: Boolean,
    val isBanned: Boolean,
    val isVerified: Boolean,
    val location: String,
    val name: String,
    val password: String,
    val phone: String,
    val rentSuccess: Int,
    val totalPost: Int,
    val updatedAt: String
)