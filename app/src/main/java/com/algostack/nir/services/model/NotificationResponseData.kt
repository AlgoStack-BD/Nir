package com.algostack.nir.services.model

data class NotificationResponseData(
    val _id: String,
    val clientName: String,
    val clientImage: String,
    val clientNumber: String,
    val createdAt: String,
    val meetingDate: String,
    val meetingTime: String,
    val ownerId: String,
    val ownerRead: Boolean,
    val postId: String,
    val postTitle: String,
    val status: String,
    val updatedAt: String,
    val userId: String,
    val userRead: Boolean,
    val location: String
)