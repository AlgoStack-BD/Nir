package com.algostack.nir.services.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(
    tableName = "LoginInfo"
)
data class userResponseData (

    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val email: String,
    val image: String,
    val isAdmin: Boolean,
    val isVerified: Boolean,
    val location: String,
    val name: String,
    val password: String,
    val phone: Any,
    val rentSuccess: Int,
    val totoalPost: Int

)