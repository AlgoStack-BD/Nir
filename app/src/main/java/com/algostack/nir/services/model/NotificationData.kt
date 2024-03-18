package com.algostack.nir.services.model

data class NotificationData(
    val fromRead: Boolean,
    val toRead: Boolean,
    val status: String,
    val senderFrom : String
)