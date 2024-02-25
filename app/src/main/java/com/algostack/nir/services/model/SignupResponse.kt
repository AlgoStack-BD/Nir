package com.algostack.nir.services.model

data class SignupResponse(
    val `data`: DataXXXX,
    val jwt: String,
    val responseData: ResponseData,
    val status: Int
)