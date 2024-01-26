package com.algostack.nir.services.api

import com.algostack.nir.services.model.PublicPostResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("/single-user/:id")
    suspend fun getSingleUserPost() : Response<PublicPostResponse>
}