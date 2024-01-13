package com.algostack.nir.services.api

import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PublicPostApi {
    @GET("/all-posts")
    suspend fun getPublicPost() : Response<PublicPostResponse>

    @GET ("/single-user/:id")
    suspend fun getSingleUserPost() : Response<PublicPostResponse>

    @POST ("/create-post")
    suspend fun createPost(@Body createPost: CreatePost) : Response<CreatePostResponse>

    @Multipart
    @POST ("/upload-image")
    suspend fun uploadImage(@Part("files") listofimage: MutableList<MultipartBody.Part>  ) : Response<UploadImageResponse>


}