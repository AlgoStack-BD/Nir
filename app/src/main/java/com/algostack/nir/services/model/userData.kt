package com.algostack.nir.services.model

data class userData (

    val email: String,
    val image: String,
    val isAdmin: Boolean,
    val isVerified: Boolean,
    val location: String,
    val name: String,
    val password: String,
    val phone: String,
    val rentSuccess: Int,
    val totoalPost: Int,
    val isBanned: Boolean

)