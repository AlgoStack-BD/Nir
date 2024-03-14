package com.algostack.nir.viewmodel

import android.content.Context
import android.content.LocusId
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.DeletePostResponseData
import com.algostack.nir.services.model.PaymentRequest
import com.algostack.nir.services.model.PaymentRequestResponse
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.deletePostResponse
import com.algostack.nir.services.repository.ProfileRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

    val _userPostLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = profileRepository.profileInfoResponseLiveData


    val deletePostResponseLiveData : LiveData<NetworkResult<deletePostResponse>>
        get() = profileRepository.deletePostResponseLiveData

    val paymentResponseLiveData : LiveData<NetworkResult<PaymentRequestResponse>>
        get() = profileRepository.paymentResponseLiveData



    var applicationContext: Context?= null
    fun singleUserPost(userId: String){
        viewModelScope.launch {
            println("functioncalled0")

            applicationContext?.let {
                profileRepository.singleUserPost(it,userId)

            }
        }
    }

    fun deletePost(id: String){
        viewModelScope.launch {

            profileRepository.deletePost(id)



        }
    }

    fun makePayment(paymentRequest: PaymentRequest){
        println("makePaymentviewmodel: $paymentRequest")
        viewModelScope.launch {
            profileRepository.makePayment(paymentRequest)
        }
    }


}