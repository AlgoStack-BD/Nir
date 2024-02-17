package com.algostack.nir.services.api

import com.algostack.nir.services.model.DeletePostResponseData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.deletePostResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {
    @GET("/single-post-by-userId/{id}")
    suspend fun getSingleUserPost(@Path("id") id: String) : Response<PublicPostResponse>

    @DELETE("/delete-post/{id}")
    suspend fun deletePost(@Path("id") _id: String) : Response<deletePostResponse>
}