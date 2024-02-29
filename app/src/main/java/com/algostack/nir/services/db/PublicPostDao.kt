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
    @Query("SELECT * FROM PublicPostData WHERE " +
            "(price BETWEEN :minPrice AND :maxPrice) OR " +
            "(price = :fixedPrice) OR " +
            "(bedRoom = :bedRoom) OR " +
            "(bathRoom = :bathRoom) OR " +
            "(type = :propertyType) OR " +
            "(location LIKE '%' || :propertyLocation || '%')")
    fun searchItems(
        minPrice: Int,
        maxPrice: Int,
        fixedPrice: Int,
        bedRoom: Int,
        bathRoom: Int,
        propertyType: String,
        propertyLocation: String
    ): List<PublicPostData>


    @Query("SELECT * FROM PublicPostData WHERE bedRoom = :bedRoom")
    fun searchItemsByBedRoom(bedRoom: Int): List<PublicPostData>

    @Query("SELECT * FROM PublicPostData WHERE type = :category")
    fun searchItemsCategoryBedRoom(category: String): List<PublicPostData>

    @Delete
    suspend fun deletePublicPostData(publicPostData: PublicPostData)

    // Delete all data
    @Query("DELETE FROM PublicPostData")
    suspend fun deleteAllPublicPostData()




}