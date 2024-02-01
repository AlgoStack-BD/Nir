package com.algostack.nir.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "PublicPostData"
)
data class PublicPostData(

    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val _id: String,
    val additionalMessage: String,
    val balcony: Int?,
    val bathRoom: Int,
    val bedRoom: Int,
    val bills: Bills,
    val createdAt: String?,
    val diningRoom: Int?,
    val drawingRoom: Int?,
    val img: String,
    val isAdminPost: Boolean,
    val isApproved: Boolean,
    val isNegotiable: Boolean,
    val isPublicNumber: Boolean?,
    val isSold: Boolean,
    val kitchen: Int,
    val likeCount: Int,
    val location: String,
    val phone: String?,
    val price: Int,
    val type: String,
    val updatedAt: String?,
    val userId: String,
    val userImg: String?,
    val userName: String?,
    val phoneNumber: String,
    val isAds : Boolean,


)