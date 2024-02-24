package com.algostack.nir.services.model

data class PaymentRequestData(
    val post_id: String,
    val session_id: String,
    val user_id: String
)