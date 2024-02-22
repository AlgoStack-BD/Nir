package com.algostack.nir.services.model

data class NotificationDeleteData(
    val acknowledged: Boolean,
    val deletedCount: Int
)