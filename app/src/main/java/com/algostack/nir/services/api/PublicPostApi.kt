package com.algostack.nir.services.api

import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.PublicPostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PublicPostApi {
    @GET("/all-posts")
    suspend fun getPublicPost() : Response<PublicPostResponse>

    @GET ("/single-user/:id")
    suspend fun getSingleUserPost() : Response<PublicPostResponse>

    @POST ("/create-post")
    suspend fun createPost(@Body createPost: CreatePost) : Response<PublicPostResponse>


}