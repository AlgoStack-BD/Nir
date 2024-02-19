package com.algostack.nir.services.model

data class DataXXX(
    val acknowledged: Boolean,
    val matchedCount: Int,
    val modifiedCount: Int,
    val upsertedCount: Int,
    val upsertedId: Any
)