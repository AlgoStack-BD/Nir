package com.algostack.nir.services.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.algostack.nir.services.model.PublicPostData



@Dao
interface PublicPostDao {


    @Insert
    suspend fun insertPublicPost(publicPostData: List<PublicPostData>)

    @Query("SELECT * FROM PublicPostData")
    suspend fun getPublicPostData(): List<PublicPostData>?

    @Delete
    suspend fun deletePublicPostData(publicPostData: PublicPostData)


}