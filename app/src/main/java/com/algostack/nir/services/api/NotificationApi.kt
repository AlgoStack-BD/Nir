package com.algostack.nir.services.api

import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.RentRequestNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApi {

    @POST("create-notification")
    suspend fun sendRentRequestNotification(
        @Body rentRequestNotification: RentRequestNotification
    ): Response<CreatePostResponse>

}