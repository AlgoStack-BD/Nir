package com.algostack.nir.services.repository

import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.NotificationApi
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.RentRequestNotification
import com.algostack.nir.utils.NetworkResult
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationApi: NotificationApi
){

    private val _rentForRequestResponse = MutableLiveData<NetworkResult<CreatePostResponse>> ()

    val rentForRequestResponse : MutableLiveData<NetworkResult<CreatePostResponse>>
        get() = _rentForRequestResponse

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


}