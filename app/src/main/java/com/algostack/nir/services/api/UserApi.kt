package com.algostack.nir.services.api

import com.algostack.nir.services.model.UploadImageResponse
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.UserResponse
import com.algostack.nir.services.model.UserSigninRequest
import com.algostack.nir.services.model.UserUpdateRequest
import com.algostack.nir.services.model.userUpdateRequestResponse
import dagger.Provides
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface UserApi {

    @POST("/register")
    suspend fun signup(@Body userRequest: UserRequest) : Response<UserResponse>

    @POST("/login")
    suspend fun signin(@Body userSigninRequest: UserSigninRequest) : Response<UserResponse>


    @PUT("/update-user/{_id}")
    suspend fun updateUserInfo(
        @Path("_id") id: String,
        @Body updateRequest: UserUpdateRequest

    ): Response<userUpdateRequestResponse>



}