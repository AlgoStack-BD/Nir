package com.algostack.nir.services.api

import com.algostack.nir.services.model.ForgetPasswordRequest
import com.algostack.nir.services.model.UpdateStatusRequest
import com.algostack.nir.services.model.UserResponse
import com.algostack.nir.services.model.VerificationRequest
import com.algostack.nir.services.model.VerificationResponse
import com.algostack.nir.services.model.VerifyOTPResponse
import com.algostack.nir.services.model.VerifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VerificationAPI {
    @POST("/getVerificationCode")
    suspend fun verification(@Body verificationRequest: VerificationRequest) : Response<VerificationResponse>

    @POST("/verifyOTP")
    suspend fun vedifyOTP(@Body verifyOtpRequest: VerifyRequest) : Response<VerifyOTPResponse>

    @PUT("/update-user/{id}")
    suspend fun upVerifyStatus(@Path("id") id: String, @Body updateStatusRequest: UpdateStatusRequest) : Response<UserResponse>

//    @GET("/all-users")
//    suspend fun getAllUsers() : Response<AllUsersResponse>

    @PUT("/reset-password")
    suspend fun ResetPassword(@Body forgetPasswordRequest: ForgetPasswordRequest) : Response<UserResponse>
}