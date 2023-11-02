package com.algostack.nir.services.model

data class UserResponse (

    val `data`: userResponseData,
    val jwt: String,
    val status: Int

)