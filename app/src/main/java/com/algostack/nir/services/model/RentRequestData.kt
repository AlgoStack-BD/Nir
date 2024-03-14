package com.algostack.nir.services.model

data class RentRequestData(
    val clientName: String,
    val clientNumber: String,
    val clientimg : String,
    val from: String,
    val meetingDate: String,
    val meetingTime: String,
    val fromRead: Boolean,
    val postId: String,
    val postTitle: String,
    val status: String,
    val senderFrom: String,
    val senderTo: String,
    val to: String,
    val toRead: Boolean,
    val location : String,
    val notifyFrom : Boolean,
    val notifyTo : Boolean
)