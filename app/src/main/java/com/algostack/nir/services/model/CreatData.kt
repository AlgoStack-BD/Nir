package com.algostack.nir.services.model

data class CreatData(
    val additionalMessage: String,
    val balcony: Int,
    val bathRoom: Int,
    val bedRoom: Int,
    val bills: BillsX,
    val diningRoom: Int,
    val drawingRoom: Int,
    val img: String,
    val isAdminPost: Boolean,
    val isApproved: Boolean,
    val isNegotiable: Boolean,
    val isPublicNumber: Boolean,
    val isSold: Boolean,
    val kitchen: Int,
    val likeCount: Int,
    val location: String,
    val price: Int,
    val type: String,
    val userId: String,
    val userName: String,
    val phoneNumber: String,
    val isAds: Boolean,
    val title: String,
    val userImage: String

)