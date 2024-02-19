package com.algostack.nir.services.api

import com.algostack.nir.services.model.BooleanCheck
import com.algostack.nir.services.model.FavouriteRequest
import com.algostack.nir.services.model.FavouriteResponse
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.RemoveFavouriteItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FavouriteAPI {
    @POST("/create-favorite")
    suspend fun createFavorite(@Body favorite: FavouriteRequest) : Response<FavouriteResponse>



    @GET("/user-favorites/{id}")
    suspend fun getUserFavourite(@Path("id") id: String) : Response<PublicPostResponse>

    @PATCH("/update-favorite/{id}")
    suspend fun updateFavorite(@Path("id") id: String, @Body removeFavouriteItem: RemoveFavouriteItem ) : Response<FavouriteResponse>


    @GET("/specific-favorite/{userId}/{postId}")
    suspend fun getSpecificFavourite(@Path("userId") userId: String, @Path("postId") postId: String) : Response<BooleanCheck>
}