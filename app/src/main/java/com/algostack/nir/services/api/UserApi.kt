package com.algostack.nir.services.api

import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.UserResponse
import com.algostack.nir.services.model.UserSigninRequest
import dagger.Provides
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UserApi {

    @POST("/register")
    suspend fun signup(@Body userRequest: UserRequest) : Response<UserResponse>

    @POST("/login")
    suspend fun signin(@Body userSigninRequest: UserSigninRequest) : Response<UserResponse>



}