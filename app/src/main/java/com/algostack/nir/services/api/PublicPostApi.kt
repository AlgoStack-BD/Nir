package com.algostack.nir.services.api

import com.algostack.nir.services.model.PublicPostResponse
import retrofit2.Response
import retrofit2.http.GET

interface PublicPostApi {
    @GET("/all-posts")
    suspend fun getPublicPost() : Response<PublicPostResponse>

}