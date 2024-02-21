package com.algostack.nir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.CreatePostResponse
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

    fun rentRequestNotification(rentRequestNotification: RentRequestNotification) {
        viewModelScope.launch {
            notificationRepository.sendRentRequestNotification(rentRequestNotification)
        }
    }



}