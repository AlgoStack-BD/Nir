package com.algostack.nir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.NotificatinUpdateRespne
import com.algostack.nir.services.model.NotificationDeleteResponse
import com.algostack.nir.services.model.NotificationResponse
import com.algostack.nir.services.model.NotificationUpdateRequest
import com.algostack.nir.services.model.PublicPostResponse
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

    val userNotificationsTo : LiveData<NetworkResult<NotificationResponse>>
        get() = notificationRepository.userNotificationsTo

    val deleteNotificationResponse : LiveData<NetworkResult<NotificationDeleteResponse>>
        get() = notificationRepository.deleteNotificationResponse

    val updateNotificationResponse : LiveData<NetworkResult<NotificatinUpdateRespne>>
        get() = notificationRepository.updateNotificationResponse

    val signlePostResponse : LiveData<NetworkResult<PublicPostResponse>>
        get() = notificationRepository.signlePostResponse

    fun rentRequestNotification(rentRequestNotification: RentRequestNotification) {
        viewModelScope.launch {
            notificationRepository.sendRentRequestNotification(rentRequestNotification)
        }
    }

    fun getFromNotifications(userId: String) {
        viewModelScope.launch {
            notificationRepository.getFromNotifications(userId)
        }
    }

    fun getToNotifications(userId: String) {
        viewModelScope.launch {
            notificationRepository.getToNotifications(userId)
        }
    }








    fun getallNotifications() {
        viewModelScope.launch {
            notificationRepository.getallNotifications()
        }
    }

    fun deleteNotification(notificationId: String) {

        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId)
        }
    }

    fun updateNotification(notificationId: String, notificationUpdateRequest: NotificationUpdateRequest) {

        viewModelScope.launch {
            notificationRepository.updateNotification(notificationId, notificationUpdateRequest)
        }
    }

    fun getSingleNotification(postId: String) {

        println("called postId = $postId")
        viewModelScope.launch {
            notificationRepository.getSingleNotification(postId)
        }
    }


}