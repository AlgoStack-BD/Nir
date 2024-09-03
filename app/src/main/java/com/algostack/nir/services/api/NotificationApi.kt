package com.algostack.nir.services.api

import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.NotificatinUpdateRespne
import com.algostack.nir.services.model.NotificationDeleteResponse
import com.algostack.nir.services.model.NotificationResponse
import com.algostack.nir.services.model.NotificationUpdateRequest
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.RentRequestNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {

    @POST("/create-notification")
    suspend fun sendRentRequestNotification(
        @Body rentRequestNotification: RentRequestNotification
    ): Response<CreatePostResponse>





    // /user-notifications/656f1d253449d567288c1a22
    @GET("/to-notifications/{userId}")
    suspend fun getToNotifications(
        @Path("userId") id: String
    ): Response<NotificationResponse>

    @GET("/from-notifications/{userId}")
    suspend fun getFromNotifications(
        @Path("userId") id: String
    ): Response<NotificationResponse>

    @GET("/single-notification/{userId}")
    suspend fun getSingleNotification(
        @Path("userId") id: String
    ): Response<PublicPostResponse>

    // get all notifications
    @GET("/all-notifications")
    suspend fun getAllNotifications(): Response<NotificationResponse>





    // /delete-notification/65b936c2b8a49b08dfe34135
    @DELETE("/delete-notification/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") id: String
    ): Response<NotificationDeleteResponse>

   // /update-notification/65b9347d4e32682dfd36df7c
    @PATCH("/update-notification/{notificationId}")
    suspend fun updateNotification(
       @Path("notificationId") id: String,
       @Body notificationUpdateRequest: NotificationUpdateRequest
    ): Response<NotificatinUpdateRespne>

}