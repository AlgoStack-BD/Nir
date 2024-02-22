package com.algostack.nir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.NotificatinUpdateRespne
import com.algostack.nir.services.model.NotificationDeleteResponse
import com.algostack.nir.services.model.NotificationResponse
import com.algostack.nir.services.model.NotificationUpdateRequest
import com.algostack.nir.services.model.RentRequestNotification
import com.algostack.nir.services.repository.NotificationRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepository: NotificationRepository) : ViewModel() {


  val rentForRequestResponse : LiveData<NetworkResult<CreatePostResponse>>
    get() = notificationRepository.rentForRequestResponse

    val userNotifications : LiveData<NetworkResult<NotificationResponse>>
        get() = notificationRepository.userNotifications

    val deleteNotificationResponse : LiveData<NetworkResult<NotificationDeleteResponse>>
        get() = notificationRepository.deleteNotificationResponse

    val updateNotificationResponse : LiveData<NetworkResult<NotificatinUpdateRespne>>
        get() = notificationRepository.updateNotificationResponse

    fun rentRequestNotification(rentRequestNotification: RentRequestNotification) {
        viewModelScope.launch {
            notificationRepository.sendRentRequestNotification(rentRequestNotification)
        }
    }

    fun getNotifications(userId: String) {
        println("called userId = $userId")
        viewModelScope.launch {
            notificationRepository.getUserNotifications(userId)
        }
    }

    fun deleteNotification(notificationId: String) {
        println("called notificationId = $notificationId")
        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId)
        }
    }

    fun updateNotification(notificationId: String, notificationUpdateRequest: NotificationUpdateRequest) {
        println("called notificationId = $notificationId")
        viewModelScope.launch {
            notificationRepository.updateNotification(notificationId, notificationUpdateRequest)
        }
    }



}