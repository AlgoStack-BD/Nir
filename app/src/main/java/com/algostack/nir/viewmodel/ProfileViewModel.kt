package com.algostack.nir.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.repository.ProfileRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) : ViewModel() {

    val _userPostLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = profileRepository.profileInfoResponseLiveData



    var applicationContext: Context?= null
    fun singleUserPost(){
        viewModelScope.launch {
            applicationContext?.let {
                profileRepository.singleUserPost(it)
            }
        }
    }
}