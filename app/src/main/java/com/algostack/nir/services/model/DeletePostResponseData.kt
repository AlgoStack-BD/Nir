package com.algostack.nir.services.model

data class DeletePostResponseData(
    val acknowledged: Boolean,
    val deletedCount: Int
)