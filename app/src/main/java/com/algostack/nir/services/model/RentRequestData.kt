package com.algostack.nir.services.model

data class RentRequestData(
    val clientName: String,
    val clientNumber: String,
    val meetingDate: String,
    val meetingTime: String,
    val ownerId: String,
    val ownerRead: Boolean,
    val postId: String,
    val postTitle: String,
    val status: String,
    val userId: String,
    val userRead: Boolean
)