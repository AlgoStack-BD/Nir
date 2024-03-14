package com.algostack.nir.services.repository

import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.NotificationApi
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.NotificatinUpdateRespne
import com.algostack.nir.services.model.NotificationDeleteResponse
import com.algostack.nir.services.model.NotificationResponse
import com.algostack.nir.services.model.NotificationUpdateRequest
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.RentRequestNotification
import com.algostack.nir.utils.NetworkResult
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationApi: NotificationApi
){

    private val _rentForRequestResponse = MutableLiveData<NetworkResult<CreatePostResponse>> ()
    private val _userNotifications = MutableLiveData<NetworkResult<NotificationResponse>> ()
    private val _deleteNotificationResponse = MutableLiveData<NetworkResult<NotificationDeleteResponse>> ()
    private val _updateNotificationResponse = MutableLiveData<NetworkResult<NotificatinUpdateRespne>> ()
    private val _signlePostResponse = MutableLiveData<NetworkResult<PublicPostResponse>> ()
    val rentForRequestResponse : MutableLiveData<NetworkResult<CreatePostResponse>>
        get() = _rentForRequestResponse

    val userNotifications : MutableLiveData<NetworkResult<NotificationResponse>>
        get() = _userNotifications

    val deleteNotificationResponse : MutableLiveData<NetworkResult<NotificationDeleteResponse>>
        get() = _deleteNotificationResponse

    val updateNotificationResponse : MutableLiveData<NetworkResult<NotificatinUpdateRespne>>
        get() = _updateNotificationResponse

    val signlePostResponse : MutableLiveData<NetworkResult<PublicPostResponse>>
        get() = _signlePostResponse

    suspend fun sendRentRequestNotification(rentRequestNotification: RentRequestNotification) {
        _rentForRequestResponse.postValue(NetworkResult.Loading())

        try {
            val response = notificationApi.sendRentRequestNotification(rentRequestNotification)

            if (response.isSuccessful && response.body() != null) {
                _rentForRequestResponse.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _rentForRequestResponse.postValue(NetworkResult.Error(e.message))
        }
    }

    suspend fun getFromNotifications(userId: String) {
        _userNotifications.postValue(NetworkResult.Loading())

        try {
            val response = notificationApi.getFromNotifications(userId)

            if (response.isSuccessful && response.body() != null) {
                _userNotifications.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _userNotifications.postValue(NetworkResult.Error(e.message))
        }
    }

    suspend fun getToNotifications(userId: String) {
        _userNotifications.postValue(NetworkResult.Loading())

        try {
            val response = notificationApi.getToNotifications(userId)

            if (response.isSuccessful && response.body() != null) {
                _userNotifications.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _userNotifications.postValue(NetworkResult.Error(e.message))
        }
    }






    suspend fun getallNotifications() {
        _userNotifications.postValue(NetworkResult.Loading())

        try {
            val response = notificationApi.getAllNotifications()

            println("NotificationRepository.getallNotifications: response = ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                _userNotifications.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _userNotifications.postValue(NetworkResult.Error(e.message))
        }
    }

    suspend fun deleteNotification(notificationId: String) {
        _deleteNotificationResponse.postValue(NetworkResult.Loading())

        try {
            println("final call notificationId = $notificationId")
            val response = notificationApi.deleteNotification(notificationId)
            println("final call response = ${response.body()}")

            if (response.isSuccessful && response.body() != null) {
                _deleteNotificationResponse.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _deleteNotificationResponse.postValue(NetworkResult.Error(e.message))
        }
    }

    suspend fun updateNotification(notificationId: String, notificationUpdateRequest: NotificationUpdateRequest) {
        _updateNotificationResponse.postValue(NetworkResult.Loading())

        try {
            val response = notificationApi.updateNotification(notificationId, notificationUpdateRequest)

            if (response.isSuccessful && response.body() != null) {
                _updateNotificationResponse.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _updateNotificationResponse.postValue(NetworkResult.Error(e.message))
        }
    }


    suspend fun getSingleNotification(postId: String) {
        _signlePostResponse.postValue(NetworkResult.Loading())

        try {
            println("final call postId = $postId")
            val response = notificationApi.getSingleNotification(postId)

            println("singlePost: response = ${notificationApi.getSingleNotification(postId)}")

            if (response.isSuccessful && response.body() != null) {
                _signlePostResponse.postValue(NetworkResult.Success(response.body()!!))
            }
        }catch (e: Exception) {
            _signlePostResponse.postValue(NetworkResult.Error(e.message))
        }
    }

}