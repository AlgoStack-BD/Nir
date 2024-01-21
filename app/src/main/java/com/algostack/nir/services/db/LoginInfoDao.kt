package com.algostack.nir.services.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.algostack.nir.services.model.userResponseData

@Dao
interface LoginInfoDao {

    @Insert
    suspend fun upsert(userResponseData: userResponseData)

    @Query("SELECT name FROM LoginInfo")
    suspend fun getName(): String?

    @Query("SELECT image FROM LoginInfo")
    suspend fun getImage(): String?

    @Query("DELETE FROM LoginInfo")
    suspend fun deleteLoginInfo()

}