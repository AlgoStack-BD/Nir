package com.algostack.nir.services.model

data class NotificationResponse(
    val `data`: List<NotificationResponseData>,
    val status: Int
)