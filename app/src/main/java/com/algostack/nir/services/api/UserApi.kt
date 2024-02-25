package com.algostack.nir.services.api

import com.algostack.nir.services.model.SignupResponse
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.UserResponse
import com.algostack.nir.services.model.UserSigninRequest
import com.algostack.nir.services.model.UserUpdateRequest
import com.algostack.nir.services.model.userUpdateRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserApi {

    @POST("/register")
    suspend fun signup(@Body userRequest: UserRequest) : Response<SignupResponse>

    @POST("/login")
    suspend fun signin(@Body userSigninRequest: UserSigninRequest) : Response<UserResponse>


    @PUT("/update-user/{id}")
    suspend fun updateUserInfo(
        @Path("id") _id: String,
        @Body updateRequest: UserUpdateRequest
    ): Response<userUpdateRequestResponse>



}