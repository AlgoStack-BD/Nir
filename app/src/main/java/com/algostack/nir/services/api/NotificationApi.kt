package com.algostack.nir.services.api

import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.NotificationDeleteResponse
import com.algostack.nir.services.model.NotificationResponse
import com.algostack.nir.services.model.RentRequestNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {

    @POST("create-notification")
    suspend fun sendRentRequestNotification(
        @Body rentRequestNotification: RentRequestNotification
    ): Response<CreatePostResponse>



    // /user-notifications/656f1d253449d567288c1a22
    @GET("user-notifications/{userId}")
    suspend fun getUserNotifications(
        @Path("userId") id: String
    ): Response<NotificationResponse>


    // /delete-notification/65b936c2b8a49b08dfe34135
    @GET("delete-notification/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") id: String
    ): Response<NotificationDeleteResponse>

   // /update-notification/65b9347d4e32682dfd36df7c
    @GET("update-notification/{notificationId}")
    suspend fun updateNotification(
       @Path("notificationId") id: String
    ): Response<NotificationDeleteResponse>

}