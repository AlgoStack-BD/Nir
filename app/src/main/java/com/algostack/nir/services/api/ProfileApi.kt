package com.algostack.nir.services.api

import com.algostack.nir.services.model.PublicPostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {
    @GET("/single-post-by-userId/{id}")
    suspend fun getSingleUserPost(@Path("id") id: String) : Response<PublicPostResponse>
}